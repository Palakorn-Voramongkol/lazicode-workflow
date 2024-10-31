package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import com.lazicode.workflow.exceptions.InvalidExpression;

import static org.junit.jupiter.api.Assertions.*;

class MathExpression_ABNORMAL_Test {

    @Test
    void testUnsupportedOperatorInExpression() {
        try {
            new MathExpression("A B ADD"); // 'ADD' is not a supported operator
            fail("Invalid token found in expression: 'ADD'");
        } catch (InvalidExpression e) {
            assertTrue(e.getMessage().contains("Invalid token found in expression: 'ADD'"),
                    "Expected error message about unsupported operator.");
        }
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
    void testInsufficientOperandForUnaryMinus() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new MathExpression("-"); // Unary minus requires one operand
        });
        assertTrue(
                exception.getMessage()
                        .contains("Invalid expression type, allow only valid infix or postfix mathematical expressions."),
                "Expected error message for insufficient operand for unary minus.");
    }

    @Test
    void testInvalidVariableToken() {
        try {
            InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
                new MathExpression("A 1 +"); // '1' is not a valid variable
            });
            assertTrue(exception.getMessage().contains("Invalid token found in expression: '1'"),
                    "Expected error message about invalid variable token.");
        } catch (AssertionError e) {
            fail("Expected InvalidExpression exception due to unsupported variable '1', but it was not thrown.");
        }
    }

    @Test
    void testInvalidExpressionFormatWithTooManyOperands() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new MathExpression("A B C +"); // Leaves two results in the stack, which is invalid
        });
    
        assertTrue(
                exception.getMessage()
                        .contains("Invalid postfix expression format."),
                "Expected error message for invalid postfix expression format.");
    }

    @Test
    void testComplexValidExpression() {
        // Complex expression: ((A + B) * (C - D)) / (E % F)
        String expression = "A B + C D - * E F % /";

        assertDoesNotThrow(() -> new MathExpression(expression),
                "Expected no exception for a valid complex expression.");
    }

    @Test
    void testMultipleUnaryOperators() {
        // Complex expression: "- - A"
        String expression = "- - A";

        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new MathExpression(expression);
        });
        // Check that the exception message indicates an issue with multiple unary operators
        assertTrue(exception.getMessage().contains("Invalid expression type, allow only valid infix or postfix mathematical expressions."),
                "Expected error message about invalid multiple unary operators.");
    }

}
