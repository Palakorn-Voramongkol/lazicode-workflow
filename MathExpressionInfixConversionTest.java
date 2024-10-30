package com.lazicode.workflow.expressions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MathExpressionInfixConversionTest {

    @Test
    void testSimpleAdditionConversion() {
        MathExpression expr = new MathExpression("A B +");
        assertEquals("(A + B)", expr.getInfixExpression(), "Expected infix conversion for 'A B +' to be '(A + B)'.");
    }

    @Test
    void testSimpleSubtractionConversion() {
        MathExpression expr = new MathExpression("A B -");
        assertEquals("(A - B)", expr.getInfixExpression(), "Expected infix conversion for 'A B -' to be '(A - B)'.");
    }

    @Test
    void testMultiplicationConversion() {
        MathExpression expr = new MathExpression("A B *");
        assertEquals("(A * B)", expr.getInfixExpression(), "Expected infix conversion for 'A B *' to be '(A * B)'.");
    }

    @Test
    void testDivisionConversion() {
        MathExpression expr = new MathExpression("A B /");
        assertEquals("(A / B)", expr.getInfixExpression(), "Expected infix conversion for 'A B /' to be '(A / B)'.");
    }

    @Test
    void testModulusConversion() {
        MathExpression expr = new MathExpression("A B %");
        assertEquals("(A % B)", expr.getInfixExpression(), "Expected infix conversion for 'A B %' to be '(A % B)'.");
    }

    @Test
    void testExponentiationConversion() {
        MathExpression expr = new MathExpression("A B ^");
        assertEquals("(A ^ B)", expr.getInfixExpression(), "Expected infix conversion for 'A B ^' to be '(A ^ B)'.");
    }

    @Test
    void testComplexExpressionWithMultipleOperators() {
        // Expression: ((A + B) * (C - D)) / (E ^ F)
        MathExpression expr = new MathExpression("A B + C D - * E F ^ /");
        assertEquals("(((A + B) * (C - D)) / (E ^ F))", expr.getInfixExpression(),
                "Expected infix conversion for 'A B + C D - * E F ^ /' to be '(((A + B) * (C - D)) / (E ^ F))'.");
    }

    @Test
    void testExpressionWithNegation() {
        // Unary negation: -(A + B) * C
        MathExpression expr = new MathExpression("A B + - C *");
        assertEquals("((- (A + B)) * C)", expr.getInfixExpression(), 
                "Expected infix conversion for 'A B + - C *' to be '((- (A + B)) * C)'.");
    }

    @Test
    void testNestedComplexExpression() {
        // Complex nested expression: ((A * (B + C)) - (D % (E / F)))
        MathExpression expr = new MathExpression("A B C + * D E F / % -");
        assertEquals("((A * (B + C)) - (D % (E / F)))", expr.getInfixExpression(),
                "Expected infix conversion for 'A B C + * D E F / % -' to be '((A * (B + C)) - (D % (E / F)))'.");
    }
}
