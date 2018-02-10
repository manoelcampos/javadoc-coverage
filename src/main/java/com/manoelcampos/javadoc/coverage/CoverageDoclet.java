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

import com.manoelcampos.javadoc.coverage.configuration.Configuration;
import com.manoelcampos.javadoc.coverage.exporter.*;
import com.manoelcampos.javadoc.coverage.stats.JavaDocsStats;
import com.sun.javadoc.*;

/**
 * A {@link Doclet} that computes coverage of JavaDoc documentation.
 * It is the entry point for the JavaDoc Tool, which can be executed
 * either directly using the JavaDoc command line tool or from maven.
 *
 * <p><b>References:</b>
 * <ul>
 *     <li><a href="http://docs.oracle.com/javase/8/docs/technotes/guides/javadoc/doclet/overview.html">Doclet Overview</a></li>
 * </ul>
 * </p>
 *
 * @author Manoel Campos da Silva Filho
 * @see ConsoleExporter
 * @since 1.0.0
 */
public class CoverageDoclet {
    /**
     * The {@link DataExporter} object to export the coverage report to a file
     * in a specific format.
     */
    private final DataExporter exporter;
    private final RootDoc rootDoc;

    /**
     * Starts the actual parsing or JavaDoc documentation and generation of the coverage report. This is the entry point
     * for the JavaDoc tool to start the Doclet.
     *
     * @param rootDoc root element which enables reading JavaDoc documentation
     * @return true if the Doclet was started successfully, false otherwise
     * @see Doclet#start(RootDoc)
     */
    public static boolean start(final RootDoc rootDoc) {
        return new CoverageDoclet(rootDoc).render();
    }

    /**
     * Instantiate the Doclet which will parse the JavaDocs and generate the coverage report.
     *
     * @param rootDoc root element which enables reading JavaDoc documentation
     */
    public CoverageDoclet(final RootDoc rootDoc) {
        this.rootDoc = rootDoc;
        Configuration config = new Configuration(rootDoc.options());

        JavaDocsStats stats = new JavaDocsStats(rootDoc, config);

        // this needs to be the last part as it already accesses some stuff from the doclet
        this.exporter = new HtmlExporter(config, stats);
    }

    /**
     * Gets the number of arguments that a given command line option must contain.
     *
     * @param option the command line option
     * @return the number of arguments required for the given option
     * @see Doclet#optionLength(String)
     */
    public static int optionLength(final String option) {
        return Configuration.getOptionLength(option);
    }

    /**
     * Checks that all given option are valid
     *
     * @param options the options to be checked on validity
     * @param errorReporter
     * @return true if the options are valid
     * @see Doclet#validOptions(String[][], DocErrorReporter)
     */
    public static boolean validOptions(final String[][] options, final DocErrorReporter errorReporter) {
        return Configuration.areValidOptions(options, errorReporter);
    }

    /**
     * Gets the version of the Java Programming Language supported
     * by this doclet.
     *
     * @return the Java language supported version
     * @see Doclet#languageVersion()
     */
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }

    /**
     * Renders the JavaDoc documentation for all elements inside the {@link RootDoc} object
     * received by this Doclet.
     *
     * @return true if the {@link RootDoc} was rendered successfully, false otherwise
     */
    private boolean render() {
        return exporter.build();
    }

    /**
     * Gets the root element which enables reading JavaDoc documentation
     * from the Java files given to the JavaDoc tool.
     */
    public RootDoc getRootDoc() {
        return rootDoc;
    }
}
