package com.lazicode.workflow.expressions.evaluators;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PostfixMath_Test {
    

    // Helper function to evaluate a single test case for PostfixMath
    private void evaluateTestCase(String expression, Map<String, Double> valueMap, Double expected) {
        Double result = PostfixMath.eval(expression, valueMap);

        if (expected == null) {
            assertTrue(result.isNaN(), String.format("Failed for expression: '%s' with values %s", expression, valueMap));
        } else {
            assertEquals(expected, result, 0.0001, String.format("Failed for expression: '%s' with values %s", expression, valueMap));
        }
    }

    @Test
    void testAddition() {
        evaluateTestCase("A B +", Map.of("A", 5.0, "B", 3.0), 8.0); // 5 + 3 = 8
    }

    @Test
    void testSubtraction() {
        evaluateTestCase("A B -", Map.of("A", 10.0, "B", 4.0), 6.0); // 10 - 4 = 6
    }

    @Test
    void testMultiplication() {
        evaluateTestCase("A B *", Map.of("A", 2.0, "B", 3.0), 6.0); // 2 * 3 = 6
    }

    @Test
    void testDivision() {
        evaluateTestCase("A B /", Map.of("A", 8.0, "B", 2.0), 4.0); // 8 / 2 = 4
    }

    @Test
    void testDivisionByZero() {
        evaluateTestCase("A B /", Map.of("A", 8.0, "B", 0.0), Double.NaN); // 8 / 0 = NaN (division by zero)
    }

    @Test
    void testModulus() {
        evaluateTestCase("A B %", Map.of("A", 10.0, "B", 3.0), 1.0); // 10 % 3 = 1
    }

    @Test
    void testExponentiation() {
        evaluateTestCase("A B ^", Map.of("A", 2.0, "B", 3.0), 8.0); // 2^3 = 8
    }

    @Test
    void testComplexExpression1() {
        // (A + B) * (C - D) / E -> A B + C D - * E /
        evaluateTestCase("A B + C D - * E /", Map.of("A", 3.0, "B", 2.0, "C", 8.0, "D", 4.0, "E", 2.0), 10.0);
    }

    @Test
    void testComplexExpression2() {
        // A ^ B + C * D % E -> A B ^ C D * E % +
        evaluateTestCase("A B ^ C D * E % +", Map.of("A", 2.0, "B", 3.0, "C", 4.0, "D", 5.0, "E", 6.0), 10.0); // 2^3 + (4 * 5) % 6 = 12
    }

    @Test
    void testExpressionWithMissingValues() {
        // A B + C * where C is NaN -> Result should be NaN
        evaluateTestCase("A B + C *", Map.of("A", 2.0, "B", 3.0, "C", Double.NaN), Double.NaN);
    }

    @Test
    void testExpressionAllOperators() {
        // (A * B) + (C / D) - (E % F) ^ G -> A B * C D / + E F % G ^ -
        evaluateTestCase("A B * C D / + E F % G ^ -", Map.of("A", 3.0, "B", 4.0, "C", 8.0, "D", 2.0, "E", 10.0, "F", 3.0, "G", 2.0), 15.0); // (3 * 4) + (8 / 2) - (10 % 3)^2 = 17
    }

    @Test
    void testNegativeResult() {
        // A - B * C -> A B C * -
        evaluateTestCase("A B C * -", Map.of("A", 5.0, "B", 3.0, "C", 2.0), -1.0); // 5 - (3 * 2) = -1
    }

    @Test
    void testExpressionWithZeroOperands() {
        // A + B - C * D -> A B + C D * -
        evaluateTestCase("A B + C D * -", Map.of("A", 0.0, "B", 0.0, "C", 1.0, "D", 2.0), -2.0); // 0 + 0 - (1 * 2) = -2
    }

    @Test
    void testExpressionWithLargeNumbers() {
        // A ^ B * C -> A B ^ C *
        evaluateTestCase("A B ^ C *", Map.of("A", 10.0, "B", 2.0, "C", 5.0), 500.0); // 10^2 * 5 = 5000
    }
}
