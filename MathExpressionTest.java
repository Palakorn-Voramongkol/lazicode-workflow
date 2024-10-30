package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MathExpressionTest {

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
    void testExponentValidExpression() {
        assertDoesNotThrow(() -> new MathExpression("A B ^"), "Expected no exception for a valid expression 'A B ^'.");
    }

    @Test
    void testComplexExpressionWithMultipleOperators() {
        // Complex expression: ((A + B) * (C - D)) / (E ^ F)
        String expression = "A B + C D - * E F ^ /";
        assertDoesNotThrow(() -> new MathExpression(expression), "Expected no exception for a valid complex expression.");
    }

    @Test
    void testUnsupportedOperatorInExpression() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("A B @"); // '@' is not a supported operator
        });
        assertTrue(exception.getMessage().contains("Unsupported token"), "Expected error message about unsupported operator.");
    }

    @Test
    void testInsufficientOperandsForAdditionOperator() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("A +"); // '+' requires two operands
        });
        assertTrue(exception.getMessage().contains("requires two operands"), "Expected error message for insufficient operands for '+'.");
    }

    @Test
    void testInsufficientOperandForUnaryMinusOperator() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("-"); // Unary minus requires one operand
        });
        assertTrue(exception.getMessage().contains("requires one operand"), "Expected error message for insufficient operand for unary '-'.");
    }

    @Test
    void testInvalidVariableToken() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("A 1 +"); // '1' is not a valid variable
        });
        assertTrue(exception.getMessage().contains("Unsupported token"), "Expected error message about unsupported token.");
    }

    @Test
    void testInvalidExpressionFormatWithTooManyOperands() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("A B C +"); // Leaves two results in the stack, which is invalid
        });
        assertTrue(exception.getMessage().contains("Expected a single final result"), "Expected error message for invalid postfix expression format.");
    }

    @Test
    void testEmptyExpression() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression(""); // Empty expression is invalid
        });
        assertTrue(exception.getMessage().contains("Invalid expression"), "Expected error message for an empty expression.");
    }
}
