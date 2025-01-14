package com.lazicode.workflow.expressions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;
import java.util.List;

import org.json.JSONObject;

import com.lazicode.workflow.exceptions.InvalidExpression;
import com.lazicode.workflow.interfaces.JSONPersistable;
import com.lazicode.workflow.expressions.utils.*;

/**
 * Abstract class representing a generic expression. It provides methods to
 * handle
 * infix and postfix expressions, manage variables, and serialize the expression
 * to JSON.
 */
public abstract class Expression implements JSONPersistable {
    private String expressionString;
    private Set<String> variables;
    private Map<String, Object> variableValues;
    private Object output;
    protected String infixExpression;
    protected String postfixExpression;

    /**
     * Constructs an Expression object, initializing the expression string,
     * extracting
     * variables, and setting up storage for variable values and output.
     *
     * @param expressionString The original expression string.
     */
    public Expression(String expressionString) throws InvalidExpression {

        this.expressionString = ExpressionUtils.normalizeSpaces(expressionString); // Cleanse spaces
        validateExpression(this.expressionString);
        this.variables = extractVariables(expressionString);
        this.variableValues = new HashMap<>();
        this.output = null;

    }

    /**
     * Returns the infix expression representation.
     *
     * @return The infix expression as a String.
     */
    public String getInfixExpression() {
        return infixExpression;
    }

    /**
     * Returns the postfix expression representation.
     *
     * @return The postfix expression as a String.
     */
    public String getPostfixExpression() {
        return postfixExpression;
    }

    /**
     * Returns the original expression string.
     *
     * @return The original expression string.
     */
    public String getExpressionString() {
        return expressionString;
    }

    /**
     * Retrieves the set of variables used in the expression.
     *
     * @return A Set containing the variables in the expression.
     */
    public Set<String> getVariables() {
        return variables;
    }

    /**
     * Retrieves the map of variable values.
     *
     * @return A Map containing variable values.
     */
    protected Map<String, Object> getVariableValues() {
        return variableValues;
    }

