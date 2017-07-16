package com.manoelcampos.javadoc.coverage.exporter;

import com.manoelcampos.javadoc.coverage.stats.*;

import java.io.PrintWriter;

/**
 * Exports data to HTML format.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public class HtmlDataExporter {
    public static final String COLUMNS = "<td>%s</td><td>%s</td><td>%s</td><td>%d</td><td>%d</td><td>%d</td><td>%.2f%%</td>\n";
    private final JavaDocsStats stats;
    private final PrintWriter writer;

    public HtmlDataExporter(final JavaDocsStats stats, final PrintWriter writer){
        this.writer = writer;
        this.stats = stats;
    }

    public void build(){
        try{
            writer.println("<!DOCTYPE html>\n<html lang=en>");
            writer.println("<head>");
            writer.println("    <title>JavaDoc Coverage Report</title>");
            writer.println("    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>\n");
            writer.println("    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css' integrity='sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp' crossorigin='anonymous'>");
            writer.println("    <script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js' integrity='sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa' crossorigin='anonymous'></script>");
            writer.println("    <meta content='width=device-width,initial-scale=1' name=viewport>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<table>");
            writer.println("<tr>");
            writer.println("<th>Element Type</th><th>Name</th><th>Package</th><th>Count</th><th>Undocumented</th><th>Documented</th><th>Documented Percent</th>");
            writer.println("</tr>");

            exportMembersDocStatsSummary(stats.getClassesDocStats());
            for (final ClassDocStats classDocStats : stats.getClassesDocStats().getClassesList()) {
                exportMembersDocStatsSummary(classDocStats)
            }

            writer.println("</table>");
            writer.println("</body>");
            writer.flush();
        } finally {
            writer.close();
        }
    }

    private <T extends DocStats & DocumentableMembers> void exportMembersDocStatsSummary(final T membersDocStats) {
        exportMembersDocStatsSummary(membersDocStats, "","");
    }

    private <T extends DocStats & DocumentableMembers> void exportMembersDocStatsSummary(final T membersDocStats, final String name, final String pkg) {
        writer.println("<tr>");
        writer.printf(COLUMNS,
                membersDocStats.getType(),
                name, pkg,
                membersDocStats.getMembersNumber(),
                membersDocStats.getUndocumentedMembers(),
                membersDocStats.getDocumentedMembers(),
                membersDocStats.getDocumentedMembersPercent());
        writer.println("</tr>");
    }
}
