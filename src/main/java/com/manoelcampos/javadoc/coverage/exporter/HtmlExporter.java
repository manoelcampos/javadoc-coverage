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

import java.util.List;

import com.manoelcampos.javadoc.coverage.Utils;
import com.manoelcampos.javadoc.coverage.configuration.Configuration;
import com.manoelcampos.javadoc.coverage.stats.*;

import lombok.NonNull;

/**
 * Exports the JavaDoc coverage report to an HTML file.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class HtmlExporter extends AbstractDataExporter {
    public static final String COLUMNS = "<td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%.2f%%</td>\n";

    public HtmlExporter(@NonNull final Configuration config, @NonNull JavaDocsStats stats) {
        super(config, stats, ".html");
    }

    @Override
    protected void exportProjectDocumentationCoverageSummary() {
        JavaDocsStats stats = getStats();
        getWriter().printf("<tr>" + COLUMNS + "</tr>", "<strong>Project Documentation Coverage</strong>", "", "",
                stats.getNumberOfDocumentableMembers(), stats.getUndocumentedMembersOfElement(),
                stats.getNumberOfDocumentedMembers(), stats.getDocumentedMembersPercent());
    }

    @Override
    protected void header() {
        getWriter().println("<!DOCTYPE html>\n<html lang=en>");
        getWriter().println("<head>");
        getWriter().println("    <title>JavaDoc Coverage Report</title>");
        getWriter().println("    <meta charset='utf-8'>");
        getWriter().println("    <meta content='width=device-width,initial-scale=1' name=viewport>");
        getWriter().println("    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'>\n");
        //https://github.com/maslianok/stickyRows
        getWriter().println("    <link rel='stylesheet' href='http://maslianok.github.io/stickyRows/css/stickyRows.css'>\n");
        getWriter().println("</head>");
        getWriter().println("<body>");
        getWriter().println("<div class='table-responsive'> ");
        getWriter().println("<h2>JavaDoc Coverage Report</h2>");
        getWriter().println("<table id='table1' class='table table-bordered table-with-sticky-rows'>");
        getWriter().println("<thead class='thead-inverse'>");
        getWriter().println("<tr>");
        getWriter().println("<th>Element Type</th><th>Name</th><th>Package</th><th>Documentable Members</th><th>Undocumented</th><th>Documented</th><th>Documented Percent</th>");
        getWriter().println("</tr>");
        getWriter().println("</thead>");
        getWriter().println("<tbody>");
    }

    @Override
    protected void footer() {
        getWriter().println("</tbody>");
        getWriter().println("</table>");
        getWriter().println("</div>");
        getWriter().println("</body>");

        getWriter().println("<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js'></script>\n");
        getWriter().println("<script src='https://code.jquery.com/jquery-3.2.1.min.js' integrity='sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=' crossorigin='anonymous'></script>\n");
        getWriter().println("<script src='http://maslianok.github.io/stickyRows/js/stickyRows.js'></script>\n");
        getWriter().println("<script> $(document).ready(function(){ $('#table1').stickyRows(); }); </script>\n");
        getWriter().println("</html>");
    }

    @Override
    protected void exportPackagesDocStats() {
        for (final PackageDocStats doc : getStats().getPackagesDocStats()) {
            getWriter().println("<tr>");
            exportLine(0, "Package", doc.getName(), "", doc.getNumberOfDocumentableMembers(), doc.getUndocumentedMembersOfElement(),
                    doc.getNumberOfDocumentedMembers(), doc.getDocumentedMembersPercent());
            int isPackageDocumented = Utils.boolToInt(doc.isDocumented());
            exportLine(2, "package-info.java", "", "", 1L, 1L - isPackageDocumented, (long) isPackageDocumented,
                    isPackageDocumented * 100.0);
            exportClassesDocStats(doc);
        }
    }


    private void exportClassesDocStats(PackageDocStats pkg) {
        for (final ClassDocStats classDocStats : pkg.getClassDocs()) {
            exportMembersDocStatsSummary(classDocStats, 2, classDocStats.getName(), pkg.getName());
            int isClassDocumented = Utils.boolToInt(classDocStats.isDocumented());
            exportLine(3, classDocStats.getType() + " Doc", "", "", 1L, 1L - isClassDocumented, (long) isClassDocumented,
                    isClassDocumented * 100.0);
            exportMembersDocStatsSummary(classDocStats.getEnumsStats(), 3);
            exportMembersDocStatsSummary(classDocStats.getFieldsStats(), 3);
            exportMembersDocStatsSummary(classDocStats.getAnnotationsStats(), 3);
            exportMethodsDocStats(classDocStats.getConstructorsStats());
            exportMethodsDocStats(classDocStats.getMethodsStats());
        }
    }

    private void exportMethodsDocStats(final List<MethodDocStats> methods) {
        for (MethodDocStats m : methods) {
            exportMembersDocStatsSummary(m, 4, m.getMethodName(), "");
            int isMethodDocumented = Utils.boolToInt(m.isDocumented());
            exportLine(5, m.getType() + " Doc", "", "", 1L, 1L - isMethodDocumented, (long) isMethodDocumented,
                    isMethodDocumented * 100.0);
            if (!m.isVoidMethodOrConstructor()) {
                int isReturnDocumented = Utils.boolToInt(m.isReturnDocumented());
                exportLine(5, "Return Value", "", "", 1L, 1L - isReturnDocumented, (long) isReturnDocumented, isReturnDocumented * 100.0);
            }
            exportMembersDocStatsSummary(m.getParamsStats(), 5);
            exportMembersDocStatsSummary(m.getThrownExceptionsStats(), 5);
        }
    }

    private void exportMembersDocStatsSummary(final MembersDocStats membersDocStats, final int indentLevel) {
        exportMembersDocStatsSummary(membersDocStats, indentLevel, "","");
    }

    private void exportMembersDocStatsSummary(final MembersDocStats membersDocStats, final int indentLevel, final String name, final String pkg) {
        if (!membersDocStats.isPrintIfNoMembers() && membersDocStats.getNumberOfDocumentableMembers() == 0) {
            return;
        }

        exportLine(
                indentLevel, membersDocStats.getType(), name, pkg,
                membersDocStats.getNumberOfDocumentableMembers(),
                membersDocStats.getUndocumentedMembersOfElement(),
                membersDocStats.getNumberOfDocumentedMembers(),
                membersDocStats.getDocumentedMembersPercent());
    }

    private void exportLine(int indentLevel, String memberType, String name, String pkg, Long members, Long undocumented, Long documented, double documentedPercent){
        exportLine(indentLevel, memberType, name, pkg, members.toString(), undocumented.toString(), documented.toString(), documentedPercent);
    }

    private void exportLine(int indentLevel, String memberType, String name, String pkg, String members, String undocumented, String documented, double documentedPercent){
        getWriter().println("<tr>");
        final String type = getIndentation(indentLevel) + memberType;
        getWriter().printf(COLUMNS, type, name, pkg, members, undocumented, documented, documentedPercent);
        getWriter().println("</tr>");
    }

    private String getIndentation(final int indentLevel) {
        final int len = indentLevel*4 - 3;
        return String.format("%"+len+"s", "").replace(" ", "&nbsp;");
    }

    @Override
    public void afterBuild() {
        System.out.printf("\nJavaDoc Coverage report saved to %s\n", getFile().getAbsolutePath());
    }
}
