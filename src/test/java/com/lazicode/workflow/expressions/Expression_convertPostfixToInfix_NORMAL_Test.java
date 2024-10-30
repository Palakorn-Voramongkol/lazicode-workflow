package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class Expression_convertPostfixToInfix_NORMAL_Test {

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
    void testSimpleANDConversion() {
        try {
            setPostfixExpression("A B AND");
            String infix = invokeConvertPostfixToInfix("A B AND");
            assertEquals("(A AND B)", infix, "Expected infix conversion for 'A B AND' to be '(A AND B)'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A B AND': " + e.getMessage());
        }
    }

    @Test
    void testSimpleORConversion() {
        try {
            setPostfixExpression("A B OR");
            String infix = invokeConvertPostfixToInfix("A B OR");
            assertEquals("(A OR B)", infix, "Expected infix conversion for 'A B OR' to be '(A OR B)'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A B OR': " + e.getMessage());
        }
    }

    @Test
    void testNOTConversion() {
        try {
            setPostfixExpression("A NOT");
            String infix = invokeConvertPostfixToInfix("A NOT");
            assertEquals("(NOT A)", infix, "Expected infix conversion for 'A NOT' to be '(NOT A)'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A NOT': " + e.getMessage());
        }
    }

    @Test
    void testNOTNOTConversion() {
        try {
            setPostfixExpression("A NOT NOT");
            String infix = invokeConvertPostfixToInfix("A NOT NOT");
            assertEquals("(NOT (NOT A))", infix, "Expected infix conversion for 'A NOT NOT' to be '(NOT (NOT A))'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A NOT NOT': " + e.getMessage());
        }
    }

    @Test
    void testComplexExpressionWithANDORNOT() {
        try {
            setPostfixExpression("A B AND C NOT OR");
            String infix = invokeConvertPostfixToInfix("A B AND C NOT OR");
            assertEquals("((A AND B) OR (NOT C))", infix, "Expected infix conversion for 'A B AND C NOT OR' to be '((A AND B) OR (NOT C))'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A B AND C NOT OR': " + e.getMessage());
        }
    }

    @Test
    void testNestedComplexExpression() {
        try {
            setPostfixExpression("A B C OR AND D E F NOR XOR AND");
            String infix = invokeConvertPostfixToInfix("A B C OR AND D E F NOR XOR AND");
            assertEquals("((A AND (B OR C)) AND (D XOR (E NOR F)))", infix, "Expected infix conversion for 'A B C OR AND D E F NOR XOR AND' to be '((A AND (B OR C)) AND (D XOR (E NOR F)))'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A B C OR AND D E F NOR XOR AND': " + e.getMessage());
        }
    }

    @Test
    void testXNORConversion() {
        try {
            setPostfixExpression("A B XNOR");
            String infix = invokeConvertPostfixToInfix("A B XNOR");
            assertEquals("(A XNOR B)", infix, "Expected infix conversion for 'A B XNOR' to be '(A XNOR B)'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A B XNOR': " + e.getMessage());
        }
    }

    @Test
    void testComplexExpressionWithMultipleOperators() {
        try {
            setPostfixExpression("A B NAND C D NOT OR AND E F AND G XOR OR");
            String infix = invokeConvertPostfixToInfix("A B NAND C D NOT OR AND E F AND G XOR OR");
            assertEquals("(((A NAND B) AND (C OR (NOT D))) OR ((E AND F) XOR G))", infix,
                    "Expected infix conversion for 'A B NAND C D NOT OR AND E F AND G XOR OR' to be '(((A NAND B) AND (C OR (NOT D))) OR ((E AND F) XOR G))'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for valid postfix expression 'A B NAND C D NOT OR AND E F AND G XOR OR': " + e.getMessage());
        }
    }
}
