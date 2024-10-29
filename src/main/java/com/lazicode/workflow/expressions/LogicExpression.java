package com.lazicode.workflow.expressions;

import java.util.*;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.lazicode.workflow.exceptions.InvalidOperatorPlacementException;

/**
 * Represents a logical expression that can be evaluated based on provided
 * variables.
 * Supports operators: AND, OR, NOT, NAND, NOR, XOR, XNOR.
 */
public class LogicExpression extends Expression {
    private static final Logger LOGGER = Logger.getLogger(LogicExpression.class.getName());

    // Define operator precedence and associativity
    private static final Map<String, Integer> PRECEDENCE_MAP = new HashMap<>();
    private static final Map<String, String> ASSOCIATIVITY_MAP = new HashMap<>();

    static {
        PRECEDENCE_MAP.put("NOT", 3);
        PRECEDENCE_MAP.put("AND", 2);
        PRECEDENCE_MAP.put("NAND", 2);
        PRECEDENCE_MAP.put("OR", 1);
        PRECEDENCE_MAP.put("NOR", 1);
        PRECEDENCE_MAP.put("XOR", 1);
        PRECEDENCE_MAP.put("XNOR", 1);

        ASSOCIATIVITY_MAP.put("NOT", "RIGHT");
        ASSOCIATIVITY_MAP.put("AND", "LEFT");
        ASSOCIATIVITY_MAP.put("OR", "LEFT");
        ASSOCIATIVITY_MAP.put("NAND", "LEFT");
        ASSOCIATIVITY_MAP.put("NOR", "LEFT");
        ASSOCIATIVITY_MAP.put("XOR", "LEFT");
        ASSOCIATIVITY_MAP.put("XNOR", "LEFT");
    }

    private boolean shortCircuit;
    private String processedExpressionString; // New variable to store processed expression

    public LogicExpression(String expressionString, boolean shortCircuit) {
        super(expressionString);
        this.shortCircuit = shortCircuit;
        LOGGER.info("Initializing LogicExpression with expression: " + expressionString + " | Short-Circuit: "
                + shortCircuit);

        this.processedExpressionString = applyPrecedenceParentheses(expressionString); // Process the expression with
                                                                                       // precedence-based parentheses
        System.out.print("xxxxxxx"+this.processedExpressionString);

        try {
            isValid(); // Validate the expression upon initialization
        } catch (IllegalArgumentException e) {
            LOGGER.severe("Invalid logic expression: " + e.getMessage());
            throw e; // Rethrow to propagate the specific error message
        }
        LOGGER.info("LogicExpression initialized successfully.");
    }

    private String applyPrecedenceParentheses(String expression) {
        List<String> tokens = tokenize(expression);
        Stack<String> output = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (isOperand(token)) {
                output.push(token);
            } else if (isOperator(token)) {
                while (!operators.isEmpty() &&
                        PRECEDENCE_MAP.getOrDefault(operators.peek(), 0) >= PRECEDENCE_MAP.get(token)) {
                    output.push(")");
                    output.push(operators.pop());
                    output.push("(");
                }
                operators.push(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.peek().equals("(")) {
                    output.push(operators.pop());
                }
                operators.pop(); // Remove the '('
            }
        }
        while (!operators.isEmpty()) {
            output.push(operators.pop());
        }
        
