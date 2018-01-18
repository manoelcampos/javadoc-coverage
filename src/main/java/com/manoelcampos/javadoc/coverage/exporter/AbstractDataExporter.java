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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.manoelcampos.javadoc.coverage.CoverageDoclet;
import com.manoelcampos.javadoc.coverage.Utils;
import com.manoelcampos.javadoc.coverage.stats.JavaDocsStats;

/**
 * Abstract class to implement JavaDoc Coverage reports in different formats.
 * Each sub-class should implement a specific format such as HTML, CSV, JSON, etc.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public abstract class AbstractDataExporter implements DataExporter {
    private final JavaDocsStats stats;
    private final PrintWriter writer;
    private File file;
    private final CoverageDoclet doclet;
    private final String reportFileName;

    /**
     * Instantiates a DataExporter object to generate JavaDoc coverage report.
     *
     * @param doclet        the {@link CoverageDoclet} which computes teh JavaDoc coverage statistics.
     * @param fileExtension the extension to the report file. If empty, the report will be printed to the standard output.
     */
    protected AbstractDataExporter(final CoverageDoclet doclet, final String fileExtension) {
        this.doclet = doclet;

        if (Utils.isStringEmpty(fileExtension)) {
            writer = new PrintWriter(System.out);
            this.reportFileName = "";
        } else {
            this.reportFileName = generateReportFileName(fileExtension);
            try {
                this.writer = doclet.getWriter(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        this.stats = new JavaDocsStats(doclet.getRootDoc(), doclet.computeOnlyForPublicModifier());
    }

    /**
     * Instantiates a DataExporter object that generates JavaDoc coverage report to the standard output.
     *
     * @param doclet the {@link CoverageDoclet} which computes teh JavaDoc coverage statistics.
     */
    protected AbstractDataExporter(final CoverageDoclet doclet) {
        this(doclet, "");
    }

    private String generateReportFileName(final String fileExtension) {
        String fileName = getFileNameFromCommandLine();
        fileName = fileName + fileExtensionToAdd(fileName, fileExtension);
        this.file = doclet.getOutputFile(fileName);
        return fileName;
    }

    /**
     * Gets the JavaDoc Coverage Report file name from command line options
     * or the default name if no option is given.
     *
     * @return
     * @see CoverageDoclet#OUTPUT_NAME_OPTION
     */
    private String getFileNameFromCommandLine() {
        final String outputNameOption = doclet.getOptionValue(CoverageDoclet.OUTPUT_NAME_OPTION);
        return outputNameOption != null ? outputNameOption : DEFAULT_OUTPUT_NAME;
    }

    /**
     * Gets the extension to add to a given file if it doesn't have one.
     *
     * @param fileName             the file name to try getting and extension to add
     * @param defaultFileExtension the default file extension to return if the file doesn't have one
     * @return the file extension to add to the file it it doesn't have one or an empty string
     * if it already has.
     */
    private String fileExtensionToAdd(final String fileName, final String defaultFileExtension) {
        return Utils.getFileExtension(fileName).isEmpty() ?
                getFileExtensionStartingWithDot(defaultFileExtension) : "";
    }

    /**
     * Adds a dot to the beginning of a file extension if it doesn't have one.
     *
     * @param fileExtension the file extension
     * @return the validated file extension
     */
    private String getFileExtensionStartingWithDot(String fileExtension) {
        return fileExtension.startsWith(".") ? fileExtension : "." + fileExtension;
    }

    @Override
    public String getReportFileName() {
        return this.reportFileName;
    }

    @Override
    public boolean build() {
        try {
            header();
            exportPackagesDocStats();
            exportProjectDocumentationCoverageSummary();
            footer();
            afterBuild();
            getWriter().flush();
            return true;
        } finally {
            getWriter().close();
        }
    }

    protected PrintWriter getWriter() {
        return writer;
    }

    protected JavaDocsStats getStats() {
        return stats;
    }

    public File getFile() {
        return file;
    }

    protected abstract void exportProjectDocumentationCoverageSummary();

    protected abstract void header();

    protected abstract void footer();

    protected abstract void afterBuild();

    protected abstract void exportPackagesDocStats();
}
