package com.lazicode.workflow.expressions;

import java.util.*;
import org.json.JSONObject;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Represents a logical expression that can be evaluated based on provided variables.
 * Supports operators: AND, OR, NOT, NAND, NOR, XOR, XNOR.
 */
public class LogicExpression extends Expression {
    private boolean shortCircuit;

    // Initialize the Logger
    private static final Logger LOGGER = Logger.getLogger(LogicExpression.class.getName());

    // Define operator precedence
    private static final Map<String, Integer> PRECEDENCE_MAP = new HashMap<>();
    static {
        PRECEDENCE_MAP.put("NOT", 4);
        PRECEDENCE_MAP.put("AND", 3);
        PRECEDENCE_MAP.put("NAND", 3);
        PRECEDENCE_MAP.put("OR", 2);
        PRECEDENCE_MAP.put("NOR", 2);
        PRECEDENCE_MAP.put("XOR", 1);
        PRECEDENCE_MAP.put("XNOR", 1);
    }

    // Define operator associativity
    private static final Set<String> RIGHT_ASSOCIATIVE = new HashSet<>(Arrays.asList("NOT"));

    /**
     * Constructs a LogicExpression with the given expression string and short-circuit flag.
     *
     * @param expressionString The logical expression in infix notation.
     * @param shortCircuit     Enables or disables short-circuit evaluation.
     * @throws IllegalArgumentException If the expression is invalid.
     */
    public LogicExpression(String expressionString, boolean shortCircuit) {
        super(expressionString);
        this.shortCircuit = shortCircuit;
        LOGGER.info("Initializing LogicExpression with expression: " + expressionString + " and shortCircuit: " + shortCircuit);
        if (!isValid()) {
            LOGGER.severe("Invalid logic expression: " + expressionString);
            throw new IllegalArgumentException("Invalid logic expression provided.");
        }
        LOGGER.info("LogicExpression initialized successfully.");
    }

    /**
     * Sets the value of a variable used in the expression.
     *
     * @param variable The variable name (single uppercase letter).
     * @param value    The Boolean value to assign to the variable.
     * @throws IllegalArgumentException If the value is not a Boolean.
     */
    @Override
    public void setVariable(String variable, Object value) {
        if (!(value instanceof Boolean)) {
            LOGGER.warning("Attempted to set variable '" + variable + "' with non-Boolean value: " + value);
            throw new IllegalArgumentException("LogicExpression can only accept Boolean values.");
        }
        LOGGER.info("Setting variable '" + variable + "' to value: " + value);
        setVariableValue(variable.toUpperCase(), value);
        setCachedResult(null); // Invalidate cache when variables change
        LOGGER.fine("Cached result invalidated after setting variable.");
    }

    /**
     * Calculates the result of the logical expression.
     *
     * @return The Boolean result of the expression.
     */
    @Override
    public Object calculate() {
        if (getCachedResult() != null) {
            LOGGER.info("Returning cached result: " + getCachedResult());
            return getCachedResult();
        }

        try {
            LOGGER.info("Performing calculation for expression: " + getExpressionString());
            Boolean result = performCalculation();
            setCachedResult(result);
            LOGGER.info("Calculation successful. Result: " + result);
            return result;
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Error during calculation: " + e.getMessage(), e);
            setCachedResult(null);
            throw e;
        }
    }

    /**
     * Performs the calculation by converting infix to postfix and evaluating it.
     *
     * @return The Boolean result of the expression.
     */
    @Override
    protected Boolean performCalculation() {
        String expression = getExpressionString();
        List<String> tokens = tokenize(expression);
        LOGGER.fine("Tokenized expression: " + tokens);
        List<String> postfix = infixToPostfix(tokens);
        LOGGER.fine("Postfix expression: " + postfix);
        return evaluatePostfix(postfix);
    }

