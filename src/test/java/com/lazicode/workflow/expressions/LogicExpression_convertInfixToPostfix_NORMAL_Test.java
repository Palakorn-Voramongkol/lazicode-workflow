package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.lazicode.workflow.exceptions.InvalidExpression;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


class LogicExpression_convertInfixToPostfix_NORMAL_Test {

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
    void testSimpleANDConversion() {
        try {
            String postfix = invokeConvertInfixToPostfix("A AND B");
            assertEquals("A B AND", postfix, "Expected postfix conversion for 'A AND B' to be 'A B AND'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid infix expression 'A AND B': " + e.getMessage());
        }
    }

    @Test
    void testSimpleORConversion() {
        try {
            String postfix = invokeConvertInfixToPostfix("A OR B");
            assertEquals("A B OR", postfix, "Expected postfix conversion for 'A OR B' to be 'A B OR'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid infix expression 'A OR B': " + e.getMessage());
        }
    }

    @Test
    void testNOTConversion() {
        try {
            String postfix = invokeConvertInfixToPostfix("NOT A");
            assertEquals("A NOT", postfix, "Expected postfix conversion for 'NOT A' to be 'A NOT'.");
        } catch (InvalidExpression e) {
            fail("InvalidExpression should not be thrown for valid infix expression 'NOT A': " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown for infix expression 'NOT A': " + e.getMessage());
        }
    }

    @Test
    void testComplexExpressionWithANDORNOT() {
        try {
            String postfix = invokeConvertInfixToPostfix("(A AND B) OR NOT C");
            assertEquals("A B AND C NOT OR", postfix, "Expected postfix conversion for '(A AND B) OR NOT C' to be 'A B AND C NOT OR'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid infix expression '(A AND B) OR NOT C': " + e.getMessage());
        }
    }

    @Test
    void testNestedComplexExpression() {
        try {
            String postfix = invokeConvertInfixToPostfix("(A AND (B OR C)) AND (D XOR (E NOR F))");
            assertEquals("A B C OR AND D E F NOR XOR AND", postfix, "Expected postfix conversion for '(A AND (B OR C)) AND (D XOR (E NOR F))' to be 'A B C OR AND D E F NOR XOR AND'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid infix expression '(A AND (B OR C)) AND (D XOR (E NOR F))': " + e.getMessage());
        }
    }

    @Test
    void testComplexExpressionWithMultipleOperators() {
        try {
            String postfix = invokeConvertInfixToPostfix("((A NAND B) AND (C OR (NOT D))) OR ((E AND F) XOR G)");
            assertEquals("A B NAND C D NOT OR AND E F AND G XOR OR", postfix,
                    "Expected postfix conversion for '((A NAND B) AND (C OR (NOT D))) OR ((E AND F) XOR G)' to be 'A B NAND C D NOT OR AND E F AND G XOR OR'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid infix expression '((A NAND B) AND (C OR (NOT D))) OR ((E AND F) XOR G)': " + e.getMessage());
        }
    }
}
