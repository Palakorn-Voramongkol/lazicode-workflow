package com.lazicode.workflow.expressions.utils;

import com.lazicode.workflow.exceptions.InvalidExpression;

public class ExpressionUtils {

    /**
     * Cleanses multiple spaces in the expression, reducing them to a single space.
     * Throws an InvalidExpression exception if the expression is empty or null.
     *
     * @param expression The original expression string with potential extra spaces
     * @return A string with extra spaces removed, leaving only single spaces
     *         between tokens
     * @throws InvalidExpression if the expression is null or empty
     */
    public static String normalizeSpaces(String expression) throws InvalidExpression {
        if (expression == null || expression.trim().isEmpty()) {
            throw new InvalidExpression("Expression is null or empty.");
        }
        return expression.trim().replaceAll("\\s+", " ");
    }
}
