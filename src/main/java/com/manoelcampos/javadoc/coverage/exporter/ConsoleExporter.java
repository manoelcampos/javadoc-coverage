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
package com.manoelcampos.javadoc.coverage.exporter;

import com.manoelcampos.javadoc.coverage.CoverageDoclet;
import com.manoelcampos.javadoc.coverage.Utils;
import com.manoelcampos.javadoc.coverage.stats.*;
import com.sun.javadoc.PackageDoc;

import java.io.PrintWriter;
import java.util.List;

/**
 * Prints the JavaDoc coverage report to the console (standard output).
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class ConsoleExporter extends AbstractDataExporter {

    public ConsoleExporter(final CoverageDoclet doclet) {
        super(doclet);
    }

    @Override
    protected void header() {/**/}

    @Override
    protected void footer() {/**/}

    @Override
    public void afterBuild() {/**/}

    @Override
    protected void exportProjectDocumentationCoverageSummary() {
        getWriter().printf("Project Documentation Coverage: %.2f%%\n\n", getStats().getDocumentedMembersPercent());
    }

    @Override
    protected void exportPackagesDocStats() {
        final PackagesDocStats packagesDocStats = getStats().getPackagesDocStats();
        exportPkgsOrClassesDocStats(packagesDocStats);
        packagesDocStats.getPackagesDoc().forEach(this::exportPackageDocStats);
        getWriter().println();
    }

    private void exportPkgsOrClassesDocStats(MembersDocStats packagesDocStats) {
        getWriter().printf("%-26s: \t%11d Undocumented: %6d Documented: %6d (%.2f%%)\n",
                packagesDocStats.getType(), packagesDocStats.getMembersNumber(), packagesDocStats.getUndocumentedMembers(),
                packagesDocStats.getDocumentedMembers(), packagesDocStats.getDocumentedMembersPercent());
    }

    /**
     * Exports the statistics about JavaDoc coverage of a given package.
     *  @param doc the object containing the JavaDoc coverage data
     *
     */
    private void exportPackageDocStats(final PackageDoc doc) {
        getWriter().printf("\tPackage %s. Documented: %s\n", doc.name(), Utils.isNotStringEmpty(doc.commentText()));
    }

    @Override
    protected void exportClassesDocStats() {
        final ClassesDocStats classesDocStats = getStats().getClassesDocStats();
        exportPkgsOrClassesDocStats(classesDocStats);

        for (final ClassDocStats classStats : getStats().getClassesDocStats().getClassesList()) {
            exportClassDocStats(classStats);
        }
        getWriter().println();
    }

    /**
     * Exports the statistics about JavaDoc coverage of a given class.
     *  @param classStats the object containing the JavaDoc coverage data
     *
     */
    private void exportClassDocStats(final ClassDocStats classStats) {
        getWriter().printf("\t%s: %s Package: %s Documented: %s (%.2f%%)\n",
                classStats.getType(), classStats.getName(), classStats.getPackageName(),
                classStats.hasDocumentation(), classStats.getDocumentedMembersPercent());

        exportMembersDocStats(getWriter(), classStats.getFieldsStats());
        exportMethodsDocStats(getWriter(), classStats.getConstructorsStats());
        exportMethodsDocStats(getWriter(), classStats.getMethodsStats());
        exportMembersDocStats(getWriter(), classStats.getEnumsStats());
        getWriter().flush();
    }

    private void exportMethodsDocStats(final PrintWriter writer, final List<MethodDocStats> methodStatsList) {
        final String memberTypeFormat = "\t\t\t%-12s";
        for (final MethodDocStats methodStats : methodStatsList) {
            writer.printf("\t\t%s: %s Documented: %s (%.2f%%)\n",
                    methodStats.getType(), methodStats.getMethodName(),
                    methodStats.hasDocumentation(), methodStats.getDocumentedMembersPercent());
            exportMembersDocStats(writer, methodStats.getParamsStats(), memberTypeFormat);

            if(methodStats.getThrownExceptions().getMembersNumber() > 0) {
                exportMembersDocStats(writer, methodStats.getThrownExceptions(), memberTypeFormat);
            }
        }
    }

    private void exportMembersDocStats(final PrintWriter writer, final MembersDocStats membersDocStats) {
        exportMembersDocStats(writer, membersDocStats, "");
    }

    private void exportMembersDocStats(final PrintWriter writer, final MembersDocStats membersDocStats, final String memberTypeFormat) {
        if (membersDocStats.getMembersNumber() == 0 && !membersDocStats.isPrintIfNoMembers()) {
            return;
        }

        final String format = (Utils.isStringEmpty(memberTypeFormat) ? "\t\t%-20s" : memberTypeFormat) +
                              " %6d Undocumented: %6d Documented: %6d (%.2f%%) \n";
        writer.printf(format,
                membersDocStats.getType()+":", membersDocStats.getMembersNumber(),
                membersDocStats.getUndocumentedMembers(),
                membersDocStats.getDocumentedMembers(),  membersDocStats.getDocumentedMembersPercent());
    }

}
