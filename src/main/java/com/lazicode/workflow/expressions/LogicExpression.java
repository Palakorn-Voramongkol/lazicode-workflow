package com.lazicode.workflow.expressions;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Collections;

import com.lazicode.workflow.exceptions.InvalidExpression;
import com.lazicode.workflow.expressions.evaluators.PostfixLogic;


public class LogicExpression extends Expression {

    private static final Set<String> SUPPORTED_OPERATORS;
    private boolean isShortCircuit = true;

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
    


    // Main constructor with initialization logic
    public LogicExpression(String expressionString) throws InvalidExpression {
        this(expressionString, false); // Default to non-short-circuit evaluation
    }

    public LogicExpression(String expressionString, boolean isShortCircuit) throws InvalidExpression {
        super(expressionString);
        this.isShortCircuit = isShortCircuit;
        initializeExpression(expressionString);
    }


    // Helper method to centralize initialization logic
    private void initializeExpression(String expressionString) throws InvalidExpression {
        String expressionType = determineExpressionType(expressionString, SUPPORTED_OPERATORS);

        if ("unknown".equals(expressionType)) {
            throw new InvalidExpression(
                "Invalid expression type, allow only valid infix or postfix logical expressions.");
        }

        if ("postfix".equals(expressionType)) {
            validatePostfixExpression(expressionString, SUPPORTED_OPERATORS);
            infixExpression = convertPostfixToInfix(expressionString);
            postfixExpression = expressionString;
        } else {
            postfixExpression = convertInfixToPostfix(expressionString);
            validatePostfixExpression(postfixExpression, SUPPORTED_OPERATORS);
            infixExpression = convertPostfixToInfix(postfixExpression); // Make the infixExpression more beautiful and correct parenthesis
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
        return SUPPORTED_OPERATORS.contains(token);
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

    /**
     * Determines the type of an operator: binary or none.
     *
     * @param operator The operator to evaluate.
     * @return "binary" if the operator is binary; "none" otherwise.
     */
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

    /**
     * Determines if an operator is left-associative. For MathExpression,
     * all operators are left-associative.
     *
     * @param operator The operator to evaluate.
     * @return true if the operator is left-associative; false otherwise.
     */
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

    /**
     * Indicates whether the MathExpression is valid.
     *
     * @return true if the expression is valid; false otherwise.
     */
    @Override
    public boolean isValid() {
        // Since the constructor validates the expression, return true if the object is created successfully
        return true;
    }

    @Override
    public String toString() {
        return "LogicExpression{" +
                "expressionString='" + getExpressionString() + '\'' +
                ", infixExpression='" + infixExpression + '\'' +
                ", postExpression='" + postfixExpression + '\'' +
                ", variables=" + getVariables() + '\'' +
                ", variableValues=" + getVariableValues() + '\'' +
                ", shortCircuit=" + this.isShortCircuit + '\'' +
                ", output=" + getOutput() +
                '}';
    }


    @Override
    protected Object performCalculation() {
        // Cast variableValues to HashMap<String, Boolean>
        @SuppressWarnings("unchecked")
        HashMap<String, Boolean> booleanValues = (HashMap<String, Boolean>) (HashMap<?, ?>) getVariableValues();
        Boolean result;
        // Call the evalShortCircuit function
        if (this.isShortCircuit) {
            result = PostfixLogic.evalShortCircuit(this.postfixExpression, booleanValues);
        } else {
            result = PostfixLogic.eval(this.postfixExpression, booleanValues);
        }
        return result;
    }
}
