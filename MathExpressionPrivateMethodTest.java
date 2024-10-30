package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

class NormalExpressionTest {

    /**
     * Helper method to set variable values for testing.
     */
    private Expression setupExpression(String expression, Map<String, Object> variables,
            Class<? extends Expression> expressionClass) {
        Expression expr;
        if (expressionClass.equals(MathExpression.class)) {
            expr = new MathExpression(expression);
        } else if (expressionClass.equals(LogicExpression.class)) {
            expr = new LogicExpression(expression);
        } else {
            throw new IllegalArgumentException("Unsupported Expression subclass.");
        }

        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            expr.setVariable(entry.getKey(), entry.getValue());
        }
        return expr;
    }

    @Test
    void testDetermineExpressionTypeInfixMath() {
        // Expression: (A + B) * C
        String infixExpression = "(A + B) * C";
        Expression expr = setupExpression(infixExpression, Map.of(), MathExpression.class);

        assertEquals("infix", expr.determineExpressionType(infixExpression, MathExpression.SUPPORTED_OPERATORS),
                "Expression should be identified as 'infix' for MathExpression.");
    }

    @Test
    void testDetermineExpressionTypePostfixMath() {
        // Expression: A B + C *
        String postfixExpression = "A B + C *";
        Expression expr = setupExpression(postfixExpression, Map.of(), MathExpression.class);

        assertEquals("postfix", expr.determineExpressionType(postfixExpression, MathExpression.SUPPORTED_OPERATORS),
                "Expression should be identified as 'postfix' for MathExpression.");
    }

    @Test
    void testDetermineExpressionTypeUnknownMath() {
        // Invalid expression: A + B *
        String invalidExpression = "A + B *";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            setupExpression(invalidExpression, Map.of(), MathExpression.class);
        });
        assertTrue(exception.getMessage().contains("Invalid expression type"),
                "Invalid expression should throw an exception for MathExpression.");
    }

    @Test
    void testCalculateInvalidMathExpression() {
        // Expression: A + B *
        String invalidExpression = "A + B *";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Expression expr = setupExpression(invalidExpression, Map.of(), MathExpression.class);
            expr.calculate();
        });
        assertTrue(exception.getMessage().contains("Invalid postfix expression format"),
                "Invalid postfix expression should throw an exception for MathExpression.");
    }


}
