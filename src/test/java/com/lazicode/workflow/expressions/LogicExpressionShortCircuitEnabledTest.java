package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LogicExpressionShortCircuitEnabledTest {
    private LogicExpression expression;

    @BeforeEach
    public void setUp() {
        // Initialize with short-circuit enabled
        expression = new LogicExpression("A AND B OR C", true);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        expression.setVariable("C", true);
    }

    @Test
    public void testSimpleTrueFalseEvaluation() {
        expression = new LogicExpression("TRUE AND FALSE", false);
        assertFalse((Boolean) expression.calculate());

        expression = new LogicExpression("TRUE OR FALSE", false);
        assertTrue((Boolean) expression.calculate());
    }

    @Test
    public void testNotOperator() {
        expression = new LogicExpression("NOT TRUE", false);
        assertFalse((Boolean) expression.calculate());

        expression = new LogicExpression("NOT FALSE", false);
        assertTrue((Boolean) expression.calculate());
    }

    @Test
    public void testAndOperator() {
        expression = new LogicExpression("TRUE AND TRUE", false);
        assertTrue((Boolean) expression.calculate());

        expression = new LogicExpression("TRUE AND FALSE", false);
        assertFalse((Boolean) expression.calculate());
    }

    @Test
    public void testOrOperator() {
        expression = new LogicExpression("FALSE OR TRUE", false);
        assertTrue((Boolean) expression.calculate());

        expression = new LogicExpression("FALSE OR FALSE", false);
        assertFalse((Boolean) expression.calculate());
    }

    @Test
    public void testNandOperator() {
        expression = new LogicExpression("TRUE NAND TRUE", false);
        assertFalse((Boolean) expression.calculate());
    
        expression = new LogicExpression("TRUE NAND FALSE", false);
        assertTrue((Boolean) expression.calculate());
    }
    

    @Test
    public void testNorOperator() {
        expression = new LogicExpression("FALSE NOR FALSE", false);
        assertTrue((Boolean) expression.calculate());

        expression = new LogicExpression("TRUE NOR FALSE", false);
        assertFalse((Boolean) expression.calculate());
    }

    @Test
    public void testXorOperator() {
        expression = new LogicExpression("TRUE XOR FALSE", false);
        assertTrue((Boolean) expression.calculate());

        expression = new LogicExpression("TRUE XOR TRUE", false);
        assertFalse((Boolean) expression.calculate());
    }

    @Test
    public void testXnorOperator() {
        expression = new LogicExpression("TRUE XNOR FALSE", false);
        assertFalse((Boolean) expression.calculate());

        expression = new LogicExpression("TRUE XNOR TRUE", false);
        assertTrue((Boolean) expression.calculate());
    }

    @Test
    public void testNestedExpressionWithParentheses() {
        expression = new LogicExpression("( TRUE OR FALSE ) AND ( TRUE AND ( FALSE OR TRUE ) )", false);
        assertTrue((Boolean) expression.calculate());
    }

    @Test
    public void testShortCircuitAnd() {
        expression = new LogicExpression("FALSE AND TRUE", true);
        assertFalse((Boolean) expression.calculate()); // Should short-circuit

        // Changing variables shouldn't affect the outcome since short-circuit already determined the result
        expression = new LogicExpression("A AND B", true);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        assertFalse((Boolean) expression.calculate());
    }

    @Test
    public void testShortCircuitOr() {
        expression = new LogicExpression("TRUE OR FALSE", true);
        assertTrue((Boolean) expression.calculate()); // Should short-circuit

        // Changing variables shouldn't affect the outcome since short-circuit already determined the result
        expression = new LogicExpression("A OR B", true);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        assertTrue((Boolean) expression.calculate());
    }

    @Test
    public void testInvalidExpression() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A AND OR", false);
        });
        assertEquals("Invalid logic expression provided.", exception.getMessage());
    }
/* 
    @Test
    public void testSetInvalidVariableValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            expression.setVariable("A", 10); // Should throw an exception
        });
        assertEquals("LogicExpression can only accept Boolean values.", exception.getMessage());
    }
    
*/

    @Test
    void testInvalidTokenInExpression() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A AND B OR XORR C", true);
        });
        assertEquals("Invalid token detected: XORR", exception.getMessage());
    }

/*
    @Test
    public void testIsValidWithBalancedParentheses() {
        expression = new LogicExpression("( A AND B ) OR ( C AND D )", false);
        assertTrue(expression.isValid());
    }
*/
    @Test
    public void testIsInvalidWithUnbalancedParentheses() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("( A AND B OR ( C AND D", false);
        });
        assertEquals("Invalid logic expression provided.", exception.getMessage());
    }
/*
    @Test
    public void testIsValidWithValidTokens() {
        expression = new LogicExpression("A AND B OR C AND D", false);
        assertTrue(expression.isValid());
    }
*/

/* 
    @Test
    void testIsInvalidWithInvalidTokens() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A AND B OR C && D", true);
        });
        assertEquals("Invalid token detected: &&", exception.getMessage());
    }
*/
    @Test
    public void testCalculateWithVariableValues() {
        expression = new LogicExpression("A AND B OR C", false);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        expression.setVariable("C", true);
        assertTrue((Boolean) expression.calculate());
    }

    @Test
    public void testCalculateWithDifferentConfigurations() {
        expression = new LogicExpression("A AND ( B OR C )", false);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        expression.setVariable("C", true);
        assertTrue((Boolean) expression.calculate());

        expression.setVariable("C", false);
        assertFalse((Boolean) expression.calculate());
    }

    @Test
    public void testComplexNestedExpression() {
        expression = new LogicExpression("A AND ( B OR ( C AND ( D OR E ) ) )", false);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        expression.setVariable("C", true);
        expression.setVariable("D", false);
        expression.setVariable("E", true);
        assertTrue((Boolean) expression.calculate());
    }
}
