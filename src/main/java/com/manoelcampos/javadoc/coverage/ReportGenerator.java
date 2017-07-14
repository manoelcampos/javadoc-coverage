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
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

import java.io.*;
import java.util.List;

/**
 * A renderer class that actually exports JavaDoc comments.
 * It is used when the {@link CoverageDoclet} is started.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class ReportGenerator {
    private final JavaDocsStats stats;
    private final CoverageDoclet doclet;

    public ReportGenerator(final CoverageDoclet doclet) {
        this.doclet = doclet;
        this.stats = new JavaDocsStats(doclet.getRootDoc());
    }

    /**
     * Exports classes and packages JavaDocs, inside a {@link RootDoc} object.
     *
     * @return true if successful, false otherwise
     */
    public boolean start() {
        try (final PrintWriter writer = new PrintWriter(System.out)) {
            exportClassesDocStats(writer);
            exportPackagesDocStats(writer);
            writer.flush();
        }

        return true;
    }

    private void exportPackagesDocStats(final PrintWriter writer) {
        writer.println("Packages");
        stats.getPackagesDocStats().getPackagesDoc().forEach(pkg -> exportPackageDocStats(pkg, writer));
        writer.println();
    }

    /**
     * Exports the statistics about JavaDoc coverage of a given package.
     *
     * @param doc the object containing the JavaDoc coverage data
     * @param writer the {@link PrintWriter} used to output the statistics.
     */
    private void exportPackageDocStats(final PackageDoc doc, final PrintWriter writer) {
        writer.printf("\tPackage %s. Documented: %s\n", doc.name(), Utils.isNotStringEmpty(doc.commentText()));
    }

    private void exportClassesDocStats(final PrintWriter writer) {
        writer.println("Classes");
        for (final ClassDocStats classStats : stats.getClassesDocStats()) {
            exportClassDocStats(classStats, writer);
        }
        writer.println();
    }

    /**
     * Exports the statistics about JavaDoc coverage of a given class.
     *
     * @param classStats the object containing the JavaDoc coverage data
     * @param writer the {@link PrintWriter} used to output the statistics.
     */
    private void exportClassDocStats(final ClassDocStats classStats, final PrintWriter writer) {
        writer.printf("\t%s: %s Package: %s Documented: %s\n", classStats.getType(), classStats.getName(), classStats.getPackageName(), classStats.hasDocumentation());

        exportMembersDocStats(writer, classStats.getFieldsStats());
        exportMethodsDocStats(writer, classStats.getConstructorsStats());
        exportMethodsDocStats(writer, classStats.getMethodsStats());
        exportMembersDocStats(writer, classStats.getEnumsStats());
        writer.flush();
    }

    private void exportMethodsDocStats(final PrintWriter writer, final List<MethodDocStats> methodStatsList) {
        for (final MethodDocStats methodStats : methodStatsList) {
            writer.printf("\t\t%s: %s\n\t", methodStats.getType(), methodStats.getMethodName());
            exportMembersDocStats(writer, methodStats.getParamsStats());
            writer.print("\t");
            exportMembersDocStats(writer, methodStats.getThrownExceptions());
        }
    }

    private void exportMembersDocStats(final PrintWriter writer, final MembersDocStats membersDocStats) {
        if (membersDocStats.getMembersNumber() == 0 && !membersDocStats.isPrintIfNoMembers()) {
            writer.println();
            return;
        }

        writer.printf("\t\t%-15s %6d Documented: %6d Non-Documented: %6d\n",
                membersDocStats.getType(), membersDocStats.getMembersNumber(), membersDocStats.getDocumentedMembers(),
                membersDocStats.getUndocumentedMembers());
    }

}
