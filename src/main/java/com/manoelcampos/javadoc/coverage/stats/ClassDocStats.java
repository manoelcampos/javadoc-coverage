/*
 * Copyright 2017-2017 Manoel Campos da Silva Filho
 *
 * Licensed under the General Public License Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.gnu.org/licenses/gpl-3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.manoelcampos.javadoc.coverage.stats;

import java.util.*;

import com.manoelcampos.javadoc.coverage.Utils;
import com.manoelcampos.javadoc.coverage.configuration.Configuration;
import com.sun.javadoc.*;

/**
 * Computes statistics about the JavaDocs of a class, inner class, interface or enum
 * and its members, such as: fields, methods, constructors and annotations.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class ClassDocStats extends MembersDocStats {
    /**
     * This value is added to the number of elements in order to count the class itself as an element
     * which can be documented.
     *
     * @see #getMembersNumber()
     */
    private static final int CLASS_DOC = 1;

    private final ClassDoc doc;
    private final ClassMembersDocStats fieldsStats;
    private final ClassMembersDocStats enumsStats;
    private ClassMembersDocStats annotationsStats;

    private List<MethodDocStats> methodsStats;
    private List<MethodDocStats> constructorsStats;

    public ClassDocStats(final ClassDoc doc, Configuration config) {
        this.doc = doc;
        fieldsStats = new ClassMembersDocStats(doc.fields(false), "Fields", config);
        enumsStats = new ClassMembersDocStats(doc.enumConstants(), "Enum Consts", config);
        processMethodsDocsStats(doc, config.computePublicCoverageOnly());
        processConstructorsDocsStats(doc, config.computePublicCoverageOnly());
        processAnnotationsDocsStats(doc, config);
    }

    private void processAnnotationsDocsStats(ClassDoc doc, Configuration config) {
        if (doc instanceof AnnotationTypeDoc) {
            annotationsStats = new ClassMembersDocStats(((AnnotationTypeDoc) doc).elements(), "Annotations", config);
        } else {
            annotationsStats = new ClassMembersDocStats(new AnnotationTypeElementDoc[0], "Annotations", config);
        }
    }

    private void processConstructorsDocsStats(ClassDoc doc, boolean computeOnlyForPublic) {
        final ConstructorDoc[] constructors = doc.constructors(false);
        constructorsStats = new ArrayList<>();
        for (final ConstructorDoc constructor : constructors) {
            if (!computeOnlyForPublic || constructor.isPublic()) {
                constructorsStats.add(new MethodDocStats(constructor));
            }
        }
    }

    private void processMethodsDocsStats(ClassDoc doc, boolean computeOnlyForPublic) {
        final MethodDoc[] methods = doc.methods(false);
        methodsStats = new ArrayList<>();
        for (final MethodDoc method : methods) {
            if (!computeOnlyForPublic || method.isPublic()) {
                methodsStats.add(new MethodDocStats(method));
            }
        }
    }

    @Override
    public long getDocumentedMembers() {
        return Utils.boolToInt(isDocumented()) +
                fieldsStats.getDocumentedMembers() +
                enumsStats.getDocumentedMembers() +
                getDocumentedMethodMembers(methodsStats) +
                getDocumentedMethodMembers(constructorsStats) +
                annotationsStats.getDocumentedMembers();
    }

    @Override
    public long getMembersNumber() {
        return CLASS_DOC +
                fieldsStats.getMembersNumber() +
                enumsStats.getMembersNumber() +
                getMethodMembers(methodsStats) +
                getMethodMembers(constructorsStats) +
                annotationsStats.getMembersNumber();
    }

    private long getDocumentedMethodMembers(final List<MethodDocStats> methodOrConstructor) {
        return methodOrConstructor.stream().filter(MethodDocStats::isDocumented).count() +
                methodOrConstructor.stream().mapToLong(MethodDocStats::getDocumentedMembers).sum();
    }

    /**
     * Gets the amount of documentable members from a given list of methods/constructors.
     *
     * @param methodOrConstructor a list containing the methods and constructors to get their number of members
     * @return the total number of members for the given list of methods/constructors
     * @see MethodDocStats#getMembersNumber()
     */
    private long getMethodMembers(final List<MethodDocStats> methodOrConstructor) {
        return methodOrConstructor
                .stream()
                .mapToLong(MethodDocStats::getMembersNumber)
                .sum();
    }

    public String getName() {
        return doc.name();
    }

    public String getPackageName() {
        return doc.containingPackage().name();
    }

    @Override
    public String getType() {
        return doc.isInterface() ? "Interface" : doc.isEnum() ? "Enum" : "Class";
    }

    public ClassMembersDocStats getFieldsStats() {
        return fieldsStats;
    }

    public ClassMembersDocStats getEnumsStats() {
        return enumsStats;
    }

    public ClassMembersDocStats getAnnotationsStats() {
        return annotationsStats;
    }

    public List<MethodDocStats> getMethodsStats() {
        return Collections.unmodifiableList(methodsStats);
    }

    public List<MethodDocStats> getConstructorsStats() {
        return Collections.unmodifiableList(constructorsStats);
    }

    public ClassDoc getDoc() {
        return doc;
    }

    @Override
    public boolean isDocumented() {
        return Utils.isElementDocumented(doc.getRawCommentText());
    }
}
