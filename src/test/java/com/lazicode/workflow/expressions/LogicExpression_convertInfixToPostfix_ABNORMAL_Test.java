package com.lazicode.workflow.expressions;

import com.lazicode.workflow.exceptions.InvalidExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class LogicExpression_convertInfixToPostfix_ABNORMAL_Test {

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

    private String invokeConvertInfixToPostfix(String infix) throws Exception {
        // Use reflection to invoke the protected convertInfixToPostfix method
        Method convertMethod = Expression.class.getDeclaredMethod("convertInfixToPostfix", String.class);
        convertMethod.setAccessible(true);
        return (String) convertMethod.invoke(expr, infix);
    }

    @Test
    void testMissingOperator() {
        assertThrows(InvalidExpression.class, () -> {
            try {
                invokeConvertInfixToPostfix("A B");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for missing operator in 'A B'.");
    }

    @Test
    void testMismatchedParentheses() {
        assertThrows(InvalidExpression.class, () -> {
            try {
                invokeConvertInfixToPostfix("(A AND (B OR C)");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for mismatched parentheses in '(A AND (B OR C)'.");
    }

    @Test
    void testInvalidToken() {
        assertThrows(InvalidExpression.class, () -> {
            try {
                invokeConvertInfixToPostfix("A AND $ B");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for invalid token '$' in 'A AND $ B'.");
    }

    @Test
    void testEmptyExpression() {
        assertThrows(InvalidExpression.class, () -> {
            try {
                invokeConvertInfixToPostfix("");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for empty infix expression.");
    }

    @Test
    void testUnbalancedParenthesesExpression() {
        assertThrows(InvalidExpression.class, () -> {
            try {
                invokeConvertInfixToPostfix("((A OR B) AND C");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for unbalanced parentheses in '((A OR B) AND C'.");
    }
}
