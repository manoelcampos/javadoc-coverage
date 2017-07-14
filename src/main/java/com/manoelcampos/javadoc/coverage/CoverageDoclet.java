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

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;

/**
 * A {@link Doclet} that computes coverage of JavaDoc documentation.
 *
 * @author Manoel Campos da Silva Filho
 * @see ReportGenerator
 */
public class CoverageDoclet extends Doclet {
    private final ReportGenerator generator;

    /**
     * Creates a ExportDoclet to export javadoc.
     *
     * @param rootDoc the root of the program structure information.
     *                From this root all other program structure information can be extracted.
     */
    public CoverageDoclet(RootDoc rootDoc) {
        this.generator = new ReportGenerator(rootDoc);
    }

    /**
     * Starts the doclet.
     *
     * @param rootDoc the root of the program structure information.
     *                From this root all other program structure information can be extracted.
     * @return true if the doclet was started successfully, false otherwise
     * @see Doclet#start(RootDoc)
     */
    public static boolean start(RootDoc rootDoc) {
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
    public static boolean validOptions(String[][] options, DocErrorReporter errorReporter) {
        return Standard.validOptions(options, errorReporter);
    }

    /**
     * Gets the number of arguments that a given command line option must contain.
     *
     * @param option the command line option
     * @return the number of arguments required for the given option
     * @see Doclet#optionLength(String)
     */
    public static int optionLength(String option) {
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
        return generator.start();
    }
}
