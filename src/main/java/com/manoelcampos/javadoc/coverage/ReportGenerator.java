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
package com.manoelcampos.javadoc.coverage;

import com.manoelcampos.javadoc.coverage.stats.ClassDocStats;
import com.manoelcampos.javadoc.coverage.stats.MembersDocStats;
import com.manoelcampos.javadoc.coverage.stats.MethodDocStats;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A renderer class that actually exports javadoc comments.
 * It is used when the {@link CoverageDoclet} is started.
 *
 * @author Manoel Campos da Silva Filho
 */
class ReportGenerator {
    /**
     * Stores the root of the program structure information.
     */
    private final RootDoc rootDoc;

    ReportGenerator(final RootDoc rootDoc) {
        this.rootDoc = rootDoc;
    }

    /**
     * Exports classes and packages javadocs, inside a {@link RootDoc} object.
     *
     * @return true if successful, false otherwise
     */
    boolean start() {
        try (PrintWriter writer = new PrintWriter(System.out)) {
            final Set<PackageDoc> packages = exportClassesDoc(writer);
            exportPackagesDoc(writer, packages);
            writer.flush();
        }

        return true;
    }

    private void exportPackagesDoc(PrintWriter writer, Set<PackageDoc> packages) {
        writer.println("Packages");
        packages.forEach(pkg -> exportPackageDoc(pkg, writer));
        writer.println();
    }

    private Set<PackageDoc> exportClassesDoc(final PrintWriter writer) {
        final Set<PackageDoc> packages = new HashSet<>();
        writer.println("Classes");
        for (final ClassDoc doc : rootDoc.classes()) {
            packages.add(doc.containingPackage());
            exportClassDoc(doc, writer);
        }
        writer.println();

        return packages;
    }

    /**
     * Exports a class documentation.
     *
     * @param doc    the class documentation object
     * @param writer the {@link PrintWriter} used to output the class documentation.
     */
    private void exportClassDoc(final ClassDoc doc, final PrintWriter writer) {
        ClassDocStats stats = new ClassDocStats(doc);
        writer.printf("\t%s: %s Package: %s Documented: %s\n", stats.getType(), stats.getName(), stats.getPackageName(), stats.hasDocumentation());

        exportMembersDocSummary(writer, stats.getFieldsStats());
        exportMethodsDocSummary(writer, stats.getConstructorsStats());
        exportMethodsDocSummary(writer, stats.getMethodsStats());
        exportMembersDocSummary(writer, stats.getEnumsStats());
    }

    private void exportMethodsDocSummary(final PrintWriter writer, final List<MethodDocStats> methodStatsList) {
        for (final MethodDocStats methodStats : methodStatsList) {
            writer.printf("\t\t%s: %s\n\t", methodStats.getType(), methodStats.getMethodName());
            exportMembersDocSummary(writer, methodStats.getParamsStats());
            writer.print("\t");
            exportMembersDocSummary(writer, methodStats.getThrownExceptions());
        }
    }

    private void exportMembersDocSummary(final PrintWriter writer, final MembersDocStats membersDocStats) {
        if (membersDocStats.getMembersNumber() == 0 && !membersDocStats.isPrintIfNoMembers()) {
            writer.println();
            return;
        }

        writer.printf("\t\t%-15s %6d Documented: %6d Non-Documented: %6d\n",
                membersDocStats.getMembersType(), membersDocStats.getMembersNumber(), membersDocStats.getDocumentedMembers(),
                membersDocStats.getUndocumentedMembers());
    }

    /**
     * Exports a package documentation.
     *
     * @param doc    the package documentation object
     * @param writer the {@link PrintWriter} used to output the package documentation.
     */
    private void exportPackageDoc(final PackageDoc doc, final PrintWriter writer) {
        writer.printf("\tPackage %s. Documented: %s\n", doc.name(), !Utils.isStringEmpty(doc.commentText()));
    }

    /**
     * Gets a {@link PrintWriter} to export the documentation of a class or package.
     *
     * @param packageDoc the package documentation object that will be the package that the documentation
     *                   is being exported or the package of the class that its documentation
     *                   is being exported
     * @param fileName   the name of the file to export the documentation to
     */
    private PrintWriter getWriter(final PackageDoc packageDoc, final String fileName) throws FileNotFoundException {
        final File packageDirectory = new File(getOutputDir() + packageDoc.name().replace('.', File.separatorChar));
        if (!packageDirectory.exists() && !packageDirectory.mkdirs()) {
            throw new RuntimeException("The directory was not created due to unknown reason.");
        }

        final File file = new File(packageDirectory, fileName + ".txt");
        return new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
    }

    /**
     * Gets the output directory passed as a command line argument to javadoc tool.
     *
     * @return the output directory to export the javadocs
     */
    private String getOutputDir() {
        for (final String[] option : rootDoc.options()) {
            if (option.length == 2 && option[0].equals("-d")) {
                return includeTrailingDirSeparator(option[1]);
            }
        }

        return "";
    }

    /**
     * Adds a trailing slash at the end of a path if it doesn't have one yet.
     * The trailing slash type is system-dependent and will be accordingly selected.
     *
     * @param path the path to include a trailing slash
     * @return the path with a trailing slash if there wasn't one and the path is not empty,
     * the original path otherwise
     */
    private String includeTrailingDirSeparator(final String path) {
        if (path.trim().isEmpty()) {
            return path;
        }

        if (path.charAt(path.length() - 1) != File.separatorChar) {
            return path + File.separator;
        }

        return path;
    }
}
