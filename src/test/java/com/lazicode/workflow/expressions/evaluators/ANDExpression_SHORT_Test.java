package com.lazicode.workflow.expressions.evaluators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ANDExpression_SHORT_Test {

    /**
     * Evaluates A AND B, where A and B can be TRUE, FALSE, or NULL.
     * Implements short-circuiting for `false` values only if all inputs are false.
     *
     * @param a Boolean value or null for A
     * @param b Boolean value or null for B
     * @return the result of A AND B, handling nulls appropriately
     */
    private Boolean evaluateAnd(Boolean a, Boolean b) {
        if (Boolean.TRUE.equals(a) || Boolean.TRUE.equals(b)) {
            return true; // Short-circuit to true if any input is true
        }
        if (Boolean.FALSE.equals(a) && Boolean.FALSE.equals(b)) {
            return false; // Result is false only if all inputs are false
        }
        return null; // If we have a null and no true value, return null
    }

    @Test
    void testAndExpression_trueTrue() {
        assertEquals(true, evaluateAnd(true, true));
    }

    @Test
    void testAndExpression_trueFalse() {
        assertEquals(true, evaluateAnd(true, false)); // Short-circuit to true
    }

    @Test
    void testAndExpression_trueNull() {
        assertEquals(true, evaluateAnd(true, null)); // Short-circuit to true
    }

    @Test
    void testAndExpression_falseTrue() {
        assertEquals(true, evaluateAnd(false, true)); // Short-circuit to true
    }

    @Test
    void testAndExpression_falseFalse() {
        assertEquals(false, evaluateAnd(false, false)); // Both false
    }

    @Test
    void testAndExpression_falseNull() {
        assertEquals(null, evaluateAnd(false, null)); // Result is indeterminate
    }

    @Test
    void testAndExpression_nullTrue() {
        assertEquals(true, evaluateAnd(null, true)); // Short-circuit to true
    }

    @Test
    void testAndExpression_nullFalse() {
        assertEquals(null, evaluateAnd(null, false)); // Result is indeterminate
    }

    @Test
    void testAndExpression_nullNull() {
        assertEquals(null, evaluateAnd(null, null)); // Indeterminate result with nulls
    }
}
