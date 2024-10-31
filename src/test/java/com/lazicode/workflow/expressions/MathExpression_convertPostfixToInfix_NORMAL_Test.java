package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class MathExpression_convertPostfixToInfix_NORMAL_Test {

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
    void testSimpleAdditionConversion() {
        try {
            setPostfixExpression("A B +");
            String infix = invokeConvertPostfixToInfix("A B +");
            assertEquals("(A + B)", infix, "Expected infix conversion for 'A B +' to be '(A + B)'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A B +': " + e.getMessage());
        }
    }

    @Test
    void testSimpleSubtractionConversion() {
        try {
            setPostfixExpression("A B -");
            String infix = invokeConvertPostfixToInfix("A B -");
            assertEquals("(A - B)", infix, "Expected infix conversion for 'A B -' to be '(A - B)'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A B -': " + e.getMessage());
        }
    }

    @Test
    void testMultiplicationConversion() {
        try {
            setPostfixExpression("A B *");
            String infix = invokeConvertPostfixToInfix("A B *");
            assertEquals("(A * B)", infix, "Expected infix conversion for 'A B *' to be '(A * B)'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A B *': " + e.getMessage());
        }
    }

    @Test
    void testDivisionConversion() {
        try {
            setPostfixExpression("A B /");
            String infix = invokeConvertPostfixToInfix("A B /");
            assertEquals("(A / B)", infix, "Expected infix conversion for 'A B /' to be '(A / B)'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A B /': " + e.getMessage());
        }
    }

    @Test
    void testModulusConversion() {
        try {
            setPostfixExpression("A B %");
            String infix = invokeConvertPostfixToInfix("A B %");
            assertEquals("(A % B)", infix, "Expected infix conversion for 'A B %' to be '(A % B)'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A B %': " + e.getMessage());
        }
    }

    @Test
    void testComplexExpressionWithMultipleOperators() {
        try {
            setPostfixExpression("A B * C D / + E %");
            String infix = invokeConvertPostfixToInfix("A B * C D / + E %");
            assertEquals("(((A * B) + (C / D)) % E)", infix,
                    "Expected infix conversion for 'A B * C D / + E %' to be '(((A * B) + (C / D)) % E)'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A B * C D / + E %': " + e.getMessage());
        }
    }
}
