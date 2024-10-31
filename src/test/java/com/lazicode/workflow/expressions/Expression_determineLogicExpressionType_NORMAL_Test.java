package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Expression_determineLogicExpressionType_NORMAL_Test {

    private LogicExpression expr;

    @BeforeEach
    void setUp() throws Exception {
        // Create a mock of LogicExpression to bypass the constructor
        expr = Mockito.mock(LogicExpression.class, Mockito.CALLS_REAL_METHODS);

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
    void testDetermineLogicExpressionType() throws Exception {
        // Define SUPPORTED_OPERATORS as a local variable
        Set<String> SUPPORTED_OPERATORS = new HashSet<>();
        SUPPORTED_OPERATORS.add("AND");
        SUPPORTED_OPERATORS.add("OR");
        SUPPORTED_OPERATORS.add("NOT");
        SUPPORTED_OPERATORS.add("NAND");
        SUPPORTED_OPERATORS.add("NOR");
        SUPPORTED_OPERATORS.add("XOR");
        SUPPORTED_OPERATORS.add("XNOR");

        // Test various cases for infix and postfix expressions
        assertEquals("infix", invokeDetermineExpressionType("(A AND B) OR C", SUPPORTED_OPERATORS), "Expected infix notation.");
        assertEquals("postfix", invokeDetermineExpressionType("A B AND C OR", SUPPORTED_OPERATORS), "Expected postfix notation.");
        assertEquals("unknown", invokeDetermineExpressionType("A AND OR B", SUPPORTED_OPERATORS), "Expected unknown notation.");
        assertEquals("postfix", invokeDetermineExpressionType("A B AND", SUPPORTED_OPERATORS), "Expected postfix notation.");
        assertEquals("infix", invokeDetermineExpressionType("(A AND (B OR C))", SUPPORTED_OPERATORS), "Expected infix notation.");
        assertEquals("infix", invokeDetermineExpressionType("A B AND (C", SUPPORTED_OPERATORS), "Expected unknown notation due to unbalanced parentheses.");
    }
}
