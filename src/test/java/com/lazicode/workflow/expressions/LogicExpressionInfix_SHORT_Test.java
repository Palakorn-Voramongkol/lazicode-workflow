package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import com.lazicode.workflow.exceptions.InvalidExpression;

import static org.junit.jupiter.api.Assertions.*;

class LogicExpressionInfix_SHORT_Test {

    @Test
    void testSimpleAndExpression() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("A AND B");
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
        LogicExpression exp = new LogicExpression("A OR B");
        exp.setVariable("A", false);
        exp.setVariable("B", true);
        assertEquals(true, exp.getOutput(), "Failed for A=false, B=true with OR");

        exp.setVariable("A", false);
        exp.setVariable("B", false);
        assertEquals(false, exp.getOutput(), "Failed for A=false, B=false with OR");
    }

    @Test
    void testNotExpression() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("NOT A");
        exp.setVariable("A", true);
        assertEquals(false, exp.getOutput(), "Failed for A=true with NOT");

        exp.setVariable("A", false);
        assertEquals(true, exp.getOutput(), "Failed for A=false with NOT");
    }

    @Test
    void testNotNotExpression() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("NOT NOT A");
        exp.setVariable("A", true);
        assertEquals(false, exp.getOutput(), "Failed for A=true with NOT");

        exp.setVariable("A", false);
        assertEquals(true, exp.getOutput(), "Failed for A=false with NOT");
    }

    @Test
    void testNotNotNotExpression() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("NOT NOT NOT A");
        exp.setVariable("A", true);
        assertEquals(false, exp.getOutput(), "Failed for A=true with NOT");

        exp.setVariable("A", false);
        assertEquals(true, exp.getOutput(), "Failed for A=false with NOT");
    }

    @Test
    void testNotAAndBExpression() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("NOT A AND B");
        exp.setVariable("A", true);
        assertEquals(false, exp.getOutput(), "Failed for A=true with NOT");

        exp.setVariable("A", false);
        assertEquals(true, exp.getOutput(), "Failed for A=false with NOT");
    }

    @Test
    void testComplexExpression() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("(A AND B) OR C");
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
        LogicExpression exp = new LogicExpression("A NAND B");
        exp.setVariable("A", true);
        exp.setVariable("B", true);
        assertEquals(false, exp.getOutput(), "Failed for A=true, B=true with NAND");

        exp.setVariable("A", true);
        exp.setVariable("B", false);
        assertEquals(true, exp.getOutput(), "Failed for A=true, B=false with NAND");
    }

    @Test
    void testWithXOROperator() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("A XOR B");
        exp.setVariable("A", true);
        exp.setVariable("B", false);
        assertEquals(true, exp.getOutput(), "Failed for A=true, B=false with XOR");

        exp.setVariable("A", true);
        exp.setVariable("B", true);
        assertEquals(false, exp.getOutput(), "Failed for A=true, B=true with XOR");
    }

    @Test
    void testWithXNOROperator() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("A XNOR B");
        exp.setVariable("A", true);
        exp.setVariable("B", true);
        assertEquals(true, exp.getOutput(), "Failed for A=true, B=true with XNOR");

        exp.setVariable("A", true);
        exp.setVariable("B", false);
        assertEquals(false, exp.getOutput(), "Failed for A=true, B=false with XNOR");
    }

    @Test
    void testWithMixedOperators() throws InvalidExpression {
        LogicExpression exp = new LogicExpression("(A OR B) AND (C XOR D)");
        exp.setVariable("A", false);
        exp.setVariable("B", true);
        exp.setVariable("C", true);
        exp.setVariable("D", false);
        assertEquals(true, exp.getOutput(), "Failed for (A OR B) AND (C XOR D)");

        exp.setVariable("A", true);
        exp.setVariable("B", false);
        exp.setVariable("C", false);
        exp.setVariable("D", false);
        assertEquals(false, exp.getOutput(), "Failed for (A OR B) AND (C XOR D)");
    }
}
