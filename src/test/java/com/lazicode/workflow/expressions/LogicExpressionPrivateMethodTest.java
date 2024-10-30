package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

class LogicExpressionPrivateMethodTest {

    @Test
    void testDetermineExpressionType() throws Exception {
        // Create an instance of LogicExpression with any expression, since we're testing a private method
        LogicExpression expr = new LogicExpression("A B AND");

        // Access the private method `determineExpressionType` using reflection
        Method method = LogicExpression.class.getDeclaredMethod("determineExpressionType", String.class);
        method.setAccessible(true);

        // Test various cases for infix and postfix expressions
        assertEquals("infix", method.invoke(expr, "(A AND B) OR C"), "Expected infix notation.");
        assertEquals("postfix", method.invoke(expr, "A B AND C OR"), "Expected postfix notation.");
        assertEquals("unknown", method.invoke(expr, "A AND OR B"), "Expected unknown notation.");
        assertEquals("postfix", method.invoke(expr, "A B AND"), "Expected postfix notation.");
        assertEquals("infix", method.invoke(expr, "(A AND (B OR C))"), "Expected infix notation.");
        assertEquals("unknown", method.invoke(expr, "A B AND (C"), "Expected unknown notation due to unbalanced parentheses.");
    }
}
