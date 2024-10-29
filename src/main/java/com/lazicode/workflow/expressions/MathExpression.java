package com.lazicode.workflow.expressions;

import java.util.*;
import org.json.JSONObject;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Represents a mathematical expression that can be evaluated based on provided variables.
 * Supports operators: +, -, *, /.
 */
public class MathExpression extends Expression {
    private static final Logger LOGGER = Logger.getLogger(MathExpression.class.getName());

    // Define operator precedence
    private static final Map<String, Integer> PRECEDENCE_MAP = new HashMap<>();
    static {
        PRECEDENCE_MAP.put("+", 1);
        PRECEDENCE_MAP.put("-", 1);
        PRECEDENCE_MAP.put("*", 2);
        PRECEDENCE_MAP.put("/", 2);
    }

    /**
     * Constructs a MathExpression with the given expression string.
     *
     * @param expressionString The mathematical expression in infix notation.
     * @throws IllegalArgumentException If the expression is invalid.
     */
    public MathExpression(String expressionString) {
        super(expressionString);
        LOGGER.info("Initializing MathExpression with expression: " + expressionString);
        try {
            isValid(); // This will throw IllegalArgumentException if invalid
        } catch (IllegalArgumentException e) {
            LOGGER.severe("Invalid math expression: " + e.getMessage());
            throw e; // Rethrow to propagate the specific error message
        }
        LOGGER.info("MathExpression initialized successfully.");
    }

    /**
     * Sets the value of a variable used in the expression.
     *
     * @param variable The variable name (single uppercase letter).
     * @param value    The numeric value to assign to the variable.
     * @throws IllegalArgumentException If the value is not a Number.
     */
    @Override
    public void setVariable(String variable, Object value) {
        if (!(value instanceof Number)) {
            LOGGER.warning("Attempted to set variable '" + variable + "' with non-numeric value: " + value);
            throw new IllegalArgumentException("MathExpression can only accept numeric values.");
        }
        LOGGER.info("Setting variable '" + variable + "' to value: " + value);
        setVariableValue(variable.toUpperCase(), value);
        setCachedResult(null); // Invalidate cache when variables change
        LOGGER.fine("Cached result invalidated after setting variable.");
    }

    /**
     * Calculates the result of the mathematical expression.
     *
     * @return The Double result of the expression.
     */
    @Override
    public Object calculate() {
        if (getCachedResult() != null) {
            LOGGER.info("Returning cached result: " + getCachedResult());
            return getCachedResult();
        }

        try {
            LOGGER.info("Performing calculation for expression: " + getExpressionString());
            Double result = performCalculation();
            setCachedResult(result);
            LOGGER.info("Calculation successful. Result: " + result);
            return result;
        } catch (IllegalArgumentException | ArithmeticException e) {
            LOGGER.log(Level.SEVERE, "Error during calculation: " + e.getMessage(), e);
            setCachedResult(null);
            throw e;
        }
    }

    /**
     * Performs the calculation by converting infix to postfix and evaluating it.
     *
     * @return The Double result of the expression.
     */
    @Override
    protected Double performCalculation() {
        String expression = getExpressionString();
        List<String> tokens = tokenize(expression);
        LOGGER.fine("Tokenized expression: " + tokens);
        List<String> postfix = infixToPostfix(tokens);
        LOGGER.fine("Postfix expression: " + postfix);
        return evaluatePostfix(postfix);
    }

    /**
     * Validates the mathematical expression for correct syntax.
     *
     * @return True if the expression is valid; otherwise, throws an exception.
     * @throws IllegalArgumentException If the expression is invalid.
     */
    @Override
    public boolean isValid() {
        LOGGER.fine("Validating expression: " + getExpressionString());
        String trimmedExpr = getExpressionString().trim();
        if (trimmedExpr.isEmpty()) {
            LOGGER.warning("Empty expression provided.");
            throw new IllegalArgumentException("Invalid expression: Stack should contain a single result after evaluation.");
        }

        int openParentheses = 0;
        List<String> tokens = tokenize(trimmedExpr);
        String previousToken = "";

        for (String token : tokens) {
            if (token.equals("(")) {
                openParentheses++;
                // Check for empty parentheses like ()
                if (previousToken.equals("(")) {
                    LOGGER.warning("Empty parentheses detected.");
                    throw new IllegalArgumentException("Empty parentheses detected.");
                }
            } else if (token.equals(")")) {
                openParentheses--;
                if (openParentheses < 0) {
                    LOGGER.warning("Mismatched parentheses detected.");
                    throw new IllegalArgumentException("Mismatched parentheses.");
                }
                // Check for empty parentheses like ()
                if (previousToken.equals("(")) {
                    LOGGER.warning("Empty parentheses detected.");
                    throw new IllegalArgumentException("Empty parentheses detected.");
                }
            } else if (!isOperator(token) && !isNumber(token) && !isVariable(token)) {
                LOGGER.warning("Invalid token detected: " + token);
                throw new IllegalArgumentException("Invalid token detected: " + token);
            }

            // Additional syntax checks
            if (isOperator(token)) {
                if (previousToken.isEmpty() || previousToken.equals("(") || isOperator(previousToken)) {
                    if (!isUnaryOperator(token)) {
                        LOGGER.warning("Invalid operator placement: " + token + " after " + previousToken);
                        throw new IllegalArgumentException("Invalid operator placement: " + token + " after " + previousToken);
                    }
                }
            } else if (isNumber(token) || isVariable(token)) {
                if (!previousToken.isEmpty() && (previousToken.equals(")") || isOperandUsedForValidation(previousToken))) {
                    LOGGER.warning("Invalid operand placement: " + token + " after " + previousToken);
                    throw new IllegalArgumentException("Invalid operand placement: " + token + " after " + previousToken);
                }
            }

            previousToken = token;
        }

        if (openParentheses != 0) {
            LOGGER.warning("Mismatched parentheses. Open parentheses count: " + openParentheses);
            throw new IllegalArgumentException("Mismatched parentheses. Open parentheses count: " + openParentheses);
        }

        // Check if the last token is an operator
        String lastToken = tokens.get(tokens.size() - 1);
        if (isOperator(lastToken)) {
            LOGGER.warning("Invalid operator placement: " + lastToken + " at the end of the expression.");
            throw new IllegalArgumentException("Invalid expression: Stack should contain a single result after evaluation.");
        }

        LOGGER.info("Expression is valid.");
        return true; // If no exception is thrown, the expression is valid
    }

