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
import com.manoelcampos.javadoc.coverage.stats.JavaDocsStats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public abstract class AbstractDataExporter implements DataExporter {
    private final JavaDocsStats stats;
    private final PrintWriter writer;
    private File file;

    protected AbstractDataExporter(final CoverageDoclet doclet, final String extension) {
        if(Utils.isNotStringEmpty(extension)) {
            this.file = doclet.getOutputFile(generateFileName(extension));
            try {
                this.writer = doclet.getWriter(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else writer = new PrintWriter(System.out);

        this.stats = new JavaDocsStats(doclet.getRootDoc());
    }

    private String generateFileName(final String extension) {
        return extension.startsWith(".") ? COVERAGE_REPORT_FILE + extension : COVERAGE_REPORT_FILE + "." + extension;
    }

    protected AbstractDataExporter(final CoverageDoclet doclet) throws FileNotFoundException {
        this(doclet, "");
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
}
