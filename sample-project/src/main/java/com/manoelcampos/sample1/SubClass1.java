package com.manoelcampos.sample1;

/**
 * The SubClass1.
 */
public class SubClass1 extends Class1 {

    @Override
    public int getField1(){
        return -1;
    }

    /**
     * Gets the value of field 2 as a negative number.
     * @return
     */
    @Override
    public int getField2(){
        return -1;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public int getField3(){
        return -1;
    }

}