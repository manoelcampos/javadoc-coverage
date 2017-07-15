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

import com.manoelcampos.javadoc.coverage.stats.MethodDocStats;

import java.io.File;
import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * An utility class.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public final class Utils {
    /**
     * A private constructor to avoid class instantiation.
     */
    private Utils() {}

    public static boolean isNotStringEmpty(final String str) {
        return !isStringEmpty(str);
    }

    public static boolean isStringEmpty(final String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Adds a trailing slash at the end of a path if it doesn't have one yet.
     * The trailing slash type is system-dependent and will be accordingly selected.
     *
     * @param path the path to include a trailing slash
     * @return the path with a trailing slash if there wasn't one and the path is not empty,
     * the original path otherwise
     */
    public static String includeTrailingDirSeparator(final String path) {
        if (path.trim().isEmpty()) {
            return path;
        }

        if (path.charAt(path.length() - 1) != File.separatorChar) {
            return path + File.separator;
        }

        return path;
    }

    /**
     * Computes the percentage that a partial value corresponds to a given total.
     * @param partialValue the partial value to compute the percentage related to the total value
     * @param totalValue the total the partial value corresponds to
     * @return the percentage (in scale from 0 to 100) the partial value represents from the total
     */
    public static double computePercentage(final double partialValue, final double totalValue){
        if(totalValue == 0){
            return 0;
        }

        return (partialValue/totalValue) * 100.0;

    }

    public static double mean(final double... values){
        if(values == null){
            return 0;
        }

        return average(Arrays.stream(values));
    }

    public static double average(final DoubleStream stream) {
        return stream.average().orElse(0);
    }

    public static int boolToInt(final boolean bool){ return bool ? 1 : 0; }
}
