package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lazicode.workflow.exceptions.InvalidOperatorPlacementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the LogicExpression class with short-circuiting enabled.
 */
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

    // -------------------- Valid Expression Tests --------------------

    @Test
    public void testSimpleTrueFalseEvaluation() {
        expression = new LogicExpression("TRUE AND FALSE", false);
        assertFalse((Boolean) expression.calculate(), "Expression 'TRUE AND FALSE' should evaluate to FALSE");

        expression = new LogicExpression("TRUE OR FALSE", false);
        assertTrue((Boolean) expression.calculate(), "Expression 'TRUE OR FALSE' should evaluate to TRUE");
    }

    @Test
    public void testNotOperator() {
        expression = new LogicExpression("NOT TRUE", false);
        assertFalse((Boolean) expression.calculate(), "Expression 'NOT TRUE' should evaluate to FALSE");

        expression = new LogicExpression("NOT FALSE", false);
        assertTrue((Boolean) expression.calculate(), "Expression 'NOT FALSE' should evaluate to TRUE");
    }

    @Test
    public void testAndOperator() {
        expression = new LogicExpression("TRUE AND TRUE", false);
        assertTrue((Boolean) expression.calculate(), "Expression 'TRUE AND TRUE' should evaluate to TRUE");

        expression = new LogicExpression("TRUE AND FALSE", false);
        assertFalse((Boolean) expression.calculate(), "Expression 'TRUE AND FALSE' should evaluate to FALSE");
    }

    @Test
    public void testOrOperator() {
        expression = new LogicExpression("FALSE OR TRUE", false);
        assertTrue((Boolean) expression.calculate(), "Expression 'FALSE OR TRUE' should evaluate to TRUE");

        expression = new LogicExpression("FALSE OR FALSE", false);
        assertFalse((Boolean) expression.calculate(), "Expression 'FALSE OR FALSE' should evaluate to FALSE");
    }

    @Test
    public void testNandOperator() {
        expression = new LogicExpression("TRUE NAND TRUE", false);
        assertFalse((Boolean) expression.calculate(), "Expression 'TRUE NAND TRUE' should evaluate to FALSE");

        expression = new LogicExpression("TRUE NAND FALSE", false);
        assertTrue((Boolean) expression.calculate(), "Expression 'TRUE NAND FALSE' should evaluate to TRUE");
    }

    @Test
    public void testNorOperator() {
        expression = new LogicExpression("FALSE NOR FALSE", false);
        assertTrue((Boolean) expression.calculate(), "Expression 'FALSE NOR FALSE' should evaluate to TRUE");

        expression = new LogicExpression("TRUE NOR FALSE", false);
        assertFalse((Boolean) expression.calculate(), "Expression 'TRUE NOR FALSE' should evaluate to FALSE");
    }

    @Test
    public void testXorOperator() {
        expression = new LogicExpression("TRUE XOR FALSE", false);
        assertTrue((Boolean) expression.calculate(), "Expression 'TRUE XOR FALSE' should evaluate to TRUE");

        expression = new LogicExpression("TRUE XOR TRUE", false);
        assertFalse((Boolean) expression.calculate(), "Expression 'TRUE XOR TRUE' should evaluate to FALSE");
    }

    @Test
    public void testXnorOperator() {
        expression = new LogicExpression("TRUE XNOR FALSE", false);
        assertFalse((Boolean) expression.calculate(), "Expression 'TRUE XNOR FALSE' should evaluate to FALSE");

        expression = new LogicExpression("TRUE XNOR TRUE", false);
        assertTrue((Boolean) expression.calculate(), "Expression 'TRUE XNOR TRUE' should evaluate to TRUE");
    }

    @Test
    public void testNestedExpressionWithParentheses() {
        expression = new LogicExpression("( TRUE OR FALSE ) AND ( TRUE AND ( FALSE OR TRUE ) )", false);
        assertTrue((Boolean) expression.calculate(), "Nested expression should evaluate to TRUE");
    }

    // -------------------- Short-Circuiting Behavior Tests --------------------

    @Test
    public void testShortCircuitAnd() {
        expression = new LogicExpression("FALSE AND TRUE", true);
        assertFalse((Boolean) expression.calculate(), "Expression 'FALSE AND TRUE' should short-circuit to FALSE");

        // Changing variables shouldn't affect the outcome since short-circuit already
        // determined the result
        expression = new LogicExpression("A AND B", true);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        assertFalse((Boolean) expression.calculate(),
                "Expression 'A AND B' with A=true and B=false should evaluate to FALSE");
    }

    @Test
    public void testShortCircuitOr() {
        expression = new LogicExpression("TRUE OR FALSE", true);
        assertTrue((Boolean) expression.calculate(), "Expression 'TRUE OR FALSE' should short-circuit to TRUE");

        // Changing variables shouldn't affect the outcome since short-circuit already
        // determined the result
        expression = new LogicExpression("A OR B", true);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        assertTrue((Boolean) expression.calculate(),
                "Expression 'A OR B' with A=true and B=false should evaluate to TRUE");
    }

    @Test
    public void testShortCircuitMixedOperators() {
        expression = new LogicExpression("A AND ( B OR C )", true);
        expression.setVariable("A", false);
        expression.setVariable("B", true); // Should not be evaluated due to short-circuit
        expression.setVariable("C", false);
        assertFalse((Boolean) expression.calculate(),
                "Expression 'A AND ( B OR C )' with A=false should short-circuit to FALSE");
    }

    // -------------------- Invalid Expression Tests --------------------