    /**
     * Tokenizes the expression string into a list of tokens.
     *
     * @param expr The expression string.
     * @return A list of tokens.
     */
    private List<String> tokenize(String expr) {
        return Arrays.asList(expr.trim().split("\\s+"));
    }

    /**
     * Converts an infix expression to postfix using the Shunting Yard Algorithm.
     *
     * @param tokens The list of infix tokens.
     * @return The list of postfix tokens.
     * @throws IllegalArgumentException If there are mismatched parentheses or invalid tokens.
     */
    private List<String> infixToPostfix(List<String> tokens) {
        LOGGER.fine("Converting infix to postfix.");
        List<String> output = new ArrayList<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token) || isVariable(token)) {
                output.add(token);
                LOGGER.fine("Added operand to output: " + token);
            } else if (isOperator(token)) {
                LOGGER.fine("Processing operator: " + token);
                while (!operatorStack.isEmpty() && isOperator(operatorStack.peek())) {
                    String topOp = operatorStack.peek();
                    if ((isLeftAssociative(token) && precedence(token) <= precedence(topOp)) ||
                        (!isLeftAssociative(token) && precedence(token) < precedence(topOp))) {
                        output.add(operatorStack.pop());
                        LOGGER.fine("Popped operator from stack to output: " + topOp);
                    } else {
                        break;
                    }
                }
                operatorStack.push(token);
                LOGGER.fine("Pushed operator to stack: " + token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
                LOGGER.fine("Pushed '(' to stack.");
            } else if (token.equals(")")) {
                LOGGER.fine("Encountered ')', popping operators to output.");
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    output.add(operatorStack.pop());
                    LOGGER.fine("Popped operator from stack to output.");
                }
                if (operatorStack.isEmpty() || !operatorStack.peek().equals("(")) {
                    LOGGER.severe("Mismatched parentheses detected during postfix conversion.");
                    throw new IllegalArgumentException("Mismatched parentheses.");
                }
                operatorStack.pop(); // Remove '(' from stack
                LOGGER.fine("Popped '(' from stack.");
            } else {
                LOGGER.severe("Invalid token encountered during postfix conversion: " + token);
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }

        while (!operatorStack.isEmpty()) {
            String op = operatorStack.pop();
            if (op.equals("(") || op.equals(")")) {
                LOGGER.severe("Mismatched parentheses detected at end of postfix conversion.");
                throw new IllegalArgumentException("Mismatched parentheses.");
            }
            output.add(op);
            LOGGER.fine("Popped operator from stack to output at end: " + op);
        }

        LOGGER.fine("Postfix expression: " + output);
        return output;
    }

    /**
     * Evaluates a postfix expression.
     *
     * @param postfix The list of postfix tokens.
     * @return The Double result of the expression.
     * @throws IllegalArgumentException If the expression is invalid.
     * @throws ArithmeticException      If a mathematical error occurs (e.g., division by zero).
     */
    private Double evaluatePostfix(List<String> postfix) {
        LOGGER.fine("Evaluating postfix expression: " + postfix);
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            LOGGER.fine("Evaluating token: " + token);
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
                LOGGER.fine("Pushed number onto stack: " + token);
            } else if (isVariable(token)) {
                Object varValue = getVariable(token);
                if (varValue == null) {
                    LOGGER.severe("Undefined variable encountered during evaluation: " + token);
                    throw new IllegalArgumentException("Variable " + token + " has not been set.");
                }
                if (!(varValue instanceof Number)) {
                    LOGGER.severe("Variable '" + token + "' is not a number.");
                    throw new IllegalArgumentException("Variable '" + token + "' is not a number.");
                }
                stack.push(((Number) varValue).doubleValue());
                LOGGER.fine("Pushed variable '" + token + "' value: " + varValue + " onto stack.");
            } else if (isOperator(token)) {
                LOGGER.fine("Processing operator: " + token);
                switch (token) {
                    case "+":
                        if (stack.size() < 2) {
                            LOGGER.severe("Insufficient operands for '+' operator.");
                            throw new IllegalArgumentException("Insufficient operands for '+'");
                        }
                        double addRight = stack.pop();
                        double addLeft = stack.pop();
                        double addResult = addLeft + addRight;
                        stack.push(addResult);
                        LOGGER.fine("Performed addition: " + addLeft + " + " + addRight + " = " + addResult);
                        break;
                    case "-":
                        if (stack.size() < 2) {
                            LOGGER.severe("Insufficient operands for '-' operator.");
                            throw new IllegalArgumentException("Insufficient operands for '-'");
                        }
                        double subRight = stack.pop();
                        double subLeft = stack.pop();
                        double subResult = subLeft - subRight;
                        stack.push(subResult);
                        LOGGER.fine("Performed subtraction: " + subLeft + " - " + subRight + " = " + subResult);
                        break;
                    case "*":
                        if (stack.size() < 2) {
                            LOGGER.severe("Insufficient operands for '*' operator.");
                            throw new IllegalArgumentException("Insufficient operands for '*'");
                        }
                        double mulRight = stack.pop();
                        double mulLeft = stack.pop();
                        double mulResult = mulLeft * mulRight;
                        stack.push(mulResult);
                        LOGGER.fine("Performed multiplication: " + mulLeft + " * " + mulRight + " = " + mulResult);
                        break;
                    case "/":
                        if (stack.size() < 2) {
                            LOGGER.severe("Insufficient operands for '/' operator.");
                            throw new IllegalArgumentException("Insufficient operands for '/'");
                        }
                        double divRight = stack.pop();
                        double divLeft = stack.pop();
                        if (divRight == 0) {
                            LOGGER.severe("Division by zero.");
                            throw new ArithmeticException("Division by zero.");
                        }
                        double divResult = divLeft / divRight;
                        stack.push(divResult);
                        LOGGER.fine("Performed division: " + divLeft + " / " + divRight + " = " + divResult);
                        break;
                    default:
                        LOGGER.severe("Invalid operator encountered during evaluation: " + token);
                        throw new IllegalArgumentException("Invalid operator: " + token);
                }
            } else {
                LOGGER.severe("Invalid token encountered during evaluation: " + token);
                throw new IllegalArgumentException("Invalid token in postfix expression: " + token);
            }
        }

        if (stack.size() != 1) {
            LOGGER.severe("Invalid expression: Stack should contain a single result after evaluation.");
            throw new IllegalArgumentException("Invalid expression: Stack should contain a single result after evaluation.");
        }

        double finalResult = stack.pop();
        LOGGER.info("Final evaluation result: " + finalResult);
        return finalResult;
    }

    /**
     * Checks if a token is a valid operator.
     *
     * @param token The token to check.
     * @return True if the token is an operator; otherwise, false.
     */
    private boolean isOperator(String token) {
        return PRECEDENCE_MAP.containsKey(token);
    }

    /**
     * Checks if a token is a valid number.
     *
     * @param token The token to check.
     * @return True if the token is a number; otherwise, false.
     */
    private boolean isNumber(String token) {
        return token.matches("^\\d+(\\.\\d+)?$");
    }

    /**
     * Checks if a token is a valid variable (single uppercase letter).
     *
     * @param token The token to check.
     * @return True if the token is a variable; otherwise, false.
     */
    private boolean isVariable(String token) {
        return token.matches("^[A-Z]$");
    }

    /**
     * Retrieves the precedence of an operator.
     *
     * @param operator The operator.
     * @return The precedence value.
     */
    private int precedence(String operator) {
        return PRECEDENCE_MAP.getOrDefault(operator, 0);
    }

    /**
     * Determines if an operator is left-associative.
     *
     * @param operator The operator.
     * @return True if left-associative; otherwise, false.
     */
    private boolean isLeftAssociative(String operator) {
        // For basic arithmetic operators, all are left-associative
        return true;
    }

    /**
     * Determines if an operator is unary.
     *
     * @param operator The operator.
     * @return True if the operator is unary; otherwise, false.
     */
    private boolean isUnaryOperator(String operator) {
        // In basic arithmetic, operators are binary. Extend if unary operators are needed.
        return false;
    }

    /**
     * Determines if a token is an operand used for validation.
     *
     * @param token The token to check.
     * @return True if the token is an operand; otherwise, false.
     */
    private boolean isOperandUsedForValidation(String token) {
        return isNumber(token) || isVariable(token);
    }

    // Optional: Implement toJSON and toString methods as needed
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("expression", getExpressionString());
        json.put("variables", getVariables());
        json.put("result", getCachedResult());
        return json;
    }

    @Override
    public String toString() {
        return "MathExpression{" +
                "expression='" + getExpressionString() + '\'' +
                ", variables=" + getVariables() +
                ", result=" + getCachedResult() +
                '}';
    }
}
