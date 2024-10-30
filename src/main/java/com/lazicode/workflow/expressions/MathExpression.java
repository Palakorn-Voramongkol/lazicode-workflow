package com.lazicode.workflow.expressions;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.Stack;

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
        // Exponentiation '^' is excluded as per requirements
        SUPPORTED_OPERATORS = Collections.unmodifiableSet(ops); // Make the set unmodifiable
    }

    /**
     * Constructs a MathExpression object, initializes the expression, and converts
     * between infix and postfix notations as needed.
     *
     * @param expressionString The mathematical expression string in infix or postfix notation.
     * @throws IllegalArgumentException If the expression is invalid or unsupported.
     */
    public MathExpression(String expressionString) {
        super(expressionString);
        expressionString = normalizeSpaces(expressionString); // Cleanse spaces
        String expressionType = determineExpressionType(expressionString, SUPPORTED_OPERATORS);

        if (expressionType.equals("unknown")) {
            throw new IllegalArgumentException(
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
     * Determines if a token is a valid operand. For MathExpression, operands are
     * single uppercase letters (e.g., A, B, C).
     *
     * @param token The token to evaluate.
     * @return true if the token is a single uppercase letter; false otherwise.
     */
    @Override
    protected boolean isOperand(String token) {
        return Pattern.matches("[A-Z]", token);
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
            default:
                return 0; // Unsupported operators
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
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
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
     * Performs the calculation of the mathematical expression. This method
     * evaluates the postfix expression using a stack.
     *
     * @return The result of the calculation as a Double.
     */
    @Override
    protected Object performCalculation() {
        String[] tokens = postfixExpression.split(" ");
        Stack<Double> stack = new Stack<>();

        for (String token : tokens) {
            if (isOperand(token)) {
                // Assuming operands are single uppercase letters, variables should be set with their values before calculation
                Object varValue = getVariable(token);
                if (varValue instanceof Number) {
                    double value = ((Number) varValue).doubleValue();
                    stack.push(value);
                } else {
                    throw new IllegalArgumentException("Variable '" + token + "' is not a numeric value.");
                }
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Insufficient operands for operator '" + token + "'.");
                }
                double b = stack.pop();
                double a = stack.pop();
                double result = applyOperator(a, b, token);
                stack.push(result);
            } else {
                throw new IllegalArgumentException("Invalid token '" + token + "' in expression.");
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression format.");
        }

        return stack.pop();
    }

    /**
     * Applies the given operator to two operands and returns the result.
     *
     * @param a        The first operand.
     * @param b        The second operand.
     * @param operator The operator to apply.
     * @return The result of the operation.
     */
    private double applyOperator(double a, double b, String operator) {
        switch (operator) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0) {
                    throw new IllegalArgumentException("Division by zero.");
                }
                return a / b;
            case "%":
                if (b == 0) {
                    throw new IllegalArgumentException("Modulus by zero.");
                }
                return a % b;
            default:
                throw new IllegalArgumentException("Unsupported operator '" + operator + "'.");
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
}
