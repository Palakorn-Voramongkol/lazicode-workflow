package com.lazicode.workflow.expressions;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

import com.lazicode.workflow.exceptions.InvalidExpression;

import java.util.List;

public class LogicExpression extends Expression {

    private static final Set<String> SUPPORTED_OPERATORS;

    static {
        Set<String> ops = new HashSet<>();
        ops.add("AND");
        ops.add("OR");
        ops.add("NOT");
        ops.add("NAND");
        ops.add("NOR");
        ops.add("XOR");
        ops.add("XNOR");
        SUPPORTED_OPERATORS = Collections.unmodifiableSet(ops); // Make the set unmodifiable
    }
    

    public LogicExpression(String expressionString) throws com.lazicode.workflow.exceptions.InvalidExpression {
        super(expressionString);

        String expressionType = determineExpressionType(expressionString, SUPPORTED_OPERATORS);

        if (expressionType.equals("unknown")) {
            throw new IllegalArgumentException(
                    "Invalid expression type, allow only valid infix or postfix logical expressions.");
        }
        if (expressionType.equals("postfix")) {
            validatePostfixExpression(expressionString, SUPPORTED_OPERATORS); // Validate before proceeding
            infixExpression = convertPostfixToInfix(expressionString); // Convert and store the infix expression
            postfixExpression = expressionString; // Store the original postfix expression
        } else {
            postfixExpression = convertInfixToPostfix(expressionString); // Convert and store the infix expression
            validatePostfixExpression(postfixExpression, SUPPORTED_OPERATORS); // Validate before proceeding
            infixExpression = convertPostfixToInfix(postfixExpression); // Convert and store the infix expression
                                                                        // beautifully
        }

    }

    @Override
    protected boolean isOperand(String token) {
        // Only consider single letters A-Z or a-z as valid operands
        return !isOperator(token)
                && !token.equals("(")
                && !token.equals(")")
                && token.matches("[A-Za-z]");
    }

    @Override
    protected boolean isOperator(String token) {
        switch (token) {
            case "NOT":
            case "NAND":
            case "NOR":
            case "AND":
            case "OR":
            case "XOR":
            case "XNOR":
                return true;
            default:
                return false;
        }
    }

    @Override
    protected int precedence(String operator) {
        switch (operator) {
            case "NOT":
                return 4; // Highest precedence
            case "NAND":
            case "NOR":
                return 3;
            case "AND":
                return 2;
            case "OR":
            case "XOR":
            case "XNOR":
                return 1;
            default:
                return 0;
        }
    }

    @Override
    protected String operatorType(String operator) {
        switch (operator) {
            case "NOT":
                return "unary";
            case "NAND":
            case "NOR":
            case "AND":
            case "OR":
            case "XOR":
            case "XNOR":
                return "binary";
            default:
                return "none";
        }
    }

    @Override
    protected boolean isLeftAssociative(String operator) {
        // 'NOT' is right-associative; others are left-associative
        switch (operator) {
            case "NOT":
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isValid() {
        // Validation logic as before
        return true;
    }

    @Override
    public String toString() {
        return "LogicExpression{" +
                "expressionString='" + getExpressionString() + '\'' +
                ", infixExpression='" + infixExpression + '\'' +
                ", variables=" + getVariables() +
                ", variableValues=" + getVariableValues() +
                ", output=" + getOutput() +
                '}';
    }

    @Override
    protected Object applyOperator(String operator, List<?> operands) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyOperator'");
    }

    @Override
    protected Object performCalculation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'performCalculation'");
    }
}
