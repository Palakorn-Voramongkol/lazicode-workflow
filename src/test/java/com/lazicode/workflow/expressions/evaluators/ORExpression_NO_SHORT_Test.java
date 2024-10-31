package com.lazicode.workflow.expressions.evaluators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ORExpression_NO_SHORT_Test {

    /**
     * Evaluates A OR B without short-circuiting.
     * Returns true if any input is true,
     * false only if both inputs are false,
     * null if any input is null.
     *
     * @param a Boolean value or null for A
     * @param b Boolean value or null for B
     * @return the result of A OR B with null handling
     */
    private Boolean evaluateOrNoShortCircuit(Boolean a, Boolean b) {
        if (a == null || b == null) {
            return null; // Indeterminate result if any input is null
        }
        return a || b; // Only true if any input is true; otherwise false
    }

    @Test
    void testOrExpression_trueTrue() {
        assertEquals(true, evaluateOrNoShortCircuit(true, true));
    }

    @Test
    void testOrExpression_trueFalse() {
        assertEquals(true, evaluateOrNoShortCircuit(true, false)); // Both inputs are defined
    }

    @Test
    void testOrExpression_trueNull() {
        assertEquals(null, evaluateOrNoShortCircuit(true, null)); // Any null yields null
    }

    @Test
    void testOrExpression_falseTrue() {
        assertEquals(true, evaluateOrNoShortCircuit(false, true)); // Both inputs are defined
    }

    @Test
    void testOrExpression_falseFalse() {
        assertEquals(false, evaluateOrNoShortCircuit(false, false)); // Both false without nulls
    }

    @Test
    void testOrExpression_falseNull() {
        assertEquals(null, evaluateOrNoShortCircuit(false, null)); // Null input yields null
    }

    @Test
    void testOrExpression_nullTrue() {
        assertEquals(null, evaluateOrNoShortCircuit(null, true)); // Any null yields null
    }

    @Test
    void testOrExpression_nullFalse() {
        assertEquals(null, evaluateOrNoShortCircuit(null, false)); // Any null yields null
    }

    @Test
    void testOrExpression_nullNull() {
        assertEquals(null, evaluateOrNoShortCircuit(null, null)); // Both null inputs yield null
    }
}