    /**
     * Extracts variables from the given expression string by identifying uppercase
     * letters.
     *
     * @param expression The expression string to analyze.
     * @return A Set of variables found in the expression.
     */
    protected Set<String> extractVariables(String expression) {
        Set<String> variableSet = new HashSet<>();
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            variableSet.add(matcher.group());
        }
        return variableSet;
    }

    /**
     * Retrieves the value of a specific variable.
     *
     * @param variable The variable name.
     * @return The value of the specified variable.
     * @throws InvalidExpression If the variable is not set.
     */
    public Object getVariable(String variable) throws InvalidExpression{
        if (variableValues.containsKey(variable)) {
            return variableValues.get(variable);
        } else {
            throw new InvalidExpression("Variable " + variable + " has not been set.");
        }
    }

    /**
     * Sets the value for a specific variable.
     *
     * @param variable The variable name.
     * @param value    The value to assign to the variable.
     * @throws InvalidExpression If the variable is not part of the
     *                                  expression.
     */
    public void setVariable(String variable, Object value) throws InvalidExpression{
        if (variables.contains(variable)) {
            variableValues.put(variable, value);
            output = null; // Reset output to null as the expression has changed
        } else {
            throw new InvalidExpression("Variable " + variable + " is not part of the expression.");
        }
    }

    /**
     * Calculates and returns the result of the expression, caching the output for
     * reuse.
     *
     * @return The calculated result of the expression.
     * @throws InvalidExpression If the calculation fails.
     */
    private Object calculate() {

        // Set the output cache
        output = performCalculation();
        return output;

    }

    /**
     * Returns the cached output of the expression calculation.
     *
     * @return The cached output.
     */
    public Object getOutput() {
        output = calculate();
        return output;
    }


    /**
     * Abstract method for performing the calculation of the expression.
     *
     * @return The calculated result.
     */
    protected abstract Object performCalculation();

    /**
     * Abstract method to validate the expression format.
     *
     * @return true if the expression is valid; false otherwise.
     */
    public abstract boolean isValid();

    /**
     * Determines the operator type of a given operator.
     *
     * @param operator The operator to analyze.
     * @return The operator type as a String.
     */
    protected abstract String operatorType(String operator);

    /**
     * Determines if a token is an operator.
     *
     * @param token The token to analyze.
     * @return true if the token is an operator; false otherwise.
     */
    protected abstract boolean isOperator(String token);

    /**
     * Retrieves the precedence of a given operator.
     *
     * @param operator The operator to analyze.
     * @return The precedence level as an integer.
     */
    protected abstract int precedence(String operator);

    /**
     * Determines if a token is an operand.
     *
     * @param token The token to analyze.
     * @return true if the token is an operand; false otherwise.
     */
    protected abstract boolean isOperand(String token);

    /**
     * Determines if an operator is left-associative.
     *
     * @param operator The operator to analyze.
     * @return true if the operator is left-associative; false if right-associative.
     */
    protected abstract boolean isLeftAssociative(String operator);

    public static boolean hasParentheses(String expression) {
        return expression.contains("(") || expression.contains(")");
    }

    /**
     * Checks if the expression is in infix notation by ensuring each operand is separated
     * by a binary operator.
     *
     * @param expression The expression to evaluate.
     * @return true if the expression follows infix notation; false otherwise.
     */
    private boolean isInfix(String expression) {
        // Remove all parentheses from the expression for simple infix evaluation
        expression = expression.replaceAll("[()]", "");
        
        String[] tokens = expression.trim().split("\\s+");

        boolean expectingOperand = true; // Track alternation between operand and operator

        for (String token : tokens) {
            if (isOperand(token)) {
                if (!expectingOperand) {
                    return false; // Two operands in a row without operator in between
                }
                expectingOperand = false; // After operand, expect operator next
            } else if (isOperator(token) && operatorType(token).equals("binary")) {
                if (expectingOperand) {
                    return false; // Two operators in a row or operator at the beginning
                }
                expectingOperand = true; // After operator, expect operand next
            } else {
                // Any other unexpected token (unsupported operator, invalid characters)
                return false;
            }
        }

        // Ensure the expression ends with an operand
        return !expectingOperand;
    }

    /**
     * Validates that the expression contains only valid operands and operators.
     * Throws an exception if an invalid token is found.
     *
     * @param expression The expression to validate.
     * @throws InvalidExpression if an invalid operand or operator is found.
     */
    public void validateExpression(String expression) throws InvalidExpression {

        expression = addSpacesAroundParentheses(expression);
        String[] tokens = expression.trim().split("\\s+");

        for (String token : tokens) {
            if (!isOperand(token) && !isOperator(token) && !token.equals("(") && !token.equals(")")) {
                throw new InvalidExpression("Invalid token found in expression: '" + token + "'");
            }
        }
    }

    /**
     * Determines if an expression is infix, postfix, or unknown.
     *
     * @param expression          The expression string to evaluate.
     * @param SUPPORTED_OPERATORS Set of supported operators.
     * @return "infix" if infix notation, "postfix" if postfix notation, "unknown"
     *         otherwise.
     */
    protected String determineExpressionType(String expression, Set<String> SUPPORTED_OPERATORS) {

        
        
        if (hasParentheses(expression) || isInfix(expression)) {
            return "infix";
        }

        if (!isParenthesesBalanced(expression)) {
            return "unknown";
        }
    
        expression = expression.trim().replaceAll("\\s+", " ");
    
        // Escape each operator individually for regex pattern matching
        Set<String> escapedOperators = new HashSet<>();
        for (String op : SUPPORTED_OPERATORS) {
            escapedOperators.add(Pattern.quote(op));
        }
    
        // Use escaped operators to compile a regex for infix check
        String infixPattern = "\\(.*\\b(" + String.join("|", escapedOperators) + ")\\b.*\\)";
        boolean hasInfixOperators = Pattern.compile(infixPattern).matcher(expression).find();
    
        boolean hasParentheses = expression.contains("(") || expression.contains(")");
    
        if (hasInfixOperators && hasParentheses) {
            return "infix";
        }
    
        String[] tokens = expression.split(" ");
        Stack<String> stack = new Stack<>();
        boolean lastWasOperand = false;

        for (String token : tokens) {
            if (Pattern.matches("[A-Z]", token)) {
                if (lastWasOperand) {
                    // Found two consecutive operands, indicating postfix notation
                    return "postfix";
                } else {
                    lastWasOperand = true;
                }
                stack.push(token);
            } else if (SUPPORTED_OPERATORS.contains(token)) {
                lastWasOperand = false;
                String type = operatorType(token);
    
                if (type.equals("unary")) {
                    if (stack.isEmpty()) {
                        return "unknown";
                    }
                } else if (type.equals("binary")) {
                    if (stack.size() < 2) {
                        return "unknown";
                    }
                    stack.pop();
                } else {
                    return "unknown";
                }
            } else {
                return "unknown";
            }
        }
    
        return stack.size() == 1 ? "postfix" : "unknown";
    }
    

    /**
     * Validates the Postfix format of a given expression.
     *
     * @param expressionString    The expression string to validate.
     * @param SUPPORTED_OPERATORS Set of supported operators.
     * @throws InvalidExpression If the expression is invalid.
     */
    protected void validatePostfixExpression(String expressionString, Set<String> SUPPORTED_OPERATORS)
            throws InvalidExpression {
        if (expressionString == null || expressionString.trim().isEmpty()) {
            throw new InvalidExpression("Invalid expression: Expression cannot be empty.");
        }

        String[] tokens = expressionString.split(" ");
        int operandCount = 0;

        for (String token : tokens) {

            if (SUPPORTED_OPERATORS.contains(token)) {
                String type = operatorType(token);

                switch (type) {
                    case "unary":
                        if (operandCount < 1) {
                            throw new InvalidExpression(
                                    "Operator '" + token + "' requires one operand but none was found.");
                        }
                        break;

                    case "binary":
                        if (operandCount < 2) {
                            throw new InvalidExpression("Operator '" + token + "' requires two operands but only "
                                    + operandCount + " found.");
                        }
                        operandCount--;
                        break;

                    default:
                        throw new InvalidExpression("Unsupported operator: '" + token + "'.");
                }
            } else if (isValidVariable(token)) {
                operandCount++;
            } else {
                throw new InvalidExpression("Unsupported token: '" + token
                        + "'. Valid tokens are variables [A-Z] or operators " + SUPPORTED_OPERATORS);
            }
        }

        if (operandCount != 1) {
            throw new InvalidExpression("Invalid postfix expression format. Expected a single final result, but found "
                    + operandCount + " remaining.");
        }
    }

    /**
     * Converts a postfix expression to infix notation.
     *
     * @param expressionString The postfix expression to convert.
     * @return The converted infix expression.
     * @throws InvalidExpression If the postfix expression is invalid.
     */
    protected String convertPostfixToInfix(String expressionString) throws InvalidExpression {
        Stack<String> stack = new Stack<>();
        String[] tokens = expressionString.split(" ");

        for (String token : tokens) {
            if (isValidVariable(token)) {
                stack.push(token);
            } else {
                String type = operatorType(token);

                switch (type) {
                    case "unary":
                        if (stack.isEmpty()) {
                            throw new InvalidExpression(
                                    "Invalid postfix expression for unary operator '" + token + "'.");
                        }
                        String operand = stack.pop();
                        String resultUnary = "(" + token + " " + operand + ")";
                        stack.push(resultUnary);
                        break;

                    case "binary":
                        if (stack.size() < 2) {
                            throw new InvalidExpression(
                                    "Invalid postfix expression for binary operator '" + token + "'.");
                        }
                        String operand2 = stack.pop();
                        String operand1 = stack.pop();
                        String resultBinary = "(" + operand1 + " " + token + " " + operand2 + ")";
                        stack.push(resultBinary);
                        break;

                    default:
                        throw new InvalidExpression("Invalid operator: '" + token + "'.");
                }
            }
        }

        if (stack.size() == 1) {
            return stack.pop();
        } else {
            throw new InvalidExpression("Invalid postfix expression format. Conversion to infix failed.");
        }
    }

    /**
     * Converts an infix expression to postfix notation.
     *
     * @param infix The infix expression to convert.
     * @return The converted postfix expression.
     * @throws InvalidExpression if there is an error in the expression format.
     */
    protected String convertInfixToPostfix(String infix) throws InvalidExpression {
        // Normalize spaces and insert spaces around parentheses
        infix = infix.trim().replaceAll("\\s+", " ");
        infix = infix.replaceAll("([()])", " $1 ");
        infix = infix.trim().replaceAll("\\s+", " ");
        String[] tokens = infix.split(" ");

        boolean lastWasOperand = false;

        for (String token : tokens) {
            if (isOperand(token)) {
                if (lastWasOperand) {
                    throw new InvalidExpression("Missing operator between operands: " + token);
                }
                lastWasOperand = true;
            } else if (isOperator(token) || token.equals("(") || token.equals(")")) {
                lastWasOperand = false;
            } else {
                throw new InvalidExpression("Invalid token detected: " + token);
            }
        }

        StringBuilder result = new StringBuilder();
        Stack<String> stack = new Stack<>();
        boolean expectOperand = true;

        if (infix == null || infix.trim().isEmpty()) {
            throw new InvalidExpression("Expression cannot be empty.");
        }

        for (String token : tokens) {

            if (!isOperator(token) && !isOperand(token) && !isParenthesis(token)) {
                throw new InvalidExpression("Invalid token detected: " + token);
            }
            if (isOperand(token)) {
                // Operand: add directly to output
                result.append(token).append(" ");
                expectOperand = false; // Next, expect an operator
            } else if (token.equals("(")) {
                // Left parenthesis: push onto stack
                stack.push(token);
                expectOperand = true; // After '(', expect an operand
            } else if (token.equals(")")) {
                // Right parenthesis: pop until left parenthesis
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    result.append(stack.pop()).append(" ");
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop(); // Remove '(' from stack
                } else {
                    throw new InvalidExpression("Mismatched parentheses in expression");
                }
                expectOperand = false; // After ')', expect an operator
            } else if (isOperator(token)) {
                if (isUnaryOperator(token) && expectOperand) {
                    // Handle unary operator
                    stack.push(token);
                } else {
                    // Binary operator: pop operators with higher or equal precedence
                    while (!stack.isEmpty() && !stack.peek().equals("(") &&
                            ((isLeftAssociative(token) && precedence(token) <= precedence(stack.peek())) ||
                                    (!isLeftAssociative(token) && precedence(token) < precedence(stack.peek())))) {
                        result.append(stack.pop()).append(" ");
                    }
                    stack.push(token);
                    expectOperand = true; // After binary operator, expect an operand
                }
            } else {
                // Invalid token encountered
                throw new InvalidExpression("Invalid token in expression: " + token);
            }
        }

        // Pop any remaining operators from the stack
        while (!stack.isEmpty()) {
            if (stack.peek().equals("(") || stack.peek().equals(")")) {
                throw new InvalidExpression("Mismatched parentheses in expression");
            }
            result.append(stack.pop()).append(" ");
        }

        // Return the postfix expression without trailing whitespace
        return result.toString().trim();
    }

    /**
     * Checks if the given operator is a unary operator.
     * 
     * @param token The token to check.
     * @return true if the token is a unary operator, false otherwise.
     */
    private boolean isUnaryOperator(String token) {
        // Define "NOT" as a unary operator. Add other unary operators here if needed.
        return "NOT".equals(token);
    }

    /**
     * Checks if parentheses in an infix expression are balanced.
     *
     * @param expression The infix expression to validate.
     * @return true if the parentheses are balanced; false otherwise.
     */
    protected boolean isParenthesesBalanced(String expression) {
        Stack<Character> stack = new Stack<>();

        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                if (stack.isEmpty()) {
                    return false;
                }
                stack.pop();
            }
        }

        return stack.isEmpty();
    }

    protected boolean isParenthesis(String token) {
        return token.equals("(") || token.equals(")");
    }

    protected String addSpacesAroundParentheses(String expression) {
        // Add a space before and after each parenthesis
        expression = expression.replaceAll("\\(", " ( ");
        expression = expression.replaceAll("\\)", " ) ");
        
        // Replace multiple spaces with a single space to clean up
        expression = expression.trim().replaceAll("\\s+", " ");
        
        return expression;
    }

    /**
     * Validates if the token is a single uppercase letter variable.
     *
     * @param token The token to validate.
     * @return true if the token is a valid variable; false otherwise.
     */
    protected boolean isValidVariable(String token) {
        return Pattern.matches("[A-Z]", token);
    }

    /**
     * Converts the expression to JSON format for serialization.
     *
     * @return A JSONObject representing the expression.
     */
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("expressionString", expressionString);
        json.put("variables", variables);
        json.put("variableValues", variableValues);
        return json;
    }

    /**
     * Returns a string representation of the Expression object.
     *
     * @return String representation of the Expression.
     */
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
