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

/**
 * An interface to implement JavaDoc Coverage reports in different formats such as HTML, CSV, JSON, etc.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public interface DataExporter {
    /**
     * Gets the actual name to be used for the JavaDoc Coverage Report,
     * whether the default or given one.
     *
     * @return
     * @see #DEFAULT_OUTPUT_NAME
     */
    String getReportFileName();

    /**
     * Builds the JavaDoc Coverage Report to the file
     * specified by {@link #getReportFileName()}.
     *
     * @return
     */
    boolean build();
}
