package com.lazicode.workflow.expressions;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.regex.Pattern;

import com.lazicode.workflow.exceptions.InvalidExpression;

import java.util.Stack;
import java.util.List;
import java.util.Arrays;

/**
 * Class representing a mathematical expression. It supports infix and postfix notations
 * with operators: +, -, *, /, %.
 */
public class MathExpression extends Expression {

    // Make SUPPORTED_OPERATORS public for access in test classes
    public static final Set<String> SUPPORTED_OPERATORS;

    static {
        Set<String> ops = new HashSet<>();
        ops.add("+");
        ops.add("-");
        ops.add("*");
        ops.add("/");
        ops.add("%");
        ops.add("^");
        // Exponentiation '^' is excluded as per requirements
        SUPPORTED_OPERATORS = Collections.unmodifiableSet(ops); // Make the set unmodifiable
    }

    /**
     * Constructs a MathExpression object, initializes the expression, and converts
     * between infix and postfix notations as needed.
     *
     * @param expressionString The mathematical expression string in infix or postfix notation.
     * @throws com.lazicode.workflow.exceptions.InvalidExpression 
     * @throws InvalidExpression If the expression is invalid or unsupported.
     */
    public MathExpression(String expressionString) throws InvalidExpression {
        super(expressionString);
        String expressionType = determineExpressionType(expressionString, SUPPORTED_OPERATORS);

        if (expressionType.equals("unknown")) {
            throw new InvalidExpression(
                    "Invalid expression type, allow only valid infix or postfix mathematical expressions.");
        }
        if (expressionType.equals("postfix")) {
            validatePostfixExpression(expressionString, SUPPORTED_OPERATORS); // Validate before proceeding
            infixExpression = convertPostfixToInfix(expressionString); // Convert and store the infix expression
            postfixExpression = expressionString; // Store the original postfix expression
        } else { // infix
            postfixExpression = convertInfixToPostfix(expressionString); // Convert infix to postfix
            validatePostfixExpression(postfixExpression, SUPPORTED_OPERATORS); // Validate the postfix expression
            infixExpression = convertPostfixToInfix(postfixExpression); // Convert back to infix for storage
        }
    }



    /**
     * Determines if a token is a supported operator in mathematical expressions.
     *
     * @param token The token to evaluate.
     * @return true if the token is a supported operator; false otherwise.
     */
    @Override
    protected boolean isOperator(String token) {
        return SUPPORTED_OPERATORS.contains(token);
    }

    /**
     * Retrieves the precedence level of a given operator.
     *
     * @param operator The operator to evaluate.
     * @return An integer representing the precedence level.
     */
    @Override
    protected int precedence(String operator) {
        switch (operator) {
            case "*":
            case "/":
            case "%":
                return 3; // Higher precedence
            case "+":
            case "-":
                return 2; // Lower precedence
            case "^":
                return 4;
            default:
                return 0; // Unsupported operators
        }
    }

    /**
     * Determines if a token is a valid operand. For MathExpression, operands are
     * single uppercase letters (e.g., A, B, C).
     *
     * @param token The token to evaluate.
     * @return true if the token is a single uppercase letter; false otherwise.
     */
    @Override
    protected boolean isOperand(String token) {
        // Only consider single letters A-Z or a-z as valid operands
        return !isOperator(token)
                && !token.equals("(")
                && !token.equals(")")
                && token.matches("[A-Za-z]");
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
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
            case "^":
                return "binary";
            default:
                return "none"; // Indicates unsupported or invalid operator
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
        // All supported operators are left-associative
        return true;
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