    /**
     * Validates the logical expression for correct syntax.
     *
     * @return True if the expression is valid; otherwise, false.
     */
    @Override
    public boolean isValid() {
        LOGGER.fine("Validating expression: " + getExpressionString());
        int openParentheses = 0;
        String[] tokens = getExpressionString().split("\\s+");
        String previousToken = "";

        for (String token : tokens) {
            if (token.equals("(")) {
                openParentheses++;
            } else if (token.equals(")")) {
                openParentheses--;
                if (openParentheses < 0) {
                    LOGGER.warning("Mismatched parentheses detected.");
                    return false; // More closing parentheses than opening
                }
            } else if (!isOperator(token) && !token.equalsIgnoreCase("TRUE") && !token.equalsIgnoreCase("FALSE") && !isVariable(token)) {
                LOGGER.warning("Invalid token detected: " + token);
                return false; // Invalid token
            }

            // Additional syntax checks
            if (isOperator(token)) {
                if (previousToken.isEmpty() || previousToken.equals("(") || isOperator(previousToken)) {
                    if (!isUnaryOperator(token)) {
                        LOGGER.warning("Invalid operator placement: " + token + " after " + previousToken);
                        return false; // Binary operator cannot start the expression or follow another operator
                    }
                }
            } else if (token.equalsIgnoreCase("TRUE") || token.equalsIgnoreCase("FALSE") || isVariable(token)) {
                if (!previousToken.isEmpty() && (previousToken.equals(")") || isOperandUsedForValidation(previousToken))) {
                    LOGGER.warning("Invalid operand placement: " + token + " after " + previousToken);
                    return false; // Operand cannot follow a closing parenthesis or another operand without an operator
                }
            }

            previousToken = token;
        }

        if (openParentheses != 0) {
            LOGGER.warning("Mismatched parentheses. Open parentheses count: " + openParentheses);
            return false;
        }

        LOGGER.info("Expression is valid.");
        return true;
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
            if (isOperand(token)) {
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
                    throw new IllegalArgumentException("Mismatched parentheses");
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
                throw new IllegalArgumentException("Mismatched parentheses");
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
     * @return The Boolean result of the expression.
     * @throws IllegalArgumentException If the expression is invalid.
     */
    private Boolean evaluatePostfix(List<String> postfix) {
        LOGGER.fine("Evaluating postfix expression: " + postfix);
        Stack<Boolean> stack = new Stack<>();

        for (String token : postfix) {
            LOGGER.fine("Evaluating token: " + token);
            if (token.equalsIgnoreCase("TRUE")) {
                stack.push(true);
                LOGGER.fine("Pushed TRUE onto stack.");
            } else if (token.equalsIgnoreCase("FALSE")) {
                stack.push(false);
                LOGGER.fine("Pushed FALSE onto stack.");
            } else if (isVariable(token)) {
                Boolean varValue = (Boolean) getVariable(token);
                if (varValue == null) {
                    LOGGER.severe("Undefined variable encountered during evaluation: " + token);
                    throw new IllegalArgumentException("Undefined variable: " + token);
                }
                stack.push(varValue);
                LOGGER.fine("Pushed variable '" + token + "' value: " + varValue + " onto stack.");
            } else if (isOperator(token)) {
                LOGGER.fine("Processing operator: " + token);
                switch (token.toUpperCase()) {
                    case "NOT":
                        if (stack.isEmpty()) {
                            LOGGER.severe("Insufficient operands for NOT operator.");
                            throw new IllegalArgumentException("Insufficient operands for NOT");
                        }
                        boolean notOperand = stack.pop();
                        boolean notResult = !notOperand;
                        stack.push(notResult);
                        LOGGER.fine("Applied NOT: !" + notOperand + " = " + notResult);
                        break;
                    case "AND":
                        if (stack.size() < 2) {
                            LOGGER.severe("Insufficient operands for AND operator.");
                            throw new IllegalArgumentException("Insufficient operands for AND");
                        }
                        boolean andRight = stack.pop();
                        boolean andLeft = stack.pop();
                        LOGGER.fine("AND operands: " + andLeft + " AND " + andRight);
                        if (shortCircuit && !andLeft) {
                            LOGGER.fine("Short-circuit AND detected. Result: false");
                            stack.push(false); // Short-circuit: AND is false if left operand is false
                        } else {
                            boolean andResult = andLeft && andRight;
                            stack.push(andResult);
                            LOGGER.fine("AND result: " + andResult);
                        }
                        break;
                    case "OR":
                        if (stack.size() < 2) {
                            LOGGER.severe("Insufficient operands for OR operator.");
                            throw new IllegalArgumentException("Insufficient operands for OR");
                        }
                        boolean orRight = stack.pop();
                        boolean orLeft = stack.pop();
                        LOGGER.fine("OR operands: " + orLeft + " OR " + orRight);
                        if (shortCircuit && orLeft) {
                            LOGGER.fine("Short-circuit OR detected. Result: true");
                            stack.push(true); // Short-circuit: OR is true if left operand is true
                        } else {
                            boolean orResult = orLeft || orRight;
                            stack.push(orResult);
                            LOGGER.fine("OR result: " + orResult);
                        }
                        break;
                    case "NAND":
                        if (stack.size() < 2) {
                            LOGGER.severe("Insufficient operands for NAND operator.");
                            throw new IllegalArgumentException("Insufficient operands for NAND");
                        }
                        boolean nandRight = stack.pop();
                        boolean nandLeft = stack.pop();
                        boolean nandResult = !(nandLeft && nandRight);
                        stack.push(nandResult);
                        LOGGER.fine("NAND result: !(" + nandLeft + " AND " + nandRight + ") = " + nandResult);
                        break;
                    case "NOR":
                        if (stack.size() < 2) {
                            LOGGER.severe("Insufficient operands for NOR operator.");
                            throw new IllegalArgumentException("Insufficient operands for NOR");
                        }
                        boolean norRight = stack.pop();
                        boolean norLeft = stack.pop();
                        boolean norResult = !(norLeft || norRight);
                        stack.push(norResult);
                        LOGGER.fine("NOR result: !(" + norLeft + " OR " + norRight + ") = " + norResult);
                        break;
                    case "XOR":
                        if (stack.size() < 2) {
                            LOGGER.severe("Insufficient operands for XOR operator.");
                            throw new IllegalArgumentException("Insufficient operands for XOR");
                        }
                        boolean xorRight = stack.pop();
                        boolean xorLeft = stack.pop();
                        boolean xorResult = xorLeft ^ xorRight;
                        stack.push(xorResult);
                        LOGGER.fine("XOR result: " + xorLeft + " XOR " + xorRight + " = " + xorResult);
                        break;
                    case "XNOR":
                        if (stack.size() < 2) {
                            LOGGER.severe("Insufficient operands for XNOR operator.");
                            throw new IllegalArgumentException("Insufficient operands for XNOR");
                        }
                        boolean xnorRight = stack.pop();
                        boolean xnorLeft = stack.pop();
                        boolean xnorResult = !(xnorLeft ^ xnorRight);
                        stack.push(xnorResult);
                        LOGGER.fine("XNOR result: !(" + xnorLeft + " XOR " + xnorRight + ") = " + xnorResult);
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

        boolean finalResult = stack.pop();
        LOGGER.info("Final evaluation result: " + finalResult);
        return finalResult;
    }

    /**
     * Checks if a token is an operator.
     *
     * @param token The token to check.
     * @return True if the token is an operator; otherwise, false.
     */
    private boolean isOperator(String token) {
        return PRECEDENCE_MAP.containsKey(token.toUpperCase());
    }

    /**
     * Checks if a token is a valid operand.
     *
     * @param token The token to check.
     * @return True if the token is an operand; otherwise, false.
     */
    private boolean isOperand(String token) {
        return token.equalsIgnoreCase("TRUE") ||
               token.equalsIgnoreCase("FALSE") ||
               isVariable(token);
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
        return PRECEDENCE_MAP.getOrDefault(operator.toUpperCase(), 0);
    }

    /**
     * Determines if an operator is left-associative.
     *
     * @param operator The operator.
     * @return True if left-associative; otherwise, false.
     */
    private boolean isLeftAssociative(String operator) {
        return !RIGHT_ASSOCIATIVE.contains(operator.toUpperCase());
    }

    /**
     * Determines if an operator is unary.
     *
     * @param operator The operator.
     * @return True if the operator is unary; otherwise, false.
     */
    private boolean isUnaryOperator(String operator) {
        return operator.equalsIgnoreCase("NOT");
    }

    /**
     * Determines if a token is an operand used for validation.
     *
     * @param token The token to check.
     * @return True if the token is an operand; otherwise, false.
     */
    private boolean isOperandUsedForValidation(String token) {
        return token.equalsIgnoreCase("TRUE") ||
               token.equalsIgnoreCase("FALSE") ||
               isVariable(token);
    }

    // Optional: Implement toJSON and toString methods as needed
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("expression", getExpressionString());
        json.put("shortCircuit", shortCircuit);
        json.put("variables", getVariables());
        json.put("result", getCachedResult());
        return json;
    }

    @Override
    public String toString() {
        return "LogicExpression{" +
                "expression='" + getExpressionString() + '\'' +
                ", shortCircuit=" + shortCircuit +
                ", variables=" + getVariables() +
                ", result=" + getCachedResult() +
                '}';
    }
}
