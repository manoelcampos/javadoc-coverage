package com.manoelcampos.sample2;

import java.io.IOException;

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
    default int square(int num){  return num*num; }

    /**
     * Gets the sum.
     */
    default int sum(){ return 0; }

    /**
     * Computes some equation.
     * @throws IllegalArgumentException when the input parameter is negative.
     * The exception is not in fact declared in the method signature.
     * Since it's an unchecked exception, that isn't required.
     */
    int equation(int input) throws IOException;

    /**
     * Divide two numbers
     * @throws ArithmeticException when b is zero
     */
    int divide(int a, int b);


    /**
     * Computes some equation.
     * @throws IllegalArgumentException when input parameters are negative.
     * The exception is not in fact declared in the method signature.
     * Since it's an unchecked exception, that isn't required.
     */
    int equation(int input1, int input2) throws IOException, IndexOutOfBoundsException;

    /**
     * Computes some equation.
     * @throws IllegalArgumentException when the input1 is negative
     * @throws IndexOutOfBoundsException when there are not historic values
     */
    int someMethod1(int input1, int input2) throws IOException, IndexOutOfBoundsException;

    /**
     * Computes some equation.
     * @throws IllegalArgumentException when the input is negative
     * @throws IndexOutOfBoundsException when there are not historic values
     */
    double someMethod2(double input) throws IOException, IndexOutOfBoundsException, java.lang.IllegalArgumentException;

    /**
     * Computes some equation.
     * @throws IllegalArgumentException when the input is negative
     * @throws IndexOutOfBoundsException when there are not historic values
     */
    long someMethod3(long input) throws IOException, IndexOutOfBoundsException, IllegalArgumentException;

    /**
     * Computes some equation.
     * @throws IllegalArgumentException when the input is negative
     * @throws IndexOutOfBoundsException when trying to access an invalid position
     */
    long someMethod4(long input) throws IOException, IndexOutOfBoundsException;
}