package com.lazicode.workflow.expressions;

import com.lazicode.workflow.exceptions.InvalidExpression;
import com.lazicode.workflow.expressions.utils.ExpressionUtils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionUtils_normalizeSpaces_NORMAL_Test {


    @Test
    void testNormalizeSpaces_withValidExpression() throws InvalidExpression {
        String expression = "a   +   b   -    c";
        String expected = "a + b - c";
        
        assertEquals(expected, ExpressionUtils.normalizeSpaces(expression));
    }

    @Test
    void testNormalizeSpaces_withSingleSpaceExpression() throws InvalidExpression {
        String expression = "a + b - c";
        String expected = "a + b - c";
        
        assertEquals(expected, ExpressionUtils.normalizeSpaces(expression));
    }

    @Test
    void testNormalizeSpaces_withLeadingAndTrailingSpaces() throws InvalidExpression {
        String expression = "   a + b - c   ";
        String expected = "a + b - c";
        
        assertEquals(expected, ExpressionUtils.normalizeSpaces(expression));
    }

    @Test
    void testNormalizeSpaces_withEmptyExpression() {
        String expression = "";
        
        Exception exception = assertThrows(InvalidExpression.class, () -> {
            ExpressionUtils.normalizeSpaces(expression);
        });

        assertEquals("Expression is null or empty.", exception.getMessage());
    }

    @Test
    void testNormalizeSpaces_withNullExpression() {
        String expression = null;
        
        Exception exception = assertThrows(InvalidExpression.class, () -> {
            ExpressionUtils.normalizeSpaces(expression);
        });

        assertEquals("Expression is null or empty.", exception.getMessage());
    }
}
