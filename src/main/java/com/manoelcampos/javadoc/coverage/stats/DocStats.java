package com.manoelcampos.javadoc.coverage.stats;

/**
 * An interface to be implemented by classes
 * computing JavaDoc statistics.
 *
 * @author Manoel Campos da Silva Filho
 * @since 1.0.0
 */
public interface DocStats {
    /**
     * The type of the element to which JavaDoc statistics is being computed,
     * if a class, interface, field, method, method parameter, etc.
     * @return
     */
    String getType();
}