        return String.join(" ", output);
    }

    

    public String getProcessedExpression() {
        return processedExpressionString;
    }

    @Override
    public void setVariable(String variable, Object value) {
        LOGGER.fine("Setting variable: " + variable + " to value: " + value);
        if (!(value instanceof Boolean)) {
            LOGGER.warning("Invalid variable value type for variable '" + variable + "'. Expected Boolean.");
            throw new IllegalArgumentException("LogicExpression can only accept Boolean values.");
        }
        setVariableValue(variable, value);
    }

    @Override
    public boolean isValid() {
        LOGGER.fine("Validating expression: " + getExpressionString());
        String trimmedExpr = getExpressionString().trim();
        if (trimmedExpr.isEmpty()) {
            LOGGER.warning("Empty expression provided.");
            throw new IllegalArgumentException("Invalid expression: Expression cannot be empty.");
        }

        int openParentheses = 0;
        List<String> tokens = tokenize(trimmedExpr);
        String previousToken = "";
        boolean expectingOperand = true;

        for (String token : tokens) {
            if (token.equals("(")) {
                openParentheses++;
                if (!expectingOperand) {
                    LOGGER.warning("Invalid operator placement: '(' after operand.");
                    throw new InvalidOperatorPlacementException("(", previousToken);
                }
                expectingOperand = true;
            } else if (token.equals(")")) {
                openParentheses--;
                if (openParentheses < 0) {
                    LOGGER.warning("Mismatched parentheses detected.");
                    throw new IllegalArgumentException("Mismatched parentheses.");
                }
                if (expectingOperand) {
                    LOGGER.warning("Empty parentheses detected.");
                    throw new IllegalArgumentException("Empty parentheses detected.");
                }
                expectingOperand = false;
            } else if (isOperator(token)) {
                if (isUnaryOperator(token)) {
                    if (!expectingOperand) {
                        LOGGER.warning("Invalid operator placement: " + token + " after " + previousToken + ".");
                        throw new InvalidOperatorPlacementException(token, previousToken);
                    }
                    expectingOperand = true;
                } else {
                    if (expectingOperand) {
                        LOGGER.warning("Invalid operator placement: " + token + " after " + previousToken + ".");
                        throw new InvalidOperatorPlacementException(token, previousToken);
                    }
                    expectingOperand = true;
                }
            } else if (isBooleanLiteral(token) || isVariable(token)) {
                if (!expectingOperand) {
                    LOGGER.warning("Invalid operand placement: " + token + " after " + previousToken + ".");
                    throw new IllegalArgumentException(
                            "Invalid operand placement: " + token + " after " + previousToken + ".");
                }
                expectingOperand = false;
            } else {
                LOGGER.warning("Invalid token detected: " + token);
                throw new IllegalArgumentException("Invalid token detected: " + token);
            }

            previousToken = token;
        }

        if (openParentheses != 0) {
            LOGGER.warning("Mismatched parentheses. Open parentheses count: " + openParentheses);
            throw new IllegalArgumentException("Mismatched parentheses. Open parentheses count: " + openParentheses);
        }

        if (expectingOperand) {
            LOGGER.warning("Expression cannot end with an operator.");
            throw new IllegalArgumentException("Expression cannot end with an operator.");
        }

        LOGGER.info("Expression is valid.");
        return true;
    }

    private List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        for (char ch : expr.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
            } else if (ch == '(' || ch == ')') {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add(String.valueOf(ch));
            } else {
                token.append(ch);
            }
        }
        if (token.length() > 0) {
            tokens.add(token.toString());
        }
        return tokens;
    }

    private boolean isOperator(String token) {
        return PRECEDENCE_MAP.containsKey(token);
    }

    private boolean isUnaryOperator(String token) {
        return token.equals("NOT");
    }

    private boolean isBooleanLiteral(String token) {
        return token.equalsIgnoreCase("TRUE") || token.equalsIgnoreCase("FALSE");
    }

    private boolean isVariable(String token) {
        return token.matches("^[A-Z]$");
    }

    private boolean isOperand(String token) {
        return isBooleanLiteral(token) || isVariable(token);
    }

    @Override
    protected Object performCalculation() {
        List<String> postfix = infixToPostfix(tokenize(processedExpressionString));
        LOGGER.fine("Postfix expression: " + postfix);

        Stack<Boolean> stack = new Stack<>();

        for (String token : postfix) {
            if (isBooleanLiteral(token)) {
                stack.push(Boolean.parseBoolean(token.toUpperCase()));
            } else if (isVariable(token)) {
                Object varValue = getVariable(token);
                if (varValue == null) {
                    LOGGER.severe("Undefined variable encountered during evaluation: " + token);
                    throw new IllegalArgumentException("Variable " + token + " has not been set.");
                }
                stack.push((Boolean) varValue);
            } else if (isOperator(token)) {
                if (isUnaryOperator(token)) {
                    if (stack.isEmpty()) {
                        throw new IllegalArgumentException("Insufficient operands for unary operator: " + token);
                    }
                    stack.push(!stack.pop());
                } else {
                    Boolean right = stack.pop();
                    Boolean left = stack.pop();
                    stack.push(applyOperator(token, left, right));
                }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: Stack should contain a single result after evaluation.");
        }

        return stack.pop();
    }

    /**
     * Converts an infix expression to postfix using the Shunting Yard Algorithm.
     *
     * @param tokens The list of infix tokens.
     * @return The list of postfix tokens.
     * @throws IllegalArgumentException If there are mismatched parentheses or
     *                                  invalid tokens.
     */
    private List<String> infixToPostfix(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            if (isBooleanLiteral(token) || isVariable(token)) {
                output.add(token);
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() && isOperator(operatorStack.peek())) {
                    String topOp = operatorStack.peek();
                    if ((PRECEDENCE_MAP.get(token) < PRECEDENCE_MAP.get(topOp)) ||
                            (PRECEDENCE_MAP.get(token).equals(PRECEDENCE_MAP.get(topOp)) &&
                                    ASSOCIATIVITY_MAP.get(token).equals("LEFT"))) {
                        output.add(operatorStack.pop());
                    } else {
                        break;
                    }
                }
                operatorStack.push(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    output.add(operatorStack.pop());
                }
                if (operatorStack.isEmpty() || !operatorStack.peek().equals("(")) {
                    throw new IllegalArgumentException("Mismatched parentheses.");
                }
                operatorStack.pop(); // Remove '(' from stack
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }

        while (!operatorStack.isEmpty()) {
            String op = operatorStack.pop();
            if (op.equals("(") || op.equals(")")) {
                throw new IllegalArgumentException("Mismatched parentheses.");
            }
            output.add(op);
        }

        return output;
    }

    private Boolean applyOperator(String operator, Boolean left, Boolean right) {
        switch (operator) {
            case "AND":
                return left && right;
            case "OR":
                return left || right;
            case "NAND":
                return !(left && right);
            case "NOR":
                return !(left || right);
            case "XOR":
                return left ^ right;
            case "XNOR":
                return !(left ^ right);
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("shortCircuit", shortCircuit);
        return json;
    }

    @Override
    public String toString() {
        return super.toString() + ", shortCircuit=" + shortCircuit + '}';
    }
}
