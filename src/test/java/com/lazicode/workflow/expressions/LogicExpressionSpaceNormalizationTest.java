package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogicExpressionSpaceNormalizationTest {

    @Test
    void testSimpleExpressionWithExtraSpaces() {
        // Given expression with extra spaces
        LogicExpression expr = new LogicExpression("A     B     AND");

        // Expected single-space normalized expression in infix form
        assertEquals("(A AND B)", expr.getInfixExpression(),
                "Expected infix conversion for 'A     B     AND' to be '(A AND B)' after normalization.");
    }

    @Test
    void testComplexExpressionWithMultipleSpaces() {
        // Given complex expression with inconsistent spacing
        LogicExpression expr = new LogicExpression("A     B  AND     C   NOT   OR");

        // Expected infix expression after normalization and conversion
        assertEquals("((A AND B) OR (NOT C))", expr.getInfixExpression(),
                "Expected infix conversion for 'A     B  AND     C   NOT   OR' to be '((A AND B) OR (NOT C))' after normalization.");
    }

    @Test
    void testNestedExpressionWithExtraSpaces() {
        // Given nested expression with extra spaces
        LogicExpression expr = new LogicExpression("A   B    C OR  AND     D     E     F NOR XOR AND");

        // Expected infix expression after normalization
        assertEquals("((A AND (B OR C)) AND (D XOR (E NOR F)))", expr.getInfixExpression(),
                "Expected infix conversion for 'A   B    C OR  AND     D     E     F NOR XOR AND' to be '((A AND (B OR C)) AND (D XOR (E NOR F)))' after normalization.");
    }

    @Test
    void testExpressionWithLeadingAndTrailingSpaces() {
        // Given expression with leading and trailing spaces
        LogicExpression expr = new LogicExpression("     A B AND C NOT OR     ");

        // Expected infix expression after normalization
        assertEquals("((A AND B) OR (NOT C))", expr.getInfixExpression(),
                "Expected infix conversion for '     A B AND C NOT OR     ' to be '((A AND B) OR (NOT C))' after normalization.");
    }

    @Test
    void testExpressionWithOnlyExtraSpacesBetweenOperators() {
        // Given expression with extra spaces only between operators and operands
        LogicExpression expr = new LogicExpression("A    B   AND    C  OR");

        // Expected infix expression after normalization
        assertEquals("((A AND B) OR C)", expr.getInfixExpression(),
                "Expected infix conversion for 'A    B   AND    C  OR' to be '((A AND B) OR C)' after normalization.");
    }
}
