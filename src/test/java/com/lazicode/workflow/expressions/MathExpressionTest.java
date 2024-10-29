package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MathExpression class.
 */
public class MathExpressionTest {
    private MathExpression expression;

    @BeforeEach
    public void setUp() {
        // Initialize with a valid expression
        expression = new MathExpression("A + B * C");
        expression.setVariable("A", 2);
        expression.setVariable("B", 3);
        expression.setVariable("C", 4);
    }

    // -------------------- Valid Expression Tests --------------------

    @Test
    public void testSimpleAddition() {
        expression = new MathExpression("A + B");
        expression.setVariable("A", 5);
        expression.setVariable("B", 3);
        assertEquals(8.0, (Double) expression.calculate(),
            "Expression 'A + B' should evaluate to 8.0");
    }

    @Test
    public void testSimpleSubtraction() {
        expression = new MathExpression("A - B");
        expression.setVariable("A", 10);
        expression.setVariable("B", 4);
        assertEquals(6.0, (Double) expression.calculate(),
            "Expression 'A - B' should evaluate to 6.0");
    }

    @Test
    public void testSimpleMultiplication() {
        expression = new MathExpression("A * B");
        expression.setVariable("A", 7);
        expression.setVariable("B", 6);
        assertEquals(42.0, (Double) expression.calculate(),
            "Expression 'A * B' should evaluate to 42.0");
    }

    @Test
    public void testSimpleDivision() {
        expression = new MathExpression("A / B");
        expression.setVariable("A", 20);
        expression.setVariable("B", 5);
        assertEquals(4.0, (Double) expression.calculate(),
            "Expression 'A / B' should evaluate to 4.0");
    }

    @Test
    public void testMultipleOperatorsWithPrecedence() {
        expression = new MathExpression("A + B * C - D / E");
        expression.setVariable("A", 2);
        expression.setVariable("B", 3);
        expression.setVariable("C", 4);
        expression.setVariable("D", 10);
        expression.setVariable("E", 5);
        // Expected: 2 + (3*4) - (10/5) = 2 + 12 - 2 = 12
        assertEquals(12.0, (Double) expression.calculate(),
            "Expression 'A + B * C - D / E' should evaluate to 12.0");
    }

    @Test
    public void testExpressionWithParentheses() {
        expression = new MathExpression("( A + B ) * ( C - D )");
        expression.setVariable("A", 5);
        expression.setVariable("B", 3);
        expression.setVariable("C", 10);
        expression.setVariable("D", 2);
        // Expected: (5+3)*(10-2) = 8*8 = 64
        assertEquals(64.0, (Double) expression.calculate(),
            "Expression '( A + B ) * ( C - D )' should evaluate to 64.0");
    }

    @Test
    public void testComplexNestedExpression() {
        expression = new MathExpression("A + ( B * ( C + D ) ) - E / F");
        expression.setVariable("A", 1);
        expression.setVariable("B", 2);
        expression.setVariable("C", 3);
        expression.setVariable("D", 4);
        expression.setVariable("E", 10);
        expression.setVariable("F", 2);
        // Expected: 1 + (2*(3+4)) - (10/2) = 1 + 14 - 5 = 10
        assertEquals(10.0, (Double) expression.calculate(),
            "Expression 'A + ( B * ( C + D ) ) - E / F' should evaluate to 10.0");
    }

    @Test
    public void testExpressionWithExcessiveWhitespace() {
        expression = new MathExpression("  A    +    B *   C  ");
        expression.setVariable("A", 2);
        expression.setVariable("B", 3);
        expression.setVariable("C", 4);
        assertEquals(14.0, (Double) expression.calculate(),
            "Expression '  A    +    B *   C  ' should evaluate to 14.0");
    }

    // -------------------- Invalid Expression Tests --------------------

