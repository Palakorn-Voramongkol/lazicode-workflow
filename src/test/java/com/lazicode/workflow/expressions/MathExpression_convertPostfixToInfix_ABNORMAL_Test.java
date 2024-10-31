package com.lazicode.workflow.expressions;

import com.lazicode.workflow.exceptions.InvalidExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class MathExpression_convertPostfixToInfix_ABNORMAL_Test {

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

    private void setPostfixExpression(String expression) throws Exception {
        // Use reflection to set the postfixExpression field in Expression class
        Field postfixField = Expression.class.getDeclaredField("postfixExpression");
        postfixField.setAccessible(true);
        postfixField.set(expr, expression);
    }

    private String invokeConvertPostfixToInfix(String expression) throws Exception {
        // Use reflection to invoke the protected convertPostfixToInfix method
        Method convertMethod = Expression.class.getDeclaredMethod("convertPostfixToInfix", String.class);
        convertMethod.setAccessible(true);
        return (String) convertMethod.invoke(expr, expression);
    }

    @Test
    void testInvalidOperatorSequence() {
        assertThrows(InvalidExpression.class, () -> {
            setPostfixExpression("A + + B");
            try {
                invokeConvertPostfixToInfix("A + + B");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for 'A + + B' in convertPostfixToInfix.");
    }

    @Test
    void testMissingOperator() {
        assertThrows(InvalidExpression.class, () -> {
            setPostfixExpression("A B");
            try {
                invokeConvertPostfixToInfix("A B");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for 'A B' due to missing operator.");
    }

    @Test
    void testExtraOperand() {
        assertThrows(InvalidExpression.class, () -> {
            setPostfixExpression("A B + C");
            try {
                invokeConvertPostfixToInfix("A B + C");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for 'A B + C' due to extra operand.");
    }

    @Test
    void testInvalidCharacter() {
        assertThrows(InvalidExpression.class, () -> {
            setPostfixExpression("A B + $");
            try {
                invokeConvertPostfixToInfix("A B + $");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for 'A B + $' due to invalid character.");
    }

    @Test
    void testNestedOperators() {
        assertThrows(InvalidExpression.class, () -> {
            setPostfixExpression("A + - B");
            try {
                invokeConvertPostfixToInfix("A + - B");
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof InvalidExpression) {
                    throw (InvalidExpression) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected InvalidExpression for 'A + - B' due to nested operators without operands.");
    }

    @Test
    void testSingleOperandOnly() {
        try {
            setPostfixExpression("A");
            String result = invokeConvertPostfixToInfix("A");
            assertEquals("A", result, "Expected infix result to be 'A' for single operand input.");
        } catch (Exception e) {
            fail("Exception should not be thrown for a valid single operand input.");
        }
    }

    @Test
    void testValidBinaryExpression() throws Exception {
        setPostfixExpression("A B +");
        String result = invokeConvertPostfixToInfix("A B +");
        assertEquals("(A + B)", result, "Expected '(A + B)' for valid postfix expression 'A B +'.");
    }


    @Test
    void testValidComplexExpression() throws Exception {
        setPostfixExpression("A B + C *");
        String result = invokeConvertPostfixToInfix("A B + C *");
        assertEquals("((A + B) * C)", result, "Expected '((A + B) * C)' for valid postfix expression 'A B + C *'.");
    }
}
