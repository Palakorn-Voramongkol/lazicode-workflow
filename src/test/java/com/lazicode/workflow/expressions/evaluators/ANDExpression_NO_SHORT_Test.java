package com.lazicode.workflow.expressions.evaluators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ANDExpression_NO_SHORT_Test {

    /**
     * Evaluates A AND B without short-circuiting.
     * Returns true only if all inputs are true,
     * false only if all inputs are false, 
     * null if any input is null.
     *
     * @param a Boolean value or null for A
     * @param b Boolean value or null for B
     * @return the result of A AND B with null handling
     */
    private Boolean evaluateAndNoShortCircuit(Boolean a, Boolean b) {
        if (a == null || b == null) {
            return null; // Indeterminate result if any input is null
        }
        return a && b; // Only true if both are true; otherwise false
    }

    @Test
    void testAndExpression_trueTrue() {
        assertEquals(true, evaluateAndNoShortCircuit(true, true));
    }

    @Test
    void testAndExpression_trueFalse() {
        assertEquals(false, evaluateAndNoShortCircuit(true, false)); // Both inputs are defined
    }

    @Test
    void testAndExpression_trueNull() {
        assertEquals(null, evaluateAndNoShortCircuit(true, null)); // Any null yields null
    }

    @Test
    void testAndExpression_falseTrue() {
        assertEquals(false, evaluateAndNoShortCircuit(false, true)); // Both inputs are defined
    }

    @Test
    void testAndExpression_falseFalse() {
        assertEquals(false, evaluateAndNoShortCircuit(false, false)); // Both false without nulls
    }

    @Test
    void testAndExpression_falseNull() {
        assertEquals(null, evaluateAndNoShortCircuit(false, null)); // Null input yields null
    }

    @Test
    void testAndExpression_nullTrue() {
        assertEquals(null, evaluateAndNoShortCircuit(null, true)); // Any null yields null
    }

    @Test
    void testAndExpression_nullFalse() {
        assertEquals(null, evaluateAndNoShortCircuit(null, false)); // Any null yields null
    }

    @Test
    void testAndExpression_nullNull() {
        assertEquals(null, evaluateAndNoShortCircuit(null, null)); // Both null inputs yield null
    }
}
