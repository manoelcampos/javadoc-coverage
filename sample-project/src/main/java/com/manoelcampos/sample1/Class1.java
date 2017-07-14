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
package com.manoelcampos.sample1;

/**
 * The Class1.
 */
public class Class1 {
    /**
     * An enum of continents.
     */
    public enum Continent {
        /**
         * African continent.
         */
        AFRICA, 
        ANTARCTICA, 
        ASIA, 
        /**
         * European continent.
         */
        EUROPE, 
        OCEANIA
    }

    private int field1;
    private String field2;

    /**
     * The field 3.
     */
    private String field3;

    public Class1(){
    }

    /**
     * A single parameter constructor.
     * @throws RuntimeException when nothing works anymore.
     */
    public Class1(int param1) throws RuntimeException {

    }

    /**
     * A single parameter constructor.
     * @param param1 the param1
     * @param param2 the param2
     */
    public Class1(int param1, double param2, long param3){

    }

    /**
     * Gets value of field 1.
     */
    public int getField1(){
        return 0;
    }

    public void setField1(int field1) throws IllegalArgumentException{
        this.field1 = field1;
    }

    public int getField2(){
        return 0;
    }

    /**
     * An inner class.
     */
    public class InnerClass1{

        /**
         * Gets the inner class value.
         */
        public double getValue(){
            return 0.0;
        }
    }
    
}