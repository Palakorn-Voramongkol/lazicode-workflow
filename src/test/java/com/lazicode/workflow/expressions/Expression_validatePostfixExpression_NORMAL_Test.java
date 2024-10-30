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

class Expression_validatePostfixExpression_NORMAL_Test {

    private LogicExpression expr;
    private static final Set<String> SUPPORTED_OPERATORS = new HashSet<>();

    static {
        SUPPORTED_OPERATORS.add("AND");
        SUPPORTED_OPERATORS.add("OR");
        SUPPORTED_OPERATORS.add("NOT");
        SUPPORTED_OPERATORS.add("NAND");
        SUPPORTED_OPERATORS.add("NOR");
        SUPPORTED_OPERATORS.add("XOR");
        SUPPORTED_OPERATORS.add("XNOR");
    }

    @BeforeEach
    void setUp() throws Exception {
        // Mock LogicExpression to access protected methods
        expr = Mockito.mock(LogicExpression.class, Mockito.CALLS_REAL_METHODS);

        // Set up the expressionString field directly using reflection if needed
        Field expressionStringField = Expression.class.getDeclaredField("expressionString");
        expressionStringField.setAccessible(true);
        expressionStringField.set(expr, "A B AND C OR");
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
        invokeValidatePostfixExpression("A B AND C OR");
    }

    @Test
    void testInvalidOperator() {
        // Test for unsupported operator '$'
        assertThrows(InvalidExpression.class, () -> {
            try {
                invokeValidatePostfixExpression("A B $ C AND");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for unsupported operator '$' in 'A B $ C AND'.");
    }

    @Test
    void testMissingOperand() {
        // Test for missing operand with 'AND' operator
        assertThrows(InvalidExpression.class, () -> {
            try {
                invokeValidatePostfixExpression("A AND");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for missing operand with 'AND' operator in 'A AND'.");
    }

    @Test
    void testExtraOperand() {
        // Test for an extra operand, expecting it to throw an exception for invalid postfix format
        assertThrows(InvalidExpression.class, () -> {
            try {
                invokeValidatePostfixExpression("A B C AND OR D");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for extra operand in 'A B C AND OR D'.");
    }
}
