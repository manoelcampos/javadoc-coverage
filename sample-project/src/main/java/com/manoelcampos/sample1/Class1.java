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