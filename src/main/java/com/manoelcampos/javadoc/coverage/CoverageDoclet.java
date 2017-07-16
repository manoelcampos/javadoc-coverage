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

import com.manoelcampos.javadoc.coverage.exporter.ConsoleExporter;
import com.manoelcampos.javadoc.coverage.exporter.DataExporter;
import com.manoelcampos.javadoc.coverage.exporter.HtmlExporter;
import com.sun.javadoc.*;
import com.sun.tools.doclets.standard.Standard;

import java.io.*;

/**
 * A {@link Doclet} that computes coverage of JavaDoc documentation.
 * It is the entry point for the JavaDoc Tool, which can be executed
 * either directly or from maven.
 *
 * @author Manoel Campos da Silva Filho
 * @see ConsoleExporter
 * @since 1.0.0
 */
public class CoverageDoclet extends Doclet {
    private final DataExporter exporter;
    private final RootDoc rootDoc;

    /**
     * Creates a ExportDoclet to export javadoc.
     *
     * @param rootDoc the root of the program structure information.
     *                From this root all other program structure information can be extracted.
     */
    public CoverageDoclet(final RootDoc rootDoc) {
        this.rootDoc = rootDoc;
        try {
            this.exporter = new HtmlExporter(this);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts the doclet.
     *
     * @param rootDoc the root of the program structure information.
     *                From this root all other program structure information can be extracted.
     * @return true if the doclet was started successfully, false otherwise
     * @see Doclet#start(RootDoc)
     */
    public static boolean start(final RootDoc rootDoc) {
        return new CoverageDoclet(rootDoc).render();
    }

    /**
     * Validates command line options.
     *
     * @param options       the array of given options
     * @param errorReporter an object that allows printing error messages for invalid options
     * @return true if the options are valid, false otherwise
     * @see Doclet#validOptions(String[][], DocErrorReporter)
     */
    public static boolean validOptions(final String[][] options, final DocErrorReporter errorReporter) {
        return Standard.validOptions(options, errorReporter);
    }

    /**
     * Gets the number of arguments that a given command line option must contain.
     *
     * @param option the command line option
     * @return the number of arguments required for the given option
     * @see Doclet#optionLength(String)
     */
    public static int optionLength(final String option) {
        return Standard.optionLength(option);
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
     * Renders the javadoc documentation for all elements inside the {@link RootDoc} object
     * received by this doclet.
     *
     * @return true if the {@link RootDoc} was rendered successfully, false otherwise
     */
    private boolean render() {
        return exporter.build();
    }

    public RootDoc getRootDoc() {
        return rootDoc;
    }

    /**
     * Gets the output directory passed as a command line argument to javadoc tool.
     *
     * @return the output directory to export the JavaDocs
     */
    private String getOutputDir() {
        for (final String[] option : rootDoc.options()) {
            if (option.length == 2 && option[0].equals("-d")) {
                return Utils.includeTrailingDirSeparator(option[1]);
            }
        }

        return "";
    }

    /**
     * Gets a {@link PrintWriter} to export the documentation of a class or package.
     *
     * @param file the file to export the documentation to
     */
    public PrintWriter getWriter(final File file) throws FileNotFoundException {
        return new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
    }

    public File getOutputFile(final String fileName) {
        final File dir = new File(getOutputDir());
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("The directory '" + getOutputDir() + "' was not created due to unknown reason.");
        }

        return new File(dir, fileName);
    }
}
