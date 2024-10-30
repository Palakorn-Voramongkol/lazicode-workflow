package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.HashMap;

class NormalLogicExpressionTest {

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
    void testUnsupportedOperatorInExpression() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A B ADD"); // 'ADD' is not a supported operator
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, alow only valid infix or postfix logical expression."), "Expected error message about unsupported operator.");
    }

    @Test
    void testInsufficientOperandsForANDOperator() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A AND"); // AND requires two operands
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, alow only valid infix or postfix logical expression."), "Expected error message for insufficient operands for AND.");
    }

    @Test
    void testInsufficientOperandsForOROperator() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A OR"); // OR requires two operands
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, alow only valid infix or postfix logical expression."), "Expected error message for insufficient operands for OR.");
    }

    @Test
    void testInsufficientOperandForNOTOperator() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("NOT"); // NOT requires one operand
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, alow only valid infix or postfix logical expression."), "Expected error message for insufficient operand for NOT.");
    }

    @Test
    void testInvalidVariableToken() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A 1 AND"); // '1' is not a valid variable
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, alow only valid infix or postfix logical expression."), "Expected error message about unsupported token.");
    }

    @Test
    void testInvalidExpressionFormatWithTooManyOperands() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A B C AND"); // Leaves two results in the stack, which is invalid
        });
        assertTrue(exception.getMessage().contains("Invalid expression type, alow only valid infix or postfix logical expression."), "Expected error message for invalid postfix expression format.");
    }

    @Test
    void testComplexValidExpression() {
        // Complex expression: ((A AND B) OR (NOT C)) AND (D XOR (E NOR F))
        String expression = "A B AND C NOT OR D E F NOR XOR AND";
        
        assertDoesNotThrow(() -> new LogicExpression(expression), "Expected no exception for a valid complex expression.");
    }

    @Test
    void testEmptyExpression() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression(""); // Empty expression is invalid
        });
        assertTrue(exception.getMessage().contains("Invalid expression"), "Expected error message for an empty expression.");
    }

    /* 
    @Test
    void testValidPostfixExpressionWithAND() {
        LogicExpression expr = new LogicExpression("A B AND");
        expr.setVariable("A", true);
        expr.setVariable("B", true);
        assertEquals(true, expr.calculate(), "Expected true for expression 'A B AND' with A=true, B=true");
    }

    @Test
    void testValidPostfixExpressionWithOR() {
        LogicExpression expr = new LogicExpression("A B OR");
        expr.setVariable("A", true);
        expr.setVariable("B", false);
        assertEquals(true, expr.calculate(), "Expected true for expression 'A B OR' with A=true, B=false");
    }

    @Test
    void testValidPostfixExpressionWithNOT() {
        LogicExpression expr = new LogicExpression("A NOT");
        expr.setVariable("A", false);
        assertEquals(true, expr.calculate(), "Expected true for expression 'A NOT' with A=false");
    }

    @Test
    void testValidPostfixExpressionWithComplexOperators() {
        LogicExpression expr = new LogicExpression("A B AND C OR NOT");
        expr.setVariable("A", true);
        expr.setVariable("B", true);
        expr.setVariable("C", false);
        assertEquals(false, expr.calculate(), "Expected false for expression 'A B AND C OR NOT' with A=true, B=true, C=false");
    }

    @Test
    void testUnsupportedOperatorInExpression() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A B ADD"); // 'ADD' is not a supported operator
        });
        assertTrue(exception.getMessage().contains("Unsupported token: 'ADD'"), "Expected error message about unsupported operator.");
    }

    @Test
    void testInsufficientOperandsForANDOperator() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A AND");
        });
        assertTrue(exception.getMessage().contains("Operator 'AND' requires two operands"), "Expected error message for insufficient operands for AND.");
    }

    @Test
    void testInsufficientOperandsForNOTOperator() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("NOT");
        });
        assertTrue(exception.getMessage().contains("Operator 'NOT' requires one operand"), "Expected error message for insufficient operand for NOT.");
    }

    @Test
    void testInvalidVariableType() {
        LogicExpression expr = new LogicExpression("A B AND");
        expr.setVariable("A", true);
        expr.setVariable("B", "non-boolean"); // Non-boolean value for a variable
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, expr::calculate);
        assertTrue(exception.getMessage().contains("Variable 'B' is not a Boolean value."), "Expected error for non-boolean variable assignment.");
    }

    @Test
    void testInvalidExpressionFormatWithTooManyOperands() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A B C AND"); // Leaves two results in the stack, which is invalid
        });
        assertTrue(exception.getMessage().contains("Expected a single final result"), "Expected error for invalid postfix expression format.");
    }

    @Test
    void testCalculateWithXNOROperator() {
        LogicExpression expr = new LogicExpression("A B XNOR");
        expr.setVariable("A", true);
        expr.setVariable("B", true);
        assertEquals(true, expr.calculate(), "Expected true for expression 'A B XNOR' with A=true, B=true");
    }

    @Test
    void testCalculateWithNANDOperator() {
        LogicExpression expr = new LogicExpression("A B NAND");
        expr.setVariable("A", true);
        expr.setVariable("B", true);
        assertEquals(false, expr.calculate(), "Expected false for expression 'A B NAND' with A=true, B=true");
    }

    @Test
    void testToStringRepresentation() {
        LogicExpression expr = new LogicExpression("A B AND");
        expr.setVariable("A", true);
        expr.setVariable("B", true);
        assertTrue(expr.toString().contains("expressionString='A B AND'"), "Expected toString to contain expressionString.");
        assertTrue(expr.toString().contains("cachedResult=null"), "Expected cachedResult to be null initially.");
    }
*/
    }
