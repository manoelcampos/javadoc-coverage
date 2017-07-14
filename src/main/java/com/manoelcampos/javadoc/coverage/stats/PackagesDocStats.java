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
    public Stream<String> getDocumentedMembersComments() {
        return packagesDoc.stream().map(PackageDoc::getRawCommentText);
    }

    public Set<PackageDoc> getPackagesDoc() {
        return Collections.unmodifiableSet(packagesDoc);
    }
}
