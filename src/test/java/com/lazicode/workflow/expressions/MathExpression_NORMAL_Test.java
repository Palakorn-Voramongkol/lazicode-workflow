package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import com.lazicode.workflow.exceptions.InvalidExpression;
import static org.junit.jupiter.api.Assertions.*;

class MathExpression_NORMAL_Test {

    @Test
    void testAdditionValidExpression() {
        assertDoesNotThrow(() -> new MathExpression("A B +"), "Expected no exception for a valid expression 'A B +'.");
    }

    @Test
    void testSubtractionValidExpression() {
        assertDoesNotThrow(() -> new MathExpression("A B -"), "Expected no exception for a valid expression 'A B -'.");
    }

    @Test
    void testMultiplicationValidExpression() {
        assertDoesNotThrow(() -> new MathExpression("A B *"), "Expected no exception for a valid expression 'A B *'.");
    }

    @Test
    void testDivisionValidExpression() {
        assertDoesNotThrow(() -> new MathExpression("A B /"), "Expected no exception for a valid expression 'A B /'.");
    }

    @Test
    void testModulusValidExpression() {
        assertDoesNotThrow(() -> new MathExpression("A B %"), "Expected no exception for a valid expression 'A B %'.");
    }

    @Test
    void testPowerValidExpression() {
        assertDoesNotThrow(() -> new MathExpression("A B ^"), "Expected no exception for a valid expression 'A B +'.");
    }

    @Test
    void testComplexExpressionWithMultipleOperators() {
        assertDoesNotThrow(() -> new MathExpression("A B + C D / * E %"),
                "Expected no exception for a valid complex expression 'A B + C D / * E %'.");
    }

    @Test
    void testComplexExpressionWithExponentiationAndMultipleOperators() {
        assertDoesNotThrow(() -> new MathExpression("A B ^ C D / * E %"),
                "Expected no exception for a valid complex expression 'A B ^ C D / * E %'.");
    }


    @Test
    void testInsufficientOperandsForAdditionOperator() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new MathExpression("A +"); // '+' requires two operands
        });
        assertTrue(
                exception.getMessage()
                        .contains("Invalid expression type, allow only valid infix or postfix mathematical expressions."),
                "Expected error message for insufficient operands for '+'.");
    }

    @Test
    void testInsufficientOperandsForMultiplicationOperator() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new MathExpression("A *"); // '*' requires two operands
        });
        assertTrue(
                exception.getMessage()
                        .contains("Invalid expression type, allow only valid infix or postfix mathematical expressions."),
                "Expected error message for insufficient operands for '*'.");
    }

    @Test
    void testInvalidVariableToken() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new MathExpression("A 1 +"); // '1' is not a valid variable
        });
        assertTrue(exception.getMessage().contains("Invalid token found in expression: '1'"),
                "Expected error message about unsupported token.");
    }

    @Test
    void testInvalidExpressionFormatWithTooManyOperands() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new MathExpression("A B C +"); // Leaves two results in the stack, which is invalid
        });
        assertTrue(exception.getMessage().contains("Invalid postfix expression format."),
                "Expected error message for invalid postfix expression format.");
    }

    @Test
    void testComplexValidExpression() {
        // Complex expression: ((A + B) * (C - D)) / (E % F)
        String expression = "A B + C D - * E F % /";

        assertDoesNotThrow(() -> new MathExpression(expression),
                "Expected no exception for a valid complex expression.");
    }
}
