package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;

import com.lazicode.workflow.exceptions.InvalidExpression;

import static org.junit.jupiter.api.Assertions.*;


class LogicExpression_NORMAL_Test {

    @Test
    void testANDValidExpression() {
        assertDoesNotThrow(() -> new LogicExpression("A B AND"), "Expected no exception for a valid expression 'A B AND'.");
    }

    @Test
    void testORValidExpression() {
        assertDoesNotThrow(() -> new LogicExpression("A B OR"), "Expected no exception for a valid expression 'A B OR'.");
    }

    @Test
    void testNOTValidExpression() {
        assertDoesNotThrow(() -> new LogicExpression("A NOT"), "Expected no exception for a valid expression 'A NOT'.");
    }

    @Test
    void testNANDValidExpression() {
        assertDoesNotThrow(() -> new LogicExpression("A B NAND"), "Expected no exception for a valid expression 'A B NAND'.");
    }

    @Test
    void testNORValidExpression() {
        assertDoesNotThrow(() -> new LogicExpression("A B NOR"), "Expected no exception for a valid expression 'A B NOR'.");
    }

    @Test
    void testXORValidExpression() {
        assertDoesNotThrow(() -> new LogicExpression("A B XOR"), "Expected no exception for a valid expression 'A B XOR'.");
    }

    @Test
    void testXNORValidExpression() {
        assertDoesNotThrow(() -> new LogicExpression("A B XNOR"), "Expected no exception for a valid expression 'A B XNOR'.");
    }

    @Test
    void testAND_NOTValidExpression() {
        assertDoesNotThrow(() -> new LogicExpression("A B AND C NOT OR"), "Expected no exception for a valid expression 'A B XNOR'.");
    }



    @Test
    void test_NOTNOTValidExpression() {
        assertDoesNotThrow(() -> new LogicExpression("A NOT NOT"), "Expected no exception for a valid expression 'NOT NOT A'.");
    }

    @Test
    void testUnsupportedOperatorInExpression() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new LogicExpression("A B ADD"); // 'ADD' is not a supported operator
        });
        assertTrue(exception.getMessage().contains("Invalid token found in expression: 'ADD'"), "Expected error message about unsupported operator.");
    }

    @Test
    void testInsufficientOperandsForANDOperator() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new LogicExpression("A AND"); // AND requires two operands
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, allow only valid infix or postfix logical expressions."), "Expected error message for insufficient operands for AND.");
    }

    @Test
    void testInsufficientOperandsForOROperator() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new LogicExpression("A OR"); // OR requires two operands
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, allow only valid infix or postfix logical expressions."), "Expected error message for insufficient operands for OR.");
    }

    @Test
    void testInsufficientOperandForNOTOperator() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new LogicExpression("NOT"); // NOT requires one operand
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, allow only valid infix or postfix logical expressions."), "Expected error message for insufficient operand for NOT.");
    }

    @Test
    void testInvalidVariableToken() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new LogicExpression("A 1 AND"); // '1' is not a valid variable
        });
        assertTrue(exception.getMessage().contains("Invalid token found in expression: '1'"), "Expected error message about unsupported token.");
    }

    @Test
    void testInvalidExpressionFormatWithTooManyOperands() {
        InvalidExpression exception = assertThrows(InvalidExpression.class, () -> {
            new LogicExpression("A B C AND"); // Leaves two results in the stack, which is invalid
        });
        assertTrue(exception.getMessage().contains("Invalid postfix expression format."), "Expected error message for invalid postfix expression format.");
    }

    @Test
    void testComplexValidExpression() {
        // Complex expression: ((A AND B) OR (NOT C)) AND (D XOR (E NOR F))
        String expression = "A B AND C NOT OR D E F NOR XOR AND";
        
        assertDoesNotThrow(() -> new LogicExpression(expression), "Expected no exception for a valid complex expression.");
    }

    }
