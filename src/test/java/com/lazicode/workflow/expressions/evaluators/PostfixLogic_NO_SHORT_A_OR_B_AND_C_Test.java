package com.lazicode.workflow.expressions.evaluators;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PostfixLogic_NO_SHORT_A_OR_B_AND_C_Test {

    // Helper function to create the expression (A OR B) AND C in postfix notation as a String
    private String createExpression() {
        return "A B OR C AND";
    }

    // Helper function to evaluate a single test case without short-circuiting
    private void evaluateTestCase(Boolean a, Boolean b, Boolean c, Boolean expected) {
        Map<String, Boolean> valueMap = new HashMap<>();
        valueMap.put("A", a);
        valueMap.put("B", b);
        valueMap.put("C", c);

        String expression = createExpression();
        Boolean result = PostfixLogic.eval(expression, valueMap); // Use non-short-circuit method

        assertEquals(expected, result, String.format("Failed for A=%s, B=%s, C=%s", a, b, c));
    }

    @Test void testATrueBTrueCTrue() { evaluateTestCase(true, true, true, true); }

    @Test void testATrueBTrueCFalse() { evaluateTestCase(true, true, false, false); }

    @Test void testATrueBTrueCNull() { evaluateTestCase(true, true, null, null); }

    @Test void testATrueBFalseCTrue() { evaluateTestCase(true, false, true, true); }

    @Test void testATrueBFalseCFalse() { evaluateTestCase(true, false, false, false); }

    @Test void testATrueBFalseCNull() { evaluateTestCase(true, false, null, null); }

    @Test void testATrueBNullCTrue() { evaluateTestCase(true, null, true, null); }

    @Test void testATrueBNullCFalse() { evaluateTestCase(true, null, false, null); }

    @Test void testATrueBNullCNull() { evaluateTestCase(true, null, null, null); }

    @Test void testAFalseBTrueCTrue() { evaluateTestCase(false, true, true, true); }

    @Test void testAFalseBTrueCFalse() { evaluateTestCase(false, true, false, false); }

    @Test void testAFalseBTrueCNull() { evaluateTestCase(false, true, null, null); }

    @Test void testAFalseBFalseCTrue() { evaluateTestCase(false, false, true, false); }

    @Test void testAFalseBFalseCFalse() { evaluateTestCase(false, false, false, false); }

    @Test void testAFalseBFalseCNull() { evaluateTestCase(false, false, null, null); }

    @Test void testAFalseBNullCTrue() { evaluateTestCase(false, null, true, null); }

    @Test void testAFalseBNullCFalse() { evaluateTestCase(false, null, false, null); }

    @Test void testAFalseBNullCNull() { evaluateTestCase(false, null, null, null); }

    @Test void testANullBTrueCTrue() { evaluateTestCase(null, true, true, null); }

    @Test void testANullBTrueCFalse() { evaluateTestCase(null, true, false, null); }

    @Test void testANullBTrueCNull() { evaluateTestCase(null, true, null, null); }

    @Test void testANullBFalseCTrue() { evaluateTestCase(null, false, true, null); }

    @Test void testANullBFalseCFalse() { evaluateTestCase(null, false, false, null); }

    @Test void testANullBFalseCNull() { evaluateTestCase(null, false, null, null); }

    @Test void testANullBNullCTrue() { evaluateTestCase(null, null, true, null); }

    @Test void testANullBNullCFalse() { evaluateTestCase(null, null, false, null); }

    @Test void testANullBNullCNull() { evaluateTestCase(null, null, null, null); }
}