    @Test
    public void testInvalidToken() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("A + B @");
        });
        assertEquals("Invalid token detected: @", exception.getMessage(),
            "Exception message should indicate invalid token.");
    }

    @Test
    public void testMismatchedParentheses() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("( A + B ) * ( C - D ");
        });
        assertEquals("Mismatched parentheses. Open parentheses count: 1", exception.getMessage(),
            "Exception message should indicate mismatched parentheses with count.");
    }

    @Test
    public void testInvalidOperatorPlacement() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("A + * B");
        });
        assertEquals("Invalid operator placement: * after +", exception.getMessage(),
            "Exception message should indicate invalid operator placement.");
    }

    @Test
    public void testInvalidOperatorAtStart() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("* A + B");
        });
        assertEquals("Invalid operator placement: * after ", exception.getMessage(),
            "Exception message should indicate invalid operator placement.");
    }

    @Test
    public void testDivisionByZero() {
        expression = new MathExpression("A / B");
        expression.setVariable("A", 10);
        expression.setVariable("B", 0);
        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
            expression.calculate();
        });
        assertEquals("Division by zero.", exception.getMessage(),
            "Exception message should indicate division by zero.");
    }

    @Test
    public void testUndefinedVariable() {
        MathExpression expr = new MathExpression("A + B");
        expr.setVariable("A", 5);
        // B is not set
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            expr.calculate();
        });
        assertEquals("Variable B has not been set.", exception.getMessage(),
            "Exception message should indicate undefined variable.");
    }

    @Test
    public void testSetInvalidVariableValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            expression.setVariable("A", "five"); // Should throw an exception
        });
        assertEquals("MathExpression can only accept numeric values.", exception.getMessage(),
            "Exception message should indicate invalid variable value.");
    }

    @Test
    public void testInvalidExpressionWithMultipleIssues() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("A + * ( B - )");
        });
        assertTrue(exception.getMessage().contains("Invalid operator placement") ||
                   exception.getMessage().contains("Mismatched parentheses") ||
                   exception.getMessage().contains("Empty parentheses"),
            "Exception message should indicate one of the invalid expression issues.");
    }

    @Test
    public void testEmptyExpression() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("");
        });
        assertEquals("Invalid expression: Stack should contain a single result after evaluation.", exception.getMessage(),
            "Exception message should indicate invalid expression.");
    }

    @Test
    public void testEmptyParentheses() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("A + ( ) + B");
        });
        assertEquals("Empty parentheses detected.", exception.getMessage(),
            "Exception message should indicate invalid operand placement with empty parentheses.");
    }

    @Test
    public void testConsecutiveOperators() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("A + + B");
        });
        assertEquals("Invalid operator placement: + after +", exception.getMessage(),
            "Exception message should indicate invalid operator placement with consecutive operators.");
    }

    @Test
    public void testTrailingOperator() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("A + B -");
        });
        assertEquals("Invalid expression: Stack should contain a single result after evaluation.", exception.getMessage(),
            "Exception message should indicate invalid expression.");
    }

    @Test
    public void testLeadingOperator() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("+ A + B");
        });
        assertEquals("Invalid operator placement: + after ", exception.getMessage(),
            "Exception message should indicate invalid operator placement with leading operator.");
    }

    @Test
    public void testNestedEmptyParentheses() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("A + ( ( ) ) + B");
        });
        assertTrue(exception.getMessage().contains("Empty parentheses detected"),
            "Exception message should indicate invalid operand placement with nested empty parentheses.");
    }

    @Test
    public void testExpressionWithOnlyOperators() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("+ - * /");
        });
        assertTrue(exception.getMessage().contains("Invalid operator placement"),
            "Exception message should indicate invalid operator placement with only operators.");
    }

    // -------------------- Caching Behavior Tests --------------------

    @Test
    public void testCalculateUsesCachedResult() {
        // First calculation
        Double firstResult = (Double) expression.calculate();
        assertEquals(14.0, firstResult, "First calculation should evaluate to 14.0");

        // Second calculation without variable changes should use cache
        Double secondResult = (Double) expression.calculate();
        assertEquals(14.0, secondResult, "Second calculation should return cached result 14.0");
    }

    @Test
    public void testCalculateCacheInvalidationOnVariableUpdate() {
        // First calculation
        Double firstResult = (Double) expression.calculate();
        assertEquals(14.0, firstResult, "First calculation should evaluate to 14.0");

        // Update a variable
        expression.setVariable("B", 4);

        // Second calculation should re-evaluate
        Double secondResult = (Double) expression.calculate();
        // New expression: 2 + (4 * 4) = 2 + 16 = 18
        assertEquals(18.0, secondResult, "Second calculation after variable update should evaluate to 18.0");
    }

    // -------------------- Additional Edge Case Tests --------------------

    @Test
    public void testExpressionWithUnaryMinus() {
        // Assuming unary minus is not supported as per current implementation
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MathExpression("- A + B");
        });
        assertTrue(exception.getMessage().contains("Invalid operator placement"),
            "Exception message should indicate invalid operator placement for unary minus.");
    }

    @Test
    public void testExpressionWithDecimalNumbers() {
        expression = new MathExpression("A / B + C * D");
        expression.setVariable("A", 5.5);
        expression.setVariable("B", 2.0);
        expression.setVariable("C", 3.3);
        expression.setVariable("D", 4.4);
        // Expected: (5.5 / 2.0) + (3.3 * 4.4) = 2.75 + 14.52 = 17.27
        assertEquals(17.27, (Double) expression.calculate(), 0.0001,
            "Expression 'A / B + C * D' with floating point numbers should evaluate to 17.27");
    }

    @Test
    public void testExpressionWithMultipleDigitNumbers() {
        expression = new MathExpression("A + B * C");
        expression.setVariable("A", 12);
        expression.setVariable("B", 34);
        expression.setVariable("C", 56);
        // Expected: 12 + (34 * 56) = 12 + 1904 = 1916
        assertEquals(1916.0, (Double) expression.calculate(),
            "Expression 'A + B * C' with multiple-digit numbers should evaluate to 1916.0");
    }

}
