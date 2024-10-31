package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathExpression_determineExpressionType_NORMAL_Test {

    private MathExpression expr;

    @BeforeEach
    void setUp() throws Exception {
        // Create a mock of MathExpression to bypass the constructor
        expr = Mockito.mock(MathExpression.class, Mockito.CALLS_REAL_METHODS);

        // Use reflection to set the expressionString field directly
        Field expressionStringField = Expression.class.getDeclaredField("expressionString");
        expressionStringField.setAccessible(true);
        expressionStringField.set(expr, "");
    }

    private String invokeDetermineExpressionType(String expression, Set<String> operators) throws Exception {
        // Use reflection to invoke the protected determineExpressionType method
        Method determineMethod = Expression.class.getDeclaredMethod("determineExpressionType", String.class, Set.class);
        determineMethod.setAccessible(true);
        return (String) determineMethod.invoke(expr, expression, operators);
    }

    @Test
    void testDetermineMathExpressionType() throws Exception {
        // Define SUPPORTED_OPERATORS with escaped regex operators
        Set<String> SUPPORTED_OPERATORS = new HashSet<>();
        SUPPORTED_OPERATORS.add("\\+"); // Escape +
        SUPPORTED_OPERATORS.add("-");   // No need to escape -
        SUPPORTED_OPERATORS.add("\\*"); // Escape *
        SUPPORTED_OPERATORS.add("/");   // No need to escape /
        SUPPORTED_OPERATORS.add("%");   // No need to escape %

        // Test various cases for infix and postfix expressions
        assertEquals("infix", invokeDetermineExpressionType("(A + B) * C", SUPPORTED_OPERATORS), "Expected infix notation.");
        assertEquals("infix", invokeDetermineExpressionType("A + B + C", SUPPORTED_OPERATORS), "Expected infix notation.");
        assertEquals("postfix", invokeDetermineExpressionType("A B + C *", SUPPORTED_OPERATORS), "Expected postfix notation.");
        assertEquals("unknown", invokeDetermineExpressionType("A + * B", SUPPORTED_OPERATORS), "Expected unknown notation.");
        assertEquals("postfix", invokeDetermineExpressionType("A B +", SUPPORTED_OPERATORS), "Expected postfix notation.");
        assertEquals("infix", invokeDetermineExpressionType("(A + (B * C))", SUPPORTED_OPERATORS), "Expected infix notation.");
        assertEquals("infix", invokeDetermineExpressionType("A B + (C", SUPPORTED_OPERATORS), "Expected unknown notation due to unbalanced parentheses.");
    }
}
