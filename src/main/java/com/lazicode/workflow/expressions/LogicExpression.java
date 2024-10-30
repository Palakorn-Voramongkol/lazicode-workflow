package com.lazicode.workflow.expressions;


import java.util.HashSet;
import java.util.Set;

public class LogicExpression extends Expression {


    private static final Set<String> SUPPORTED_OPERATORS = new HashSet<>();

    static {
        SUPPORTED_OPERATORS.add("AND");
        SUPPORTED_OPERATORS.add("OR");
        SUPPORTED_OPERATORS.add("NOT");
        SUPPORTED_OPERATORS.add("NAND");
        SUPPORTED_OPERATORS.add("NOR");
        SUPPORTED_OPERATORS.add("XOR");
        SUPPORTED_OPERATORS.add("XNOR");
    }

    public LogicExpression(String expressionString) {
        super(expressionString);
        expressionString = normalizeSpaces(expressionString); // Cleanse spaces
        String expressionType = determineExpressionType(expressionString, SUPPORTED_OPERATORS);

        if (expressionType.equals("unknown")) {
            throw new IllegalArgumentException(
                                "Invalid expression type, allow only valid infix or postfix logical expressions.");
        }
        if (expressionType.equals("postfix")) {
            validatePostfixExpression(expressionString, SUPPORTED_OPERATORS); // Validate before proceeding
            infixExpression = convertPostfixToInfix(expressionString); // Convert and store the infix expression
            postfixExpression = expressionString; // Store the original postfix expression
        }    
        else {
            postfixExpression = convertInfixToPostfix(expressionString); // Convert and store the infix expression
            validatePostfixExpression(postfixExpression, SUPPORTED_OPERATORS); // Validate before proceeding
            infixExpression = convertPostfixToInfix(postfixExpression); // Convert and store the infix expression beautifully
        }
        
    }



    @Override
    protected boolean isOperand(String token) {
        // Assuming operands are variables represented by letters or strings not
        // matching operators
        return !isOperator(token) && !token.equals("(") && !token.equals(")");
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
    protected Object performCalculation() {
        // Placeholder for actual calculation logic
        return null;
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
}
