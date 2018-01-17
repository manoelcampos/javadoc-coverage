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
import com.sun.javadoc.PackageDoc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Computes JavaDoc statistics for a set of packages.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class PackagesDocStats extends MembersDocStats {
    private final Set<PackageDoc> packagesDoc;

    public PackagesDocStats(){
        this.packagesDoc = new HashSet<>();
    }

    /**
     * Adds an element to the Set of elements containing packages' JavaDocs.
     *
     * @param doc the package's JavaDoc element to add to the Set
     */
    public void addPackageDoc(final PackageDoc doc){
        packagesDoc.add(doc);
    }

    @Override
    public String getType() {
        return "Packages";
    }

    @Override
    public long getMembersNumber() {
        return packagesDoc.size();
    }

    @Override
    public long getDocumentedMembers() {
        return packagesDoc.stream().map(PackageDoc::getRawCommentText).filter(Utils::isNotStringEmpty).count();
    }

    /**
     * Gets a Set of elements containing packages' JavaDocs.
     *
     * @return the Set of packages' JavaDocs
     */
    public Set<PackageDoc> getPackagesDoc() {
        return Collections.unmodifiableSet(packagesDoc);
    }

    /**
     * A set of packages doesn't have documentation,
     * only each individual package may have.
     * @return
     */
    @Override
    public boolean isDocumented() {
        return false;
    }
}
