package com.lazicode.workflow.expressions;

import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Expression_determineLogicExpressionType_NORMAL_Test {

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

        // Create an instance of LogicExpression with any expression, since we're testing a private method
        LogicExpression expr = new LogicExpression("A B AND");

        // Access the private method `determineExpressionType` using reflection
        Method method = Expression.class.getDeclaredMethod("determineExpressionType", String.class, Set.class);
        method.setAccessible(true);

        // Test various cases for infix and postfix expressions
        assertEquals("infix", method.invoke(expr, "(A AND B) OR C", SUPPORTED_OPERATORS), "Expected infix notation.");
        assertEquals("postfix", method.invoke(expr, "A B AND C OR", SUPPORTED_OPERATORS), "Expected postfix notation.");
        assertEquals("unknown", method.invoke(expr, "A AND OR B", SUPPORTED_OPERATORS), "Expected unknown notation.");
        assertEquals("postfix", method.invoke(expr, "A B AND", SUPPORTED_OPERATORS), "Expected postfix notation.");
        assertEquals("infix", method.invoke(expr, "(A AND (B OR C))", SUPPORTED_OPERATORS), "Expected infix notation.");
        assertEquals("unknown", method.invoke(expr, "A B AND (C", SUPPORTED_OPERATORS), "Expected unknown notation due to unbalanced parentheses.");
    }
}
