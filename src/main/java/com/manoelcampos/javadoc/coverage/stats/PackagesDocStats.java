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

import com.sun.javadoc.PackageDoc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Computes statistics about the JavaDocs of packages.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class PackagesDocStats extends MembersDocStats {
    private final Set<PackageDoc> packagesDoc;

    public PackagesDocStats(){
        this.packagesDoc = new HashSet<>();
    }

    public boolean addPackageDoc(final PackageDoc doc){
        return packagesDoc.add(doc);
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
    public Stream<String> getMembersComments() {
        return packagesDoc.stream().map(PackageDoc::getRawCommentText);
    }

    public Set<PackageDoc> getPackagesDoc() {
        return Collections.unmodifiableSet(packagesDoc);
    }
}
