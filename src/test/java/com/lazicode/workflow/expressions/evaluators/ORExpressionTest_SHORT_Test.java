package com.lazicode.workflow.expressions.evaluators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ORExpressionTest_SHORT_Test {

    /**
     * Evaluates A OR B, where A and B can be TRUE, FALSE, or NULL.
     *
     * @param a Boolean value or null for A
     * @param b Boolean value or null for B
     * @return the result of A OR B, handling nulls appropriately
     */
    private Boolean evaluateOr(Boolean a, Boolean b) {
        if (Boolean.TRUE.equals(a) || Boolean.TRUE.equals(b)) {
            return true;
        }
        if (Boolean.FALSE.equals(a) && Boolean.FALSE.equals(b)) {
            return false;
        }
        return null;
    }

    @Test
    void testOrExpression_trueTrue() {
        assertEquals(true, evaluateOr(true, true));
    }

    @Test
    void testOrExpression_trueFalse() {
        assertEquals(true, evaluateOr(true, false));
    }

    @Test
    void testOrExpression_trueNull() {
        assertEquals(true, evaluateOr(true, null));
    }

    @Test
    void testOrExpression_falseTrue() {
        assertEquals(true, evaluateOr(false, true));
    }

    @Test
    void testOrExpression_falseFalse() {
        assertEquals(false, evaluateOr(false, false));
    }

    @Test
    void testOrExpression_falseNull() {
        assertEquals(null, evaluateOr(false, null));
    }

    @Test
    void testOrExpression_nullTrue() {
        assertEquals(true, evaluateOr(null, true));
    }

    @Test
    void testOrExpression_nullFalse() {
        assertEquals(null, evaluateOr(null, false));
    }

    @Test
    void testOrExpression_nullNull() {
        assertEquals(null, evaluateOr(null, null));
    }
}
