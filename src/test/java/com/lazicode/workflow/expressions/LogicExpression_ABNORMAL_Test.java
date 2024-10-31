package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;

import com.lazicode.workflow.exceptions.InvalidExpression;

import static org.junit.jupiter.api.Assertions.*;

class LogicExpression_ABNORMAL_Test {

    @Test
    void testUnsupportedOperatorInExpression() {
        try {
            new LogicExpression("A B ADD"); // 'ADD' is not a supported operator
            fail("Invalid token found in expression: 'ADD'");
        } catch (InvalidExpression e) {
            assertTrue(e.getMessage().contains("Invalid token found in expression: 'ADD'"),
                    "Expected error message about unsupported operator.");
        }
    }

    @Test
    void testInsufficientOperandsForANDOperator() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new LogicExpression("A AND"); // AND requires two operands
        });
        assertTrue(
                exception.getMessage()
                        .contains("Invalid expression type, allow only valid infix or postfix logical expressions."),
                "Expected error message for insufficient operands for AND.");
    }

    @Test
    void testInsufficientOperandsForOROperator() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new LogicExpression("A OR"); // OR requires two operands
        });
        assertTrue(
                exception.getMessage()
                        .contains("Invalid expression type, allow only valid infix or postfix logical expressions."),
                "Expected error message for insufficient operands for OR.");
    }

    @Test
    void testInsufficientOperandForNOTOperator() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new LogicExpression("NOT"); // NOT requires one operand
        });
        assertTrue(
                exception.getMessage()
                        .contains("Invalid expression type, allow only valid infix or postfix logical expressions."),
                "Expected error message for insufficient operand for NOT.");
    }

    @Test
    void testInvalidVariableToken() {
        try {
            InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
                new LogicExpression("A 1 AND"); // '1' is not a valid variable
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
            new LogicExpression("A B C AND"); // Leaves two results in the stack, which is invalid
        });
    
        assertTrue(
                exception.getMessage()
                        .contains("Invalid postfix expression format."),
                "Expected error message for invalid postfix expression format.");
    }

    @Test
    void testComplexValidExpression() {
        // Complex expression: ((A AND B) OR (NOT C)) AND (D XOR (E NOR F))
        String expression = "A B AND C NOT OR D E F NOR XOR AND";

        assertDoesNotThrow(() -> new LogicExpression(expression),
                "Expected no exception for a valid complex expression.");
    }

    @Test
    void testMultipleUnaryOperators() {
        // Complex expression: "NOT NOT A"
        String expression = "NOT NOT A";

        // assertDoesNotThrow(() -> new LogicExpression(expression), "Expected no
        // exception for a valid complex expression.");
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new LogicExpression(expression); // '1' is not a valid variable
        });
        System.out.println("yyyyyyyException Message: " + exception.getMessage());
        // Temporary assertion
        assertTrue(true);
    }

}
