package com.lazicode.workflow.expressions;

import java.util.Stack;

public class MathExpression extends Expression {
    public MathExpression(String expressionString) {
        super(expressionString);
        if (!isValid()) {
            throw new IllegalArgumentException("Invalid math expression provided.");
        }
    }

    @Override
    public void setVariable(String variable, Object value) {
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException("MathExpression can only accept numeric values.");
        }
        setVariableValue(variable, value);
    }

    @Override
    protected Double performCalculation() {
        String expression = getExpressionString();
        Stack<Double> stack = new Stack<>();
        String[] tokens = expression.split(" ");

        for (String token : tokens) {
            switch (token) {
                case "+":
                    stack.push(stack.pop() + stack.pop());
                    break;
                case "-":
                    double b = stack.pop();
                    double a = stack.pop();
                    stack.push(a - b);
                    break;
                case "*":
                    stack.push(stack.pop() * stack.pop());
                    break;
                case "/":
                    double divisor = stack.pop();
                    double dividend = stack.pop();
                    if (divisor == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    stack.push(dividend / divisor);
                    break;
                case "(":
                    stack.push(null);
                    break;
                case ")":
                    double result = stack.pop();
                    while (stack.peek() != null) {
                        result += stack.pop();
                    }
                    stack.pop();
                    stack.push(result);
                    break;
                default:
                    if (token.matches("[A-Z]")) {
                        stack.push(((Number) getVariable(token)).doubleValue());
                    } else if (token.matches("[0-9]+\\.?[0-9]*")) {
                        stack.push(Double.parseDouble(token));
                    } else {
                        throw new IllegalArgumentException("Invalid token: " + token);
                    }
            }
        }
        return stack.pop();
    }

    @Override
    public boolean isValid() {
        int openParentheses = 0;
        String[] tokens = getExpressionString().split(" ");
        for (String token : tokens) {
            if (token.equals("(")) {
                openParentheses++;
            } else if (token.equals(")")) {
                openParentheses--;
                if (openParentheses < 0) {
                    return false;
                }
            } else if (!(token.matches("[0-9+\\-*/().A-Z]"))) { // Fixed the escape for '-'
                return false;
            }
        }
        return openParentheses == 0;
    }
}
