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
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;

/**
 * Computes JavaDoc statistics for a set of packages.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class PackageDocStats extends MembersDocStats {
    private final PackageDoc packageDoc;
    private final Configuration config;
    private final List<ClassDocStats> classDocs = new ArrayList<>();

    public PackageDocStats(final PackageDoc doc, Configuration config) {
        this.packageDoc = doc;
        this.config = config;
    }

    /**
     * Adds an element to the Set of elements containing packages' JavaDocs.
     *
     * @param doc the package's JavaDoc element to add to the Set
     */
    public void addClass(final ClassDoc doc) {
        if (!packageDoc.equals(doc.containingPackage())) {
            throw new IllegalArgumentException("Class is not in the correct package");
        }
        classDocs.add(new ClassDocStats(doc, config));
    }

    public List<ClassDocStats> getClassDocs() {
        return Collections.unmodifiableList(classDocs);
    }

    @Override
    public String getType() {
        return "Package";
    }

    public String getName() {
        return packageDoc.name();
    }

    @Override
    public long getNumberOfDocumentedMembers() {
        return classDocs.stream().mapToLong(DocStats::getNumberOfDocumentedMembers).sum() + Utils.boolToInt(isDocumented());
    }

    @Override
    public long getNumberOfDocumentableMembers() {
        // +1 for the package-info documentation
        return classDocs.stream().mapToLong(DocStats::getNumberOfDocumentableMembers).sum() + 1;
    }

    @Override
    public boolean isDocumented() {
        return Utils.isNotStringEmpty(packageDoc.getRawCommentText());
    }

}
