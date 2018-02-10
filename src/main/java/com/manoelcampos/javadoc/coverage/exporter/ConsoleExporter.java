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

import java.io.PrintWriter;
import java.util.List;

import com.manoelcampos.javadoc.coverage.Utils;
import com.manoelcampos.javadoc.coverage.configuration.Configuration;
import com.manoelcampos.javadoc.coverage.stats.*;

import lombok.NonNull;

/**
 * Prints the JavaDoc coverage report to the console (standard output).
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class ConsoleExporter extends AbstractDataExporter {

    public ConsoleExporter(@NonNull Configuration config, @NonNull JavaDocsStats stats) {
        super(config, stats);
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
        for (PackageDocStats packageDoc : getStats().getPackagesDocStats()) {
            getWriter().printf("%-26s: \t%11d Undocumented: %6d Documented: %6d (%.2f%%)\n",
                    packageDoc.getType() + " " + packageDoc.getName(),
                    packageDoc.getNumberOfDocumentableMembers(), packageDoc.getUndocumentedMembersOfElement(),
                    packageDoc.getNumberOfDocumentedMembers(), packageDoc.getDocumentedMembersPercent());
            exportClassesDocStats(packageDoc);
        }
        getWriter().println();
    }

    private void exportClassesDocStats(PackageDocStats packageWithClasses) {
        for (final ClassDocStats classStats : packageWithClasses.getClassDocs()) {
            getWriter().printf("\t%s: %s Package: %s Documented: %s (%.2f%%)\n", classStats.getType(), classStats.getName(),
                    packageWithClasses.getName(), classStats.isDocumented(), classStats.getDocumentedMembersPercent());
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
        exportMembersDocStats(getWriter(), classStats.getFieldsStats());
        exportMembersDocStats(getWriter(), classStats.getEnumsStats());
        exportMembersDocStats(getWriter(), classStats.getAnnotationsStats());
        exportMethodsDocStats(getWriter(), classStats.getConstructorsStats());
        exportMethodsDocStats(getWriter(), classStats.getMethodsStats());
        getWriter().flush();
    }

    private void exportMethodsDocStats(final PrintWriter writer, final List<MethodDocStats> methodStatsList) {
        final String memberTypeFormat = "\t\t\t%-12s";
        for (final MethodDocStats methodStats : methodStatsList) {
            writer.printf("\t\t%s: %s Documented: %s (%.2f%%)\n",
                    methodStats.getType(), methodStats.getMethodName(),
                    methodStats.isDocumented(), methodStats.getDocumentedMembersPercent());
            exportMembersDocStats(writer, methodStats.getParamsStats(), memberTypeFormat);

            if (methodStats.getThrownExceptionsStats().getNumberOfDocumentableMembers() > 0) {
                exportMembersDocStats(writer, methodStats.getThrownExceptionsStats(), memberTypeFormat);
            }
        }
    }

    private void exportMembersDocStats(final PrintWriter writer, final MembersDocStats membersDocStats) {
        exportMembersDocStats(writer, membersDocStats, "");
    }

    private void exportMembersDocStats(final PrintWriter writer, final MembersDocStats membersDocStats, final String memberTypeFormat) {
        if (membersDocStats.getNumberOfDocumentableMembers() == 0 && !membersDocStats.isPrintIfNoMembers()) {
            return;
        }

        final String format = (Utils.isStringEmpty(memberTypeFormat) ? "\t\t%-20s" : memberTypeFormat) +
                " %6d Undocumented: %6d Documented: %6d (%.2f%%) \n";
        writer.printf(format,
                membersDocStats.getType() + ":", membersDocStats.getNumberOfDocumentableMembers(),
                membersDocStats.getUndocumentedMembersOfElement(),
                membersDocStats.getNumberOfDocumentedMembers(), membersDocStats.getDocumentedMembersPercent());
    }

}
