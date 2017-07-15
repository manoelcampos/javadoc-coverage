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
    private ClassMembersDocStats annotationsStats;

    private List<MethodDocStats> methodsStats;
    private List<MethodDocStats> constructorsStats;

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
        return Utils.mean(
                Utils.boolToInt(hasDocumentation())*100,
                fieldsStats.getDocumentedMembersPercent(),
                enumsStats.getDocumentedMembersPercent(),
                getDocumentedMethodsPercent(),
                getDocumentedConstructorsPercent(),
                annotationsStats.getDocumentedMembersPercent());
    }

    public double getDocumentedMethodsPercent(){
        return Utils.average(methodsStats.stream().mapToDouble(MethodDocStats::getDocumentedMembersPercent));
    }

    public double getDocumentedConstructorsPercent(){
        return Utils.average(constructorsStats.stream().mapToDouble(MethodDocStats::getDocumentedMembersPercent));
    }

    @Override
    public long getMembersNumber() {
        return fieldsStats.getMembersNumber() +
                enumsStats.getMembersNumber() +
                methodsStats.size() +
                constructorsStats.size() +
                annotationsStats.getMembersNumber();
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
