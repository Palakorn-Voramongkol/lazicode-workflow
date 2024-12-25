package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MathExpressionTest {

    @Test
    void testSimpleAddition() throws Exception {
        MathExpression exp = new MathExpression("A B +");
        exp.setVariable("A", 5.0);
        exp.setVariable("B", 3.0);
        assertEquals(8.0, exp.getOutput());
    }

    @Test
    void testSubtraction() throws Exception {
        MathExpression exp = new MathExpression("A B -");
        exp.setVariable("A", 10.0);
        exp.setVariable("B", 4.0);
        assertEquals(6.0, exp.getOutput());
    }

    @Test
    void testMultiplication() throws Exception {
        MathExpression exp = new MathExpression("A B *");
        exp.setVariable("A", 2.0);
        exp.setVariable("B", 4.0);
        assertEquals(8.0, exp.getOutput());
    }

    @Test
    void testDivision() throws Exception {
        MathExpression exp = new MathExpression("A B /");
        exp.setVariable("A", 8.0);
        exp.setVariable("B", 2.0);
        assertEquals(4.0, exp.getOutput());
    }

    @Test
    void testModulo() throws Exception {
        MathExpression exp = new MathExpression("A B %");
        exp.setVariable("A", 10.0);
        exp.setVariable("B", 3.0);
        assertEquals(1.0, exp.getOutput());
    }

    @Test
    void testExponentiation() throws Exception {
        MathExpression exp = new MathExpression("A B ^");
        exp.setVariable("A", 2.0);
        exp.setVariable("B", 3.0);
        assertEquals(8.0, exp.getOutput());
    }

    @Test
    void testComplexExpression1() throws Exception {
        MathExpression exp = new MathExpression("A B + C *");
        exp.setVariable("A", 3.0);
        exp.setVariable("B", 2.0);
        exp.setVariable("C", 4.0);
        assertEquals(20.0, exp.getOutput());
    }

    @Test
    void testComplexExpression2() throws Exception {
        MathExpression exp = new MathExpression("A B C * + D /");
        exp.setVariable("A", 5.0);
        exp.setVariable("B", 2.0);
        exp.setVariable("C", 3.0);
        exp.setVariable("D", 5.0);
        assertEquals(2.2, exp.getOutput());
    }

    @Test
    void testDivisionByZero() throws Exception {
        MathExpression exp = new MathExpression("A B /");
        exp.setVariable("A", 10.0);
        exp.setVariable("B", 0.0);
        assertTrue(Double.isNaN((Double) exp.getOutput()));
    }
}
