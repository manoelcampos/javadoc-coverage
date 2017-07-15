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
import com.manoelcampos.javadoc.coverage.stats.ClassDocStats;
import com.manoelcampos.javadoc.coverage.stats.PackagesDocStats;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

import java.util.*;

/**
 * Computes statistics about the coverage of JavaDocs comments inside the
 * Java files received by the JavaDoc tool.
 *
 * <p>It's the main class to store JavaDocs statistics for any kind of
 * documentable element into a Java source file.
 * It include the computed statistics for an entire Java project
 * parsed by the JavaDoc Tool.</p>
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class JavaDocsStats implements DocStats {
    /**
     * Stores the root of the program structure information.
     */
    private final RootDoc rootDoc;

    private final PackagesDocStats packagesDocStats;
    private final List<ClassDocStats> classesDocStats;

    public JavaDocsStats(final RootDoc rootDoc) {
        this.rootDoc = rootDoc;
        this.classesDocStats = computeClassesDocsStats();
        this.packagesDocStats = computePackagesDocsStats();
    }

    private List<ClassDocStats> computeClassesDocsStats() {
        final List<ClassDocStats> stats = new ArrayList<>(rootDoc.classes().length);
        for (final ClassDoc doc : rootDoc.classes()) {
            stats.add(new ClassDocStats(doc));
        }

        return stats;
    }

    private PackagesDocStats computePackagesDocsStats() {
        final PackagesDocStats stats = new PackagesDocStats();
        for (final ClassDoc doc : rootDoc.classes()) {
            stats.addPackageDoc(doc.containingPackage());
        }

        return stats;
    }

    public PackagesDocStats getPackagesDocStats() {
        return packagesDocStats;
    }

    public long getClassesNumber(){
        return classesDocStats.size();
    }

    public long getPackagesNumber(){
        return packagesDocStats.getPackagesDoc().size();
    }

    public List<ClassDocStats> getClassesDocStats() {
        return Collections.unmodifiableList(classesDocStats);
    }

    /**
     * Gets the percentage of documented {@link #getClassesDocStats() classes}.
     *
     * @return the percentage of documented classes, in scale from 0 to 100.
     */
    public double getDocumentedClassesPercent(){
        return Utils.computePercentage(getDocumentedClasses(), getClassesNumber());
    }

    public long getDocumentedClasses(){
        return classesDocStats.stream().filter(ClassDocStats::hasDocumentation).count();
    }

    public long getUndocumentedClasses(){
        return classesDocStats.size() - getDocumentedClasses();
    }

    /**
     * Gets the percentage of documented {@link #getPackagesDocStats() packages}.
     *
     * @return the percentage of documented packages, in scale from 0 to 100.
     */
    public double getDocumentedPackagesPercent(){
        return Utils.computePercentage(getDocumentedPackages(), getPackagesNumber());
    }

    public long getDocumentedPackages(){
        return getDocumentedMembersCount(packagesDocStats.getPackagesDoc().stream().map(PackageDoc::getRawCommentText));
    }

    public long getUndocumentedPackages(){
        return packagesDocStats.getPackagesDoc().size() - getDocumentedPackages();
    }

    @Override
    public String getType() {
        return "Project JavaDoc Statistics";
    }
}
