package com.lazicode.workflow.expressions;

import com.lazicode.workflow.exceptions.InvalidExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class MathExpression_convertInfixToPostfix_NORMAL_Test {

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

    private String invokeConvertInfixToPostfix(String infix) throws Exception {
        // Use reflection to invoke the protected convertInfixToPostfix method
        Method convertMethod = Expression.class.getDeclaredMethod("convertInfixToPostfix", String.class);
        convertMethod.setAccessible(true);
        return (String) convertMethod.invoke(expr, infix);
    }

    @Test
    void testSimpleAdditionConversion() {
        try {
            String postfix = invokeConvertInfixToPostfix("A + B");
            assertEquals("A B +", postfix, "Expected postfix conversion for 'A + B' to be 'A B +'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid infix expression 'A + B': " + e.getMessage());
        }
    }

    @Test
    void testSimpleMultiplicationConversion() {
        try {
            String postfix = invokeConvertInfixToPostfix("A * B");
            assertEquals("A B *", postfix, "Expected postfix conversion for 'A * B' to be 'A B *'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid infix expression 'A * B': " + e.getMessage());
        }
    }

    @Test
    void testComplexExpressionWithAdditionAndMultiplication() {
        try {
            String postfix = invokeConvertInfixToPostfix("(A + B) * C");
            assertEquals("A B + C *", postfix, "Expected postfix conversion for '(A + B) * C' to be 'A B + C *'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid infix expression '(A + B) * C': " + e.getMessage());
        }
    }

    @Test
    void testNestedComplexExpression() {
        try {
            String postfix = invokeConvertInfixToPostfix("(A + (B * C)) - (D / E)");
            assertEquals("A B C * + D E / -", postfix, "Expected postfix conversion for '(A + (B * C)) - (D / E)' to be 'A B C * + D E / -'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid infix expression '(A + (B * C)) - (D / E)': " + e.getMessage());
        }
    }

    @Test
    void testComplexExpressionWithMultipleOperators() {
        try {
            String postfix = invokeConvertInfixToPostfix("((A + B) * (C - D)) / E");
            assertEquals("A B + C D - * E /", postfix,
                    "Expected postfix conversion for '((A + B) * (C - D)) / E' to be 'A B + C D - * E /'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid infix expression '((A + B) * (C - D)) / E': " + e.getMessage());
        }
    }
}
