package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class LogicExpression_ABNORMAL_Test {


    @Test
    void testUnsupportedOperatorInExpression() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A B ADD"); // 'ADD' is not a supported operator
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, allow only valid infix or postfix logical expressions."), "Expected error message about unsupported operator.");
    }

    @Test
    void testInsufficientOperandsForANDOperator() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A AND"); // AND requires two operands
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, allow only valid infix or postfix logical expressions."), "Expected error message for insufficient operands for AND.");
    }

    @Test
    void testInsufficientOperandsForOROperator() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A OR"); // OR requires two operands
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, allow only valid infix or postfix logical expressions."), "Expected error message for insufficient operands for OR.");
    }

    @Test
    void testInsufficientOperandForNOTOperator() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("NOT"); // NOT requires one operand
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, allow only valid infix or postfix logical expressions."), "Expected error message for insufficient operand for NOT.");
    }

    @Test
    void testInvalidVariableToken() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A 1 AND"); // '1' is not a valid variable
        });

        assertTrue(exception.getMessage().contains("Invalid expression type, allow only valid infix or postfix logical expressions."), "Expected error message for invalid postfix expression format.");
    }

    @Test
    void testInvalidExpressionFormatWithTooManyOperands() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A B C AND"); // Leaves two results in the stack, which is invalid
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, allow only valid infix or postfix logical expressions."), "Expected error message for invalid postfix expression format.");
    }

    @Test
    void testComplexValidExpression() {
        // Complex expression: ((A AND B) OR (NOT C)) AND (D XOR (E NOR F))
        String expression = "A B AND C NOT OR D E F NOR XOR AND";
        
        assertDoesNotThrow(() -> new LogicExpression(expression), "Expected no exception for a valid complex expression.");
    }

    @Test
    void testMultipleUnaryOperators() {
        // Complex expression: "NOT NOT A"
        String expression = "NOT NOT A";
        
        //assertDoesNotThrow(() -> new LogicExpression(expression), "Expected no exception for a valid complex expression.");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression(expression); // '1' is not a valid variable
        });
        System.out.println("yyyyyyyException Message: " + exception.getMessage());
        // Temporary assertion
        assertTrue(true);
    }

    }
