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
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;

import java.io.*;
import java.util.Arrays;

/**
 * A {@link Doclet} that computes coverage of JavaDoc documentation.
 * It is the entry point for the JavaDoc Tool, which can be executed
 * either directly using the JavaDoc command line tool or from maven.
 *
 * @author Manoel Campos da Silva Filho
 * @see ConsoleExporter
 * @since 1.0.0
 * @see <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/javadoc/doclet/overview.html">Doclet Overview</a>
 */
public class CoverageDoclet {
    /**
     * A command line parameter to enable defining the name of the coverage report.
     * The first value is the long version of the parameter name and the second
     * is the short one.
     */
    public static final String OUTPUT_NAME_OPTION[] = {"-outputName", "-o"};

    /**
     * The {@link DataExporter} object to export the coverage report to a file
     * in a specific format.
     */
    private final DataExporter exporter;
    private final RootDoc rootDoc;

    /**
     * Instantiate the Doclet which will parse the JavaDocs and generate the coverage report.
     *
     * @param rootDoc root element which enables reading JavaDoc documentation
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
     * Checks if a given parameter is a valid custom parameter accepted by this doclet.
     * @param paramName the name of the parameter to check
     * @return true if it's a valid custom parameter, false otherwise
     */
    private static boolean isCustomParameter(final String paramName) {
        return isParameter(paramName, OUTPUT_NAME_OPTION);
    }

    /**
     * Checks if the name of a given parameter corresponds to either its long or short form.
     *
     * @param paramName the name of the parameter to check
     * @param validNames the list of accepted names for that parameter
     * @return true if the given name corresponds to one of the valid names, false otherwise
     */
    private static boolean isParameter(final String paramName, final String[] validNames) {
        for (String validName : validNames) {
            if (validName.equalsIgnoreCase(paramName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Starts the actual parsing or JavaDoc documentation and generation of the coverage report.
     * This is the entry point for the JavaDoc tool to start the Doclet.
     *
     * @param rootDoc root element which enables reading JavaDoc documentation
     * @return true if the Doclet was started successfully, false otherwise
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
        for (final String[] opt : options) {
            if (isCustomParameter(opt[0])) {
                return true;
            }
        }

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
        /*The custom outputName parameter accepts one argument.
        * The name of the param counts as the one argument.*/
        if (isCustomParameter(option)) {
            return 2;
        }

        return Standard.optionLength(option);
    }

    /**
     * Gets the values associated to a given command line option.
     *
     * @param optionNames an array containing the valid names for the command line option to get its associated values.
     *                    This array may include the long and short versions of the option name,
     *                    for instance {@code {-outputName, -o}}.
     * @return the values associated to the option, where the 0th element is the option itself;
     * or an empty array if the option is invalid.
     */
    public String[] getOptionValues(final String[] optionNames) {
        for (final String[] optionValues : rootDoc.options()) {
            if (isParameter(optionValues[0], optionNames)) {
                return optionValues;
            }
        }

        return new String[]{};
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

    /**
     * Gets a {@link PrintWriter} used by the {@link #exporter} to write
     * the coverage report to.
     *
     * @param file the file to which the coverage report will be saved to
     */
    public PrintWriter getWriter(final File file) throws FileNotFoundException {
        return new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
    }

    /**
     * Gets a {@link File} object from a given file name.
     *
     * @param fileName the name of the file to get a {@link File} object.
     * @return the {@link File} object
     */
    public File getOutputFile(final String fileName) {
        final File dir = new File(getOutputDir());
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("The directory '" + getOutputDir() + "' was not created due to unknown reason.");
        }

        return new File(dir, fileName);
    }

    /**
     * Gets the output directory passed as a command line argument to javadoc tool.
     *
     * @return the output directory to export the JavaDocs
     */
    private String getOutputDir() {
        for (final String[] option : rootDoc.options()) {
            System.out.println(Arrays.toString(option));
        }

        for (final String[] option : rootDoc.options()) {
            if (option.length == 2 && option[0].equals("-d")) {
                return Utils.includeTrailingDirSeparator(option[1]);
            }
        }

        return "";
    }
}
