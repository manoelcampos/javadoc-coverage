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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

/**
 * Computes JavaDoc coverage statistics for Java files received by the JavaDoc tool.
 *
 * <p>It's the main class to store JavaDocs statistics for any kind of
 * documentable element into a Java source file.
 * It includes the computed statistics for an entire Java project
 * parsed by the JavaDoc Tool.</p>
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class JavaDocsStats implements DocStats {

    private final List<PackagesDocStats> packagesDocStats;

    /**
     * Instantiates an object to compute JavaDoc coverage statistics for all Java files received by the JavaDoc tool.
     *
     * @param rootDoc
     *            root element which enables reading JavaDoc documentation
     * @param computeOnlyForPublic
     *            indicates that coverage should only be compute for the public part of the javadoc
     */
    public JavaDocsStats(final RootDoc rootDoc, boolean computeOnlyForPublic) {
        this.packagesDocStats = new ArrayList<>();

        Map<PackageDoc, PackagesDocStats> tmp = new HashMap<>();
        for (final ClassDoc doc : rootDoc.classes()) {
            // add all packages regardless of public/whatever classes in it
            if (!tmp.containsKey(doc.containingPackage())) {
                PackagesDocStats pkgDoc = new PackagesDocStats(doc.containingPackage(), computeOnlyForPublic);
                tmp.put(doc.containingPackage(), pkgDoc);
            }

            // only add necessary classes depending on options
            if (!computeOnlyForPublic || doc.isPublic()) {
                tmp.get(doc.containingPackage()).addClass(doc);
            }
        }
        packagesDocStats.addAll(tmp.values());
    }

    /**
     * Gets the object containing JavaDoc coverage statistics for detected packages.
     *
     * @return packages' JavaDoc coverage statistics
     */
    public List<PackagesDocStats> getPackagesDocStats() {
        return packagesDocStats;
    }

    @Override
    public String getType() {
        return "Project JavaDoc Statistics";
    }

    @Override
    public long getNumberOfDocumentedMembers() {
        return packagesDocStats.stream().mapToLong(DocStats::getNumberOfDocumentedMembers).sum();
    }

    @Override
    public long getNumberOfDocumentableMembers() {
        return packagesDocStats.stream().mapToLong(DocStats::getNumberOfDocumentableMembers).sum();
    }
}
