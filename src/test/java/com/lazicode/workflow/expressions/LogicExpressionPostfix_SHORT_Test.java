package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import com.lazicode.workflow.exceptions.InvalidExpression;

import static org.junit.jupiter.api.Assertions.*;


class LogicExpressionPostfix_SHORT_Test {

    @Test
    void testSimpleAndExpression() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("A B AND");
        exp.setVariable("A", true);
        exp.setVariable("B", true);
        assertEquals(true, exp.getOutput(), "Failed for A=true, B=true with AND");

        exp.setVariable("A", true);
        exp.setVariable("B", false);
        assertEquals(false, exp.getOutput(), "Failed for A=true, B=false with AND");

        exp.setVariable("A", false);
        exp.setVariable("B", true);
        assertEquals(false, exp.getOutput(), "Failed for A=false, B=true with AND");
    }

    @Test
    void testSimpleOrExpression() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("A B OR");
        exp.setVariable("A", false);
        exp.setVariable("B", true);
        assertEquals(true, exp.getOutput(), "Failed for A=false, B=true with OR");

        exp.setVariable("A", false);
        exp.setVariable("B", false);
        assertEquals(false, exp.getOutput(), "Failed for A=false, B=false with OR");
    }

    @Test
    void testNotExpression() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("A NOT");
        exp.setVariable("A", true);
        assertEquals(false, exp.getOutput(), "Failed for A=true with NOT");

        exp.setVariable("A", false);
        assertEquals(true, exp.getOutput(), "Failed for A=false with NOT");
    }

    @Test
    void testComplexExpression() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("A B AND C OR");
        exp.setVariable("A", true);
        exp.setVariable("B", true);
        exp.setVariable("C", false);
        assertEquals(true, exp.getOutput(), "Failed for (A AND B) OR C");

        exp.setVariable("A", false);
        exp.setVariable("B", true);
        exp.setVariable("C", true);
        assertEquals(true, exp.getOutput(), "Failed for (A AND B) OR C");
    }

    @Test
    void testWithNANDOperator() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("A B NAND");
        exp.setVariable("A", true);
        exp.setVariable("B", true);
        assertEquals(false, exp.getOutput(), "Failed for A=true, B=true with NAND");

        exp.setVariable("A", true);
        exp.setVariable("B", false);
        assertEquals(true, exp.getOutput(), "Failed for A=true, B=false with NAND");
    }

    @Test
    void testWithXOROperator() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("A B XOR");
        exp.setVariable("A", true);
        exp.setVariable("B", false);
        assertEquals(true, exp.getOutput(), "Failed for A=true, B=false with XOR");

        exp.setVariable("A", true);
        exp.setVariable("B", true);
        assertEquals(false, exp.getOutput(), "Failed for A=true, B=true with XOR");
    }

    @Test
    void testWithXNOROperator() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("A B XNOR");
        exp.setVariable("A", true);
        exp.setVariable("B", true);
        assertEquals(true, exp.getOutput(), "Failed for A=true, B=true with XNOR");

        exp.setVariable("A", true);
        exp.setVariable("B", false);
        assertEquals(false, exp.getOutput(), "Failed for A=true, B=false with XNOR");
    }
}
