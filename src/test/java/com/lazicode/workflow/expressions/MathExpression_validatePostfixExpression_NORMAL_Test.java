package com.lazicode.workflow.expressions;

import com.lazicode.workflow.exceptions.InvalidExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MathExpression_validatePostfixExpression_NORMAL_Test {

    private MathExpression expr;
    private static final Set<String> SUPPORTED_OPERATORS = new HashSet<>();

    static {
        SUPPORTED_OPERATORS.add("+");
        SUPPORTED_OPERATORS.add("-");
        SUPPORTED_OPERATORS.add("*");
        SUPPORTED_OPERATORS.add("/");
        SUPPORTED_OPERATORS.add("%");
    }

    @BeforeEach
    void setUp() throws Exception {
        // Mock MathExpression to access protected methods
        expr = Mockito.mock(MathExpression.class, Mockito.CALLS_REAL_METHODS);

        // Set up the expressionString field directly using reflection if needed
        Field expressionStringField = Expression.class.getDeclaredField("expressionString");
        expressionStringField.setAccessible(true);
        expressionStringField.set(expr, "A B + C *");
    }

    private void invokeValidatePostfixExpression(String expression) throws Exception {
        // Invoke the protected validatePostfixExpression method using reflection
        Method validateMethod = Expression.class.getDeclaredMethod("validatePostfixExpression", String.class, Set.class);
        validateMethod.setAccessible(true);
        validateMethod.invoke(expr, expression, SUPPORTED_OPERATORS);
    }

    @Test
    void testValidExpression() throws Exception {
        // Validate that a properly formatted postfix expression does not throw an error
        invokeValidatePostfixExpression("A B + C *");
    }

    @Test
    void testInvalidOperator() {
        // Test for unsupported operator '#'
        assertThrows(InvalidExpression.class, () -> {
            try {
                invokeValidatePostfixExpression("A B # C +");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for unsupported operator '#' in 'A B # C +'.");
    }

    @Test
    void testMissingOperand() {
        // Test for missing operand with '+' operator
        assertThrows(InvalidExpression.class, () -> {
            try {
                invokeValidatePostfixExpression("A +");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for missing operand with '+' operator in 'A +'.");
    }

    @Test
    void testExtraOperand() {
        // Test for an extra operand, expecting it to throw an exception for invalid postfix format
        assertThrows(InvalidExpression.class, () -> {
            try {
                invokeValidatePostfixExpression("A B C + * D");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for extra operand in 'A B C + * D'.");
    }
}
