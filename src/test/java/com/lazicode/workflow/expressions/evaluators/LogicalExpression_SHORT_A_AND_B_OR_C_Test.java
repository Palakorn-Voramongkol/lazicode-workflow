package com.lazicode.workflow.expressions.evaluators;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LogicalExpression_SHORT_A_AND_B_OR_C_Test {

    // Helper function to create the expression A AND B OR C in postfix notation as a String
    private String createExpression() {
        return "A B AND C OR";
    }

    // Helper function to evaluate a single test case
    private void evaluateTestCase(Boolean a, Boolean b, Boolean c, Boolean expected) {
        Map<String, Boolean> valueMap = new HashMap<>();
        valueMap.put("A", a);
        valueMap.put("B", b);
        valueMap.put("C", c);

        String expression = createExpression();
        Boolean result = PostfixLogicEval.evaluatePostfixShortCircuit(expression, valueMap);

        assertEquals(expected, result, String.format("Failed for A=%s, B=%s, C=%s", a, b, c));
    }

    @Test void testATrueBTrueCTrue() { evaluateTestCase(true, true, true, true); }

    @Test void testATrueBTrueCFalse() { evaluateTestCase(true, true, false, true); }

    @Test void testATrueBTrueCNull() { evaluateTestCase(true, true, null, true); }

    @Test void testATrueBFalseCTrue() { evaluateTestCase(true, false, true, true); }

    @Test void testATrueBFalseCFalse() { evaluateTestCase(true, false, false, false); }

    @Test void testATrueBFalseCNull() { evaluateTestCase(true, false, null, null); }

    @Test void testATrueBNullCTrue() { evaluateTestCase(true, null, true, true); }

    @Test void testATrueBNullCFalse() { evaluateTestCase(true, null, false, null); }

    @Test void testATrueBNullCNull() { evaluateTestCase(true, null, null, null); }

    @Test void testAFalseBTrueCTrue() { evaluateTestCase(false, true, true, true); }

    @Test void testAFalseBTrueCFalse() { evaluateTestCase(false, true, false, false); }

    @Test void testAFalseBTrueCNull() { evaluateTestCase(false, true, null, null); }

    @Test void testAFalseBFalseCTrue() { evaluateTestCase(false, false, true, true); }

    @Test void testAFalseBFalseCFalse() { evaluateTestCase(false, false, false, false); }

    @Test void testAFalseBFalseCNull() { evaluateTestCase(false, false, null, null); }

    @Test void testAFalseBNullCTrue() { evaluateTestCase(false, null, true, true); }

    @Test void testAFalseBNullCFalse() { evaluateTestCase(false, null, false, false); }

    @Test void testAFalseBNullCNull() { evaluateTestCase(false, null, null, null); }

    @Test void testANullBTrueCTrue() { evaluateTestCase(null, true, true, true); }

    @Test void testANullBTrueCFalse() { evaluateTestCase(null, true, false, null); }

    @Test void testANullBTrueCNull() { evaluateTestCase(null, true, null, null); }

    @Test void testANullBFalseCTrue() { evaluateTestCase(null, false, true, true); }

    @Test void testANullBFalseCFalse() { evaluateTestCase(null, false, false, false); }

    @Test void testANullBFalseCNull() { evaluateTestCase(null, false, null, null); }

    @Test void testANullBNullCTrue() { evaluateTestCase(null, null, true, true); }

    @Test void testANullBNullCFalse() { evaluateTestCase(null, null, false, null); }

    @Test void testANullBNullCNull() { evaluateTestCase(null, null, null, null); }
}
