
package com.lazicode.workflow.expressions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;

import org.json.JSONObject;

import com.lazicode.workflow.interfaces.JSONPersistable;

public abstract  class Expression implements JSONPersistable {
    private String expressionString;
    private Set<String> variables;
    private Map<String, Object> variableValues;
    private Object output;
    protected String infixExpression;
    protected String postfixExpression;

    public Expression(String expressionString) {
        this.expressionString = expressionString;
        this.variables = extractVariables(expressionString);
        this.variableValues = new HashMap<>();
        this.output = null;
    }

    public String getInfixExpression() {
        return infixExpression;
    }

    public String getPostfixExpression() {
        return postfixExpression;
    }

    public String getExpressionString() {
        return expressionString;
    }


    public Set<String> getVariables() {
        return variables;
    }

    protected Map<String, Object> getVariableValues() {
        return variableValues;
    }


    protected Set<String> extractVariables(String expression) {
        Set<String> variableSet = new HashSet<>();
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            variableSet.add(matcher.group());
        }
        return variableSet;
    }


    public Object getVariable(String variable) {
        if (variableValues.containsKey(variable)) {
            return variableValues.get(variable);
        } else {
            throw new IllegalArgumentException("Variable " + variable + " has not been set.");
        }
    }

    public void setVariable(String variable, Object value) {
        if (variables.contains(variable)) {
            variableValues.put(variable, value);
            output = null;
        } else {
            throw new IllegalArgumentException("Variable " + variable + " is not part of the expression.");
        }
    }

    public Object calculate() {
        if (output != null) {
            return output;
        }

        try {
            output = performCalculation();
            return output;
        } catch (IllegalArgumentException e) {
            output = null;
            throw e;
        }
    }

    // Protected getter
    protected Object getOutput() {
        return output;
    }

    // Protected setter
    protected void setOutput(Object output) {
        this.output = output;
    }

    protected abstract Object performCalculation();

    public abstract boolean isValid();

    protected abstract String operatorType(String operator);
    protected abstract boolean isOperator(String token);
    protected abstract int precedence(String operator);
    protected abstract boolean isOperand(String token);
    protected abstract boolean isLeftAssociative(String operator);

    
    
    protected String determineExpressionType(String expression, Set<String> SUPPORTED_OPERATORS) {
        // First, check if parentheses are balanced
        if (!isParenthesesBalanced(expression)) {
            return "unknown";
        }
    
        expression = expression.trim().replaceAll("\\s+", " ");
    
        // Check for infix characteristics: parentheses and operators between operands
        boolean hasInfixOperators = Pattern.compile("\\b(" + String.join("|", SUPPORTED_OPERATORS) + ")\\b").matcher(expression).find();
        boolean hasParentheses = expression.contains("(") || expression.contains(")");
    
        if (hasInfixOperators && hasParentheses) {
            return "infix";
        }
    
        // Check for postfix characteristics
        String[] tokens = expression.split(" ");
        Stack<String> stack = new Stack<>();
    
        for (String token : tokens) {
            if (Pattern.matches("[A-Z]", token)) {
                stack.push(token);
            } else if (SUPPORTED_OPERATORS.contains(token)) {
                String type = operatorType(token);
    
                if (type.equals("unary")) {
                    if (stack.isEmpty()) {
                        return "unknown";
                    }
                } else if (type.equals("binary")) {
                    if (stack.size() < 2) {
                        return "unknown";
                    }
                    stack.pop(); // Simulate reduction of operands for a binary operator
                } else {
                    return "unknown"; // Unsupported operator type
                }
            } else {
                return "unknown"; // Unsupported token
            }
        }
    
        return stack.size() == 1 ? "postfix" : "unknown";
    }

    protected void validateExpression(String expressionString, Set<String> SUPPORTED_OPERATORS) {
        if (expressionString == null || expressionString.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid expression: Expression cannot be empty.");
        }

        String[] tokens = expressionString.split(" ");
        int operandCount = 0;

        for (String token : tokens) {
            if (SUPPORTED_OPERATORS.contains(token)) {
                String type = operatorType(token);

                switch (type) {
                    case "unary":
                        // Unary operators require one operand
                        if (operandCount < 1) {
                            throw new IllegalArgumentException("Operator '" + token + "' requires one operand but none was found.");
                        }
                        break;

                    case "binary":
                        // Binary operators require two operands
                        if (operandCount < 2) {
                            throw new IllegalArgumentException(
                                    "Operator '" + token + "' requires two operands but only " + operandCount + " found.");
                        }
                        operandCount--; // Each binary operation reduces the operand count by one
                        break;

                    default:
                        throw new IllegalArgumentException("Unsupported operator: '" + token + "'.");
                }
            } else if (isValidVariable(token)) {
                // Valid variable encountered, increase operand count
                operandCount++;
            } else {
                // Unsupported token found
                throw new IllegalArgumentException("Unsupported token: '" + token
                        + "'. Valid tokens are variables [A-Z] or operators " + SUPPORTED_OPERATORS);
            }
        }

        // Final check: a valid postfix expression should leave exactly one result
        if (operandCount != 1) {
            throw new IllegalArgumentException(
                    "Invalid postfix expression format. Expected a single final result, but found " + operandCount
                            + " remaining.");
        }
    }

    protected String convertPostfixToInfix(String expressionString) {
        Stack<String> stack = new Stack<>();
        String[] tokens = expressionString.split(" ");
    
        for (String token : tokens) {
            if (isValidVariable(token)) {
                // Push variables directly to the stack
                stack.push(token);
            } else {
                String type = operatorType(token);
    
                switch (type) {
                    case "unary":
                        // Unary operators require one operand
                        if (stack.isEmpty()) {
                            throw new IllegalArgumentException("Invalid postfix expression for unary operator '" + token + "'.");
                        }
                        String operand = stack.pop();
                        String resultUnary = "(" + token + " " + operand + ")";
                        stack.push(resultUnary);
                        break;
    
                    case "binary":
                        // Binary operators require two operands
                        if (stack.size() < 2) {
                            throw new IllegalArgumentException("Invalid postfix expression for binary operator '" + token + "'.");
                        }
                        String operand2 = stack.pop();
                        String operand1 = stack.pop();
                        String resultBinary = "(" + operand1 + " " + token + " " + operand2 + ")";
                        stack.push(resultBinary);
                        break;
    
                    default:
                        throw new IllegalArgumentException("Invalid operator: '" + token + "'.");
                }
            }
        }
    
        // Final infix expression should be the only item left in the stack
        if (stack.size() == 1) {
            return stack.pop();
        } else {
            throw new IllegalArgumentException("Invalid postfix expression format. Conversion to infix failed.");
        }
    }

    protected String convertInfixToPostfix(String infix) {
        // Normalize spaces and insert spaces around parentheses
        infix = infix.trim().replaceAll("\\s+", " ");
        // Insert spaces around parentheses
        infix = infix.replaceAll("([()])", " $1 ");
        // Normalize spaces again
        infix = infix.trim().replaceAll("\\s+", " ");
        String[] tokens = infix.split(" ");

        StringBuilder result = new StringBuilder();
        Stack<String> stack = new Stack<>();

        for (String token : tokens) {
            if (isOperand(token)) {
                // Operand: add directly to output
                result.append(token).append(" ");
            } else if (token.equals("(")) {
                // Left parenthesis: push onto stack
                stack.push(token);
            } else if (token.equals(")")) {
                // Right parenthesis: pop until left parenthesis
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    result.append(stack.pop()).append(" ");
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop(); // Remove '(' from stack
                } else {
                    throw new IllegalArgumentException("Mismatched parentheses in expression");
                }
            } else if (isOperator(token)) {
                // Operator: pop operators with higher or equal precedence
                while (!stack.isEmpty() && !stack.peek().equals("(") &&
                        ((isLeftAssociative(token) && precedence(token) <= precedence(stack.peek())) ||
                                (!isLeftAssociative(token) && precedence(token) < precedence(stack.peek())))) {
                    result.append(stack.pop()).append(" ");
                }
                stack.push(token);
            } else {
                // Invalid token encountered
                throw new IllegalArgumentException("Invalid token in expression: " + token);
            }
        }

        // Pop any remaining operators from the stack
        while (!stack.isEmpty()) {
            if (stack.peek().equals("(") || stack.peek().equals(")")) {
                throw new IllegalArgumentException("Mismatched parentheses in expression");
            }
            result.append(stack.pop()).append(" ");
        }

        // Return the postfix expression without trailing whitespace
        return result.toString().trim();
    }

    /**
     * Checks if parentheses in an infix expression are balanced.
     * 
     * @param expression The infix expression to validate.
     * @return true if the parentheses are balanced, false otherwise.
     */
    protected boolean isParenthesesBalanced(String expression) {
        Stack<Character> stack = new Stack<>();

        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                if (stack.isEmpty()) {
                    return false; // Unmatched closing parenthesis
                }
                stack.pop(); // Match found, pop the opening parenthesis
            }
        }

        // If stack is empty, all opening parentheses were matched
        return stack.isEmpty();
    }

    protected boolean isValidVariable(String token) {
        return Pattern.matches("[A-Z]", token); // Checks if the token is a single uppercase letter
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("expressionString", expressionString);
        json.put("variables", variables);
        json.put("variableValues", variableValues);
        return json;
    }

    @Override
    public String toString() {
        return "Expression{" +
                "expressionString='" + expressionString + '\'' +
                ", variables=" + variables +
                ", variableValues=" + variableValues +
                ", output=" + output +
                '}';
    }
}
