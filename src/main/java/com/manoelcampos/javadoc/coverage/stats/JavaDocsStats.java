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

import com.manoelcampos.javadoc.coverage.stats.ClassDocStats;
import com.manoelcampos.javadoc.coverage.stats.PackagesDocStats;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import java.util.*;

/**
 * Computes statistics about the coverage of JavaDocs comments inside the
 * Java files received by the JavaDoc tool.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class JavaDocsStats {
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

    public List<ClassDocStats> getClassesDocStats() {
        return Collections.unmodifiableList(classesDocStats);
    }
}
