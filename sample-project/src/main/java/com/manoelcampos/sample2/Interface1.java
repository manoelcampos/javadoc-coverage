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
package com.manoelcampos.sample2;

/**
 * The Interface1.
 */
public interface Interface1 {
    /**
     * The code.
     */
    int CODE = 1;

    /**
     * Gets the square of a number.
     * @param num the number to get the square.
     * @return the square of the number.
     */
    default int square(int num){ return num*num; }

    /**
     * Gets the sum.
     */
    default int sum(){ return 0; }

    /**
     * Computes some equation.
     */
    int equation(int input);
}