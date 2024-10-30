package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class LogicExpressionInfixToPostfixTest {

    @Test
    void testSimpleANDOperation() throws Exception {
        String result = invokeConvertInfixToPostfix("A AND B");
        assertEquals("A B AND", result, "Expected postfix conversion of 'A AND B' to be 'A B AND'.");
    }

    @Test
    void testSimpleOROperation() throws Exception {
        String result = invokeConvertInfixToPostfix("A OR B");
        assertEquals("A B OR", result, "Expected postfix conversion of 'A OR B' to be 'A B OR'.");
    }

    @Test
    void testNOTOperation() throws Exception {
        String result = invokeConvertInfixToPostfix("NOT A");
        assertEquals("A NOT", result, "Expected postfix conversion of 'NOT A' to be 'A NOT'.");
    }

    @Test
    void testANDOROperationWithParentheses() throws Exception {
        String result = invokeConvertInfixToPostfix("(A AND B) OR C");
        assertEquals("A B AND C OR", result, "Expected postfix conversion of '(A AND B) OR C' to be 'A B AND C OR'.");
    }

    @Test
    void testNestedParenthesesOperation() throws Exception {
        String result = invokeConvertInfixToPostfix("A AND (B OR (C AND D))");
        assertEquals("A B C D AND OR AND", result, "Expected postfix conversion of 'A AND (B OR (C AND D))' to be 'A B C D AND OR AND'.");
    }

    @Test
    void testComplexExpression() throws Exception {
        String result = invokeConvertInfixToPostfix("(A OR B) AND (C OR (D AND E))");
        assertEquals("A B OR C D E AND OR AND", result, "Expected postfix conversion of '(A OR B) AND (C OR (D AND E))' to be 'A B OR C D E AND OR AND'.");
    }

    @Test
    void testExtraSpacesInExpression() throws Exception {
        String result = invokeConvertInfixToPostfix(" A   AND    B ");
        assertEquals("A B AND", result, "Expected postfix conversion of ' A   AND    B ' to be 'A B AND' after trimming spaces.");
    }

    // Helper method to invoke the private method using reflection
    private String invokeConvertInfixToPostfix(String expression) throws Exception {
        Method method = LogicExpression.class.getDeclaredMethod("convertInfixToPostfix", String.class);
        method.setAccessible(true); // Make the private method accessible
        return (String) method.invoke(null, expression); // Assuming the method is static
    }
}
