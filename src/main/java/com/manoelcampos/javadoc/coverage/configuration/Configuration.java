package com.manoelcampos.javadoc.coverage.configuration;

import java.util.*;

import com.manoelcampos.javadoc.coverage.Utils;
import com.sun.javadoc.DocErrorReporter;
import com.sun.tools.doclets.standard.Standard;

public class Configuration {

    private static final NoValueOption computePublicCoverageOnly = new NoValueOption("-po", "-publicOnly",
            "Indicates if coverage should be computed only for the public parts of a project.");
    private static final StringOption coverageFileName = new StringOption("-o", "-outputName",
            "Set the filename for the created coverage file. If nothing is given, the default is \"javadoc-coverage\".",
            Optional.of("javadoc-coverage"));

    private static final List<Option<?>> ALL_OPTIONS = new ArrayList<>();

    /**
     * Static constructor for initializing the options list
     */
    static {
        ALL_OPTIONS.add(computePublicCoverageOnly);
        ALL_OPTIONS.add(coverageFileName);
    }

    /**
     * The raw options which need to be parsed to our configuration
     */
    private final String[][] rawOptions;

    public Configuration(String[][] rawOptions) {
        this.rawOptions = rawOptions;
    }

    public boolean computePublicCoverageOnly() {
        return isOptionContained(computePublicCoverageOnly);
    }

    public String getCoverageFileName() {
        if (isOptionContained(coverageFileName)) {
            return getOptionValue(coverageFileName);
        }
        return coverageFileName.getDefaultValue();
    }

    /**
     * Gets the output directory passed as a command line argument to javadoc tool.
     *
     * @return the output directory to export the JavaDocs
     */
    public String getOutputDirectory() {
        // This is no option of the doclet, but instead of the javadoc tool
        // so we don't declare it as an option here directly)

        for (final String[] option : rawOptions) {
            if (option.length == 2 && option[0].equals("-d")) {
                return Utils.includeTrailingDirSeparator(option[1]);
            }
        }

        return "";
    }

    private boolean isOptionContained(Option<?> option) {
        for (final String[] opt : rawOptions) {
            if (option.isOption(opt[0])) {
                return true;
            }
        }
        return false;
    }

    private <T> T getOptionValue(Option<T> option) {
        if (!isOptionContained(option) && !option.hasDefaultValue()) {
            throw new IllegalStateException("Option is not contained and has no default value");
        }

        if (!isOptionContained(option)) {
            return option.getDefaultValue();
        }

        for (final String[] opt: rawOptions) {
            if (option.isOption(opt[0])) {
                return option.parseValue(opt);
            }
        }

        throw new IllegalStateException("no valid option found although it should be there");
    }

    /**
     * This method is necessary for validating the doclet parameters, which happens before instantiating the
     * CoverageDoclet and the configuration container.
     *
     * @param option
     * @return the length of the option
     */
    public static int getOptionLength(final String option) {
        Optional<Option<?>> opt = ALL_OPTIONS.stream().filter(o -> o.isOption(option)).findFirst();
        if (opt.isPresent()) {
            return opt.get().getLength();
        }
        return Standard.optionLength(option);
    }

    /**
     * This method is necessary for validating the doclet parameters, which happens before instantiating the
     * CoverageDoclet and the configuration container.
     *
     * @param options
     * @param errorReporter
     * @return indicates if the given options are valid
     */
    public static boolean areValidOptions(final String[][] options, final DocErrorReporter errorReporter) {
        for (final String[] opt : options) {
            if (ALL_OPTIONS.stream().anyMatch(o -> o.isOption(opt[0]))) {
                return true;
            }
        }

        return Standard.validOptions(options, errorReporter);
    }
}
