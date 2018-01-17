package com.manoelcampos.sample2;

import java.io.IOException;

/**
 * The Interface2.
 */
public interface Interface2 {
    /**
     * The constant1
     */
    int CONSTANT1 = 1;

    int CONSTANT2 = 2;

    /**
     * The method1.
     *
     * @param input1 the first input
     * @throws IllegalArgumentException when the input1 is negative
     * @throws IndexOutOfBoundsException when there are not historic values
     */
    int method1(int input1, int input2) throws IOException, IndexOutOfBoundsException;

    /**
     * The method2.
     *
     * @throws IllegalArgumentException when the input is negative
     * @throws IndexOutOfBoundsException when there are not historic values
     */
    double method2(double input) throws IOException, IndexOutOfBoundsException, java.lang.IllegalArgumentException;

    /**
     * The method3.
     *
     * @throws IllegalArgumentException when the input is negative
     * @throws IndexOutOfBoundsException when there are not historic values
     */
    long method3(long input) throws IOException, IndexOutOfBoundsException, IllegalArgumentException;
}