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

import com.manoelcampos.javadoc.coverage.Utils;
import com.sun.javadoc.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Computes statistics about the JavaDocs of a class, inner class, interface or enum
 * and its members, such as: fields, methods, constructors and annotations.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class ClassDocStats implements CompoundedDocStats {
    private final ClassDoc doc;
    private final ClassMembersDocStats fieldsStats;
    private final ClassMembersDocStats enumsStats;
    private List<MethodDocStats> methodsStats;
    private List<MethodDocStats> constructorsStats;
    private ClassMembersDocStats annotationsStats;

    public ClassDocStats(final ClassDoc doc) {
        this.doc = doc;
        fieldsStats = new ClassMembersDocStats(doc.fields(false), "Fields");
        enumsStats = new ClassMembersDocStats(doc.enumConstants(), "Enum Consts");
        processMethodsDocsStats(doc);
        processConstructorsDocsStats(doc);
        processAnnotationsDocsStats(doc);
    }

    private void processAnnotationsDocsStats(ClassDoc doc) {
        if (doc instanceof AnnotationTypeDoc) {
            annotationsStats = new ClassMembersDocStats(((AnnotationTypeDoc) doc).elements(), "Annotations");
        } else annotationsStats = new ClassMembersDocStats(new AnnotationTypeElementDoc[0], "Annotations");
    }

    private void processConstructorsDocsStats(ClassDoc doc) {
        final ConstructorDoc[] constructors = doc.constructors(false);
        constructorsStats = new ArrayList<>(constructors.length);
        for (final ConstructorDoc constructor : constructors) {
            constructorsStats.add(new MethodDocStats(constructor));
        }
    }

    private void processMethodsDocsStats(ClassDoc doc) {
        final MethodDoc[] methods = doc.methods(false);
        methodsStats = new ArrayList<>(methods.length);
        for (final MethodDoc method : methods) {
            methodsStats.add(new MethodDocStats(method));
        }
    }

    @Override
    public double getDocumentedMembersPercent(){
        final double membersNumber =
                1 + //this 1 is used to count the class as a element which can be documented or not
                fieldsStats.getMembersNumber() +
                enumsStats.getMembersNumber() +
                methodsStats.size() +
                constructorsStats.size() +
                annotationsStats.getMembersNumber();

        /*
         * @todo the documentation of params, return value and throws must
         * count to the total documentation percentage.
         * It's being considered only the documentation of the method itself.
        */
        final double documentedMembers =
                (hasDocumentation() ? 1 : 0) +
                        fieldsStats.getDocumentedMembers() +
                        enumsStats.getDocumentedMembers() +
                        getDocumentedMembersCount(methodsStats.stream().map(m -> m.getDoc().getRawCommentText())) +
                        getDocumentedMembersCount(constructorsStats.stream().map(m -> m.getDoc().getRawCommentText())) +
                        annotationsStats.getDocumentedMembers();

        return Utils.computePercentage(documentedMembers, membersNumber);

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

    @Override
    public boolean hasDocumentation() {
        return Utils.isNotStringEmpty(doc.getRawCommentText());
    }

    @Override
    public Doc getDoc() {
        return doc;
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
}
