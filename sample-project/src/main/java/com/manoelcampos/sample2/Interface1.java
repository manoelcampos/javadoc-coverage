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