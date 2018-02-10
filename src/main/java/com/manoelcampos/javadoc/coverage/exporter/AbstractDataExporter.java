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

import java.io.*;

import com.manoelcampos.javadoc.coverage.Utils;
import com.manoelcampos.javadoc.coverage.configuration.Configuration;
import com.manoelcampos.javadoc.coverage.stats.JavaDocsStats;

import lombok.NonNull;

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
    private final Configuration config;
    private final String reportFileName;

    /**
     * Instantiates a DataExporter object to generate JavaDoc coverage report.
     *
     * @param config the configuration of the doclet
     * @param stats the javadoc statistics
     * @param fileExtension the extension to the report file. If empty, the report will be printed to the standard
     *            output.
     */
    protected AbstractDataExporter(@NonNull final Configuration config, @NonNull final JavaDocsStats stats,
            final String fileExtension) {
        this.stats = stats;
        this.config = config;

        if (Utils.isStringEmpty(fileExtension)) {
            writer = new PrintWriter(System.out);
            this.reportFileName = "";
        } else {
            this.reportFileName = generateReportFileName(fileExtension);
            try {
                this.writer = getWriter(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Instantiates a DataExporter object that generates JavaDoc coverage report to the standard output.
     *
     * @param config the configuration of the doclet
     * @param stats the javadoc statistics
     */
    protected AbstractDataExporter(@NonNull final Configuration config, @NonNull final JavaDocsStats stats) {
        this(config, stats, "");
    }

    /**
     * Gets a {@link PrintWriter} used by the {@link #exporter} to write the coverage report to.
     *
     * @param file the file to which the coverage report will be saved to
     */
    private PrintWriter getWriter(final File file) throws FileNotFoundException {
        return new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
    }

    private String generateReportFileName(final String fileExtension) {
        String fileName = config.getCoverageFileName();
        if (Utils.getFileExtension(fileName).isEmpty()) {
            fileName += getFileExtensionStartingWithDot(fileExtension);
        }
        this.file = getOutputFile(fileName);
        return fileName;
    }

    /**
     * Gets a {@link File} object for the output file
     *
     * @return the {@link File} object
     */
    private File getOutputFile(String fileName) {
        String outputDirectory = config.getOutputDirectory();
        final File dir = new File(outputDirectory);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("The directory '" + outputDirectory + "' was not created due to unknown reason.");
        }

        return new File(dir, fileName);
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
