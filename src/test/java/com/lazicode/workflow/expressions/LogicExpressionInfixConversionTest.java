package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogicExpressionInfixConversionTest {

    @Test
    void testSimpleANDConversion() {
        LogicExpression expr = new LogicExpression("A B AND");
        assertEquals("(A AND B)", expr.getInfixExpression(), "Expected infix conversion for 'A B AND' to be '(A AND B)'.");
    }

    @Test
    void testSimpleORConversion() {
        LogicExpression expr = new LogicExpression("A B OR");
        assertEquals("(A OR B)", expr.getInfixExpression(), "Expected infix conversion for 'A B OR' to be '(A OR B)'.");
    }

    @Test
    void testNOTConversion() {
        LogicExpression expr = new LogicExpression("A NOT");
        assertEquals("(NOT A)", expr.getInfixExpression(), "Expected infix conversion for 'A NOT' to be '(NOT A)'.");
    }

    @Test
    void testNOTNOTConversion() {
        LogicExpression expr = new LogicExpression("A NOT NOT");
        assertEquals("(NOT (NOT A))", expr.getInfixExpression(), "Expected infix conversion for 'A NOT NOT' to be '(NOT (NOT A))'.");
    }

    @Test
    void testComplexExpressionWithANDORNOT() {
        // Original expression in postfix: ((A AND B) OR (NOT C))
        LogicExpression expr = new LogicExpression("A B AND C NOT OR");
        assertEquals("((A AND B) OR (NOT C))", expr.getInfixExpression(), "Expected infix conversion for 'A B AND C NOT OR' to be '((A AND B) OR (NOT C))'.");
    }

    @Test
    void testNestedComplexExpression() {
        // Complex nested expression: ((A AND (B OR C)) AND (D XOR (E NOR F)))
        LogicExpression expr = new LogicExpression("A B C OR AND D E F NOR XOR AND");
        assertEquals("((A AND (B OR C)) AND (D XOR (E NOR F)))", expr.getInfixExpression(), "Expected infix conversion for 'A B C OR AND D E F NOR XOR AND' to be '((A AND (B OR C)) AND (D XOR (E NOR F)))'.");
    }

    @Test
    void testXNORConversion() {
        LogicExpression expr = new LogicExpression("A B XNOR");
        assertEquals("(A XNOR B)", expr.getInfixExpression(), "Expected infix conversion for 'A B XNOR' to be '(A XNOR B)'.");
    }

    @Test
    void testComplexExpressionWithMultipleOperators() {
        // Expression: ((A NAND B) AND (C OR (NOT D))) OR ((E AND F) XOR G)
        LogicExpression expr = new LogicExpression("A B NAND C D NOT OR AND E F AND G XOR OR");
        assertEquals("(((A NAND B) AND (C OR (NOT D))) OR ((E AND F) XOR G))", expr.getInfixExpression(),
                "Expected infix conversion for 'A B NAND C D NOT OR AND E F AND G XOR OR' to be '(((A NAND B) AND (C OR (NOT D))) OR ((E AND F) XOR G))'.");
    }
}