@Test
public void testInvalidOperatorPlacement() {
    Exception exception = assertThrows(InvalidOperatorPlacementException.class, () -> {
        new LogicExpression("A AND OR", false);
    });
    assertEquals("Invalid operator placement: OR after AND", exception.getMessage(),
            "Exception message should indicate invalid operator placement.");
}


    @Test
    public void testInvalidTokenInExpression() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A AND B OR XORR C", true);
        });
        assertEquals("Invalid token detected: XORR", exception.getMessage(),
                "Exception message should indicate invalid token.");
    }

    @Test
    public void testIsInvalidWithUnbalancedParentheses() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("( A AND B OR ( C AND D", false);
        });
        assertTrue(exception.getMessage().contains("Mismatched parentheses"),
                "Exception message should indicate mismatched parentheses.");
    }

    @Test
    public void testIsInvalidWithConsecutiveOperators() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A AND AND B", false);
        });
        assertEquals("Invalid operator placement: AND after AND", exception.getMessage(),
                "Exception message should indicate invalid operator placement with consecutive operators.");
    }

    @Test
    public void testInvalidExpressionStartingWithOperator() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("OR A AND B", false);
        });
        assertEquals("Invalid operator placement: OR after ", exception.getMessage(),
                "Exception message should indicate invalid operator placement with leading operator.");
    }

    @Test
    public void testInvalidExpressionEndingWithOperator() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A AND B OR", false);
        });
        assertEquals("Expression cannot end with an operator.",
                exception.getMessage(),
                "Exception message should indicate invalid expression.");
    }

    // -------------------- Edge Case Tests --------------------

    @Test
    public void testEmptyExpression() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("", false);
        });
        assertEquals("Invalid expression: Expression cannot be empty.",
                exception.getMessage(),
                "Exception message should indicate an empty expression.");
    }
    

    @Test
    public void testEmptyParentheses() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A AND ( ) OR B", false);
        });
        assertEquals("Empty parentheses detected.", exception.getMessage(),
                "Exception message should indicate invalid operand placement with empty parentheses.");
    }

    @Test
    public void testNestedEmptyParentheses() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("A AND ( ( ) ) OR B", false);
        });
        assertTrue(exception.getMessage().contains("Empty parentheses detected"),
                "Exception message should indicate invalid operand placement with nested empty parentheses.");
    }

    @Test
    public void testExpressionWithOnlyOperators() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LogicExpression("AND OR NOT", false);
        });
        assertTrue(exception.getMessage().contains("Invalid operator placement"),
                "Exception message should indicate invalid operator placement with only operators.");
    }

    // -------------------- Variable Assignment Tests --------------------

    @Test
    public void testCalculateWithVariableValues() {
        expression = new LogicExpression("A AND B OR C", false);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        expression.setVariable("C", true);
        assertTrue((Boolean) expression.calculate(),
                "Expression 'A AND B OR C' with A=true, B=false, C=true should evaluate to TRUE");
    }

    @Test
    public void testCalculateWithDifferentConfigurations() {
        expression = new LogicExpression("A AND ( B OR C )", false);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        expression.setVariable("C", true);
        assertTrue((Boolean) expression.calculate(),
                "Expression 'A AND ( B OR C )' with A=true, B=false, C=true should evaluate to TRUE");

        expression.setVariable("C", false);
        assertFalse((Boolean) expression.calculate(),
                "Expression 'A AND ( B OR C )' with C=false should evaluate to FALSE");
    }

    @Test
    public void testComplexNestedExpression() {
        expression = new LogicExpression("A AND ( B OR ( C AND ( D OR E ) ) )", false);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        expression.setVariable("C", true);
        expression.setVariable("D", false);
        expression.setVariable("E", true);
        assertTrue((Boolean) expression.calculate(), "Complex nested expression should evaluate to TRUE");
    }

    @Test
    public void testExpressionWithAllVariablesFalse() {
        expression = new LogicExpression("A AND B OR C NAND D NOR E XNOR F", false);
        expression.setVariable("A", false);
        expression.setVariable("B", false);
        expression.setVariable("C", false);
        expression.setVariable("D", false);
        expression.setVariable("E", false);
        expression.setVariable("F", false);
        // Expected evaluation:
        // A AND B = false AND false = false
        // C NAND D = false NAND false = true
        // E XNOR F = false XNOR false = true
        // (A AND B) OR (C NAND D) NOR (E XNOR F) = false OR true NOR true = true NOR
        // true = false
        assertFalse((Boolean) expression.calculate(), "Expression with all variables FALSE should evaluate to FALSE");
    }

    // -------------------- Additional Comprehensive Tests --------------------

    @Test
    public void testDoubleNegation() {
        expression = new LogicExpression("NOT NOT TRUE", false);
        assertTrue((Boolean) expression.calculate(), "Expression 'NOT NOT TRUE' should evaluate to TRUE");
    }

    @Test
    public void testMixedOperators() {
        expression = new LogicExpression("A OR ( NOT B AND C )", false);
        expression.setVariable("A", false);
        expression.setVariable("B", true);
        expression.setVariable("C", true);
        assertFalse((Boolean) expression.calculate(),
                "Expression 'A OR ( NOT B AND C )' with A=false, B=true, C=true should evaluate to FALSE");

        expression.setVariable("A", true);
        assertTrue((Boolean) expression.calculate(),
                "Expression 'A OR ( NOT B AND C )' with A=true, B=true, C=true should evaluate to TRUE");
    }

    @Test
    public void testAllOperatorsInOneExpression() {
        expression = new LogicExpression("A AND NOT B OR C XOR D XNOR E NAND F NOR G", false);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        expression.setVariable("C", true);
        expression.setVariable("D", false);
        expression.setVariable("E", true);
        expression.setVariable("F", false);
        expression.setVariable("G", false);
        // Evaluate step-by-step:
        // NOT B = true
        // A AND NOT B = true AND true = true
        // C XOR D = true XOR false = true
        // E XNOR F = true XNOR false = false
        // C XOR D XNOR E = true XNOR true = true
        // E NAND F = true NAND false = true
        // Final expression: true OR true NOR true = true NOR true = false
        assertFalse((Boolean) expression.calculate(), "Complex expression with all operators should evaluate to FALSE");
    }

    @Test
    public void testRedundantOperators() {
        Exception exception = assertThrows(InvalidOperatorPlacementException.class, () -> {
            new LogicExpression("A AND AND B", false);
        }, "Expression with consecutive AND operators should throw InvalidOperatorPlacementException");
        assertEquals("Invalid operator placement: AND after AND", exception.getMessage(),
                "Exception message should indicate invalid operator placement with consecutive operators.");
    }

    
    @Test
    public void testUnaryNotOperator() {
        expression = new LogicExpression("NOT A AND B", false);
        expression.setVariable("A", true);
        expression.setVariable("B", true);
        assertFalse((Boolean) expression.calculate(),
                "Expression 'NOT A AND B' with A=true, B=true should evaluate to FALSE");
    }

    @Test
    public void testMultipleNotOperators() {
        expression = new LogicExpression("NOT NOT A", false);
        expression.setVariable("A", true);
        assertTrue((Boolean) expression.calculate(), "Expression 'NOT NOT A' with A=true should evaluate to TRUE");
    }

    @Test
    public void testRedundantParentheses() {
        expression = new LogicExpression("((A AND B)) OR ((C))", false);
        expression.setVariable("A", true);
        expression.setVariable("B", true);
        expression.setVariable("C", false);
        assertTrue((Boolean) expression.calculate(), "Expression '((A AND B)) OR ((C))' should evaluate to TRUE");
    }


    @Test
    public void testAllVariablesTrue() {
        expression = new LogicExpression("A AND B OR C NAND D NOR E XNOR F", false);
        expression.setVariable("A", true);
        expression.setVariable("B", true);
        expression.setVariable("C", true);
        expression.setVariable("D", true);
        expression.setVariable("E", true);
        expression.setVariable("F", true);
        // Expected evaluation:
        // A AND B = true AND true = true
        // C NAND D = true NAND true = false
        // E XNOR F = true XNOR true = true
        // (A AND B) OR (C NAND D) NOR (E XNOR F) = true OR false NOR true = true NOR
        // true = false
        assertFalse((Boolean) expression.calculate(), "Expression with all variables TRUE should evaluate to FALSE");
    }

    @Test
    public void testExpressionWithUndefinedVariable() {
        expression = new LogicExpression("A AND B", false);
        expression.setVariable("A", true);
        // B is not set
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            expression.calculate();
        });
        assertEquals("Variable B has not been set.", exception.getMessage(),
                "Exception message should indicate undefined variable.");
    }

    @Test
    public void testSetInvalidVariableValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            expression.setVariable("A", 10); // Should throw an exception since A expects a Boolean
        });
        assertEquals("LogicExpression can only accept Boolean values.", exception.getMessage(),
                "Exception message should indicate invalid variable value.");
    }

    @Test
    public void testSetNullVariableValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            expression.setVariable("A", null); // Should throw an exception since null is not allowed
        });
        assertEquals("LogicExpression can only accept Boolean values.", exception.getMessage(),
                "Exception message should indicate invalid variable value.");
    }

    // -------------------- Caching Behavior Tests --------------------

    @Test
    public void testCalculateUsesCachedResult() {
        // First calculation
        Boolean firstResult = (Boolean) expression.calculate();
        assertEquals(true, firstResult, "First calculation should evaluate to TRUE");

        // Second calculation without variable changes should use cache
        Boolean secondResult = (Boolean) expression.calculate();
        assertEquals(true, secondResult, "Second calculation should return cached result TRUE");
    }

    @Test
    public void testCalculateCacheInvalidationOnVariableUpdate() {
        // First calculation
        Boolean firstResult = (Boolean) expression.calculate();
        assertEquals(true, firstResult, "First calculation should evaluate to TRUE");

        // Update a variable
        expression.setVariable("B", true);

        // Second calculation should re-evaluate
        Boolean secondResult = (Boolean) expression.calculate();
        // New expression: A AND B OR C = true AND true OR true = true OR true = true
        assertEquals(true, secondResult, "Second calculation after variable update should evaluate to TRUE");
    }

    @Test
    public void testCalculateCacheInvalidationOnMultipleVariableUpdates() {
        // First calculation
        Boolean firstResult = (Boolean) expression.calculate();
        assertEquals(true, firstResult, "First calculation should evaluate to TRUE");

        // Update variables
        expression.setVariable("A", false);
        expression.setVariable("C", false);

        // Second calculation should re-evaluate
        Boolean secondResult = (Boolean) expression.calculate();
        // New expression: false AND B OR false = false OR false = false
        assertEquals(false, secondResult,
                "Second calculation after multiple variable updates should evaluate to FALSE");
    }

    // -------------------- Additional Edge Case Tests --------------------

    /**
     * Tests that an expression containing only the NOT operator is invalid.
     * Expected to throw an IllegalArgumentException with a message containing
     * "Invalid operator placement".
     */
    @Test
    public void testExpressionWithOnlyNotOperator() {
        expression = new LogicExpression("NOT", false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            expression.calculate();
        }, "Expected calculate() to throw, but it didn't");
        assertTrue(exception.getMessage().contains("Invalid operator placement"),
                "Exception message should indicate invalid operator placement with only operators.");
    }

    @Test
    public void testExpressionWithMultipleNotOperators() {
        expression = new LogicExpression("NOT NOT A", false);
        expression.setVariable("A", true);
        assertTrue((Boolean) expression.calculate(), "Expression 'NOT NOT A' with A=true should evaluate to TRUE");
    }

    @Test
    public void testExpressionWithRedundantParentheses() {
        expression = new LogicExpression("((A AND B)) OR ((C))", false);
        expression.setVariable("A", true);
        expression.setVariable("B", true);
        expression.setVariable("C", false);
        assertTrue((Boolean) expression.calculate(), "Expression '((A AND B)) OR ((C))' should evaluate to TRUE");
    }

    @Test
    public void testExpressionWithAllVariablesTrue() {
        expression = new LogicExpression("A AND B OR C NAND D NOR E XNOR F", false);
        expression.setVariable("A", true);
        expression.setVariable("B", true);
        expression.setVariable("C", true);
        expression.setVariable("D", true);
        expression.setVariable("E", true);
        expression.setVariable("F", true);
        // Expected evaluation:
        // A AND B = true AND true = true
        // C NAND D = true NAND true = false
        // E XNOR F = true XNOR true = true
        // (A AND B) OR (C NAND D) NOR (E XNOR F) = true OR false NOR true = true NOR
        // true = false
        assertFalse((Boolean) expression.calculate(), "Expression with all variables TRUE should evaluate to FALSE");
    }

    // -------------------- Logical Operator Precedence Tests --------------------

    @Test
    public void testOperatorPrecedenceWithoutParentheses() {
        expression = new LogicExpression("A AND B OR C", false);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        expression.setVariable("C", true);
        // Expected: (A AND B) OR C = (true AND false) OR true = false OR true = true
        assertTrue((Boolean) expression.calculate(), "Expression 'A AND B OR C' should evaluate to TRUE");

        expression.setVariable("C", false);
        // Expected: (A AND B) OR C = (true AND false) OR false = false OR false = false
        assertFalse((Boolean) expression.calculate(),
                "Expression 'A AND B OR C' with C=false should evaluate to FALSE");
    }

    @Test
    public void testOperatorPrecedenceWithParentheses() {
        expression = new LogicExpression("A AND ( B OR C )", false);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        expression.setVariable("C", true);
        // Expected: A AND (B OR C) = true AND (false OR true) = true AND true = true
        assertTrue((Boolean) expression.calculate(), "Expression 'A AND ( B OR C )' should evaluate to TRUE");

        expression.setVariable("C", false);
        // Expected: A AND (B OR C) = true AND (false OR false) = true AND false = false
        assertFalse((Boolean) expression.calculate(),
                "Expression 'A AND ( B OR C )' with C=false should evaluate to FALSE");
    }

    @Test
    public void testLogicalExpressionWithMixedNegationsAndOperators() {
        expression = new LogicExpression("A AND NOT ( B OR NOT C )", false);
        expression.setVariable("A", true);
        expression.setVariable("B", false);
        expression.setVariable("C", false);
        // Evaluate: NOT (B OR NOT C) = NOT (false OR true) = NOT true = false
        // A AND false = true AND false = false
        assertFalse((Boolean) expression.calculate(), "Expression 'A AND NOT ( B OR NOT C )' should evaluate to FALSE");
    }
}
