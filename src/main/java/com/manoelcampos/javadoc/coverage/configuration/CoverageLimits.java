package com.manoelcampos.javadoc.coverage.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoverageLimits {

    private static final int PACKAGE_INDEX = 1;
    private static final int INTERFACE_INDEX = 2;
    private static final int CLASS_INDEX = 3;
    private static final int METHOD_INDEX = 4;

    private final double minPackageCoverage;
    private final double minInterfaceCoverage;
    private final double minClassCoverage;
    private final double minMethodCoverage;

    public static CoverageLimits noLimits() {
        return new CoverageLimits(0,0,0,0);
    }

    public static CoverageLimits fromOptionsArray(String[] arr) {
        double packageCov = safeParse(arr, PACKAGE_INDEX, "package");
        double interfaceCov = safeParse(arr, INTERFACE_INDEX, "interface");
        double classCov = safeParse(arr, CLASS_INDEX, "class");
        double methodCov = safeParse(arr, METHOD_INDEX, "method");

        return new CoverageLimits(packageCov, interfaceCov, classCov, methodCov);
    }

    private static double safeParse(String[] arr, int index, String exceptionMessagePrefix) {
        try {
            return Double.parseDouble(arr[index]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(exceptionMessagePrefix + " min coverage number was not a double: " + arr[index]);
        }
    }
}
