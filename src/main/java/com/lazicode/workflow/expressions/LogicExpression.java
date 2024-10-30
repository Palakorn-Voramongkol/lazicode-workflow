package com.lazicode.workflow.expressions;

import java.util.Stack;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

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
                                "Invalid expression type, alow only valid infix or postfix logical expression.");
        }
        if (expressionType.equals("postfix")) {
            validateExpression(expressionString, SUPPORTED_OPERATORS); // Validate before proceeding
            infixExpression = convertPostfixToInfix(expressionString); // Convert and store the infix expression
            postfixExpression = expressionString; // Store the original postfix expression
        }    
        else {
            postfixExpression = convertInfixToPostfix(expressionString); // Convert and store the infix expression
            validateExpression(postfixExpression, SUPPORTED_OPERATORS); // Validate before proceeding
            infixExpression = convertPostfixToInfix(postfixExpression); // Convert and store the infix expression beautifully
        }
        
    }



    /**
     * Private helper to determine if an expression is infix or postfix.
     *
     * @param expression The expression string to evaluate.
     * @return "infix" if the expression is in infix notation, "postfix" if in
     *         postfix notation, "unknown" if neither.
     */
    /**
     * Determines if an expression is in infix or postfix notation, considering
     * balanced parentheses.
     * 
     * @param expression The expression string to evaluate.
     * @return "infix" if the expression is in infix notation, "postfix" if in
     *         postfix notation, "unknown" if neither.
     */
    /* 
    private String determineExpressionType(String expression) {
        // First, check if parentheses are balanced
        if (!isParenthesesBalanced(expression)) {
            return "unknown";
        }

        expression = expression.trim().replaceAll("\\s+", " ");

        // Check for infix characteristics: parentheses and operators between operands
        boolean hasInfixOperators = Pattern.compile("\\b(AND|OR|NAND|NOR|XOR|XNOR|NOT)\\b").matcher(expression).find();
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
                if (token.equals("NOT")) {
                    if (stack.isEmpty()) {
                        return "unknown";
                    }
                } else {
                    if (stack.size() < 2) {
                        return "unknown";
                    }
                    stack.pop(); // Simulate reduction of operands for binary operator
                }
            } else {
                return "unknown";
            }
        }
        return stack.size() == 1 ? "postfix" : "unknown";
    }
        */
    /* 
    private String determineExpressionType(String expression, Set<String> SUPPORTED_OPERATORS) {
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
        
*/
    /**
     * Cleanses multiple spaces in the expression, reducing them to a single space.
     *
     * @param expression The original expression string with potential extra spaces
     * @return A string with extra spaces removed, leaving only single spaces
     *         between tokens
     */
    private String normalizeSpaces(String expression) {
        return expression.trim().replaceAll("\\s+", " ");
    }
/* 
    private void validateExpression(String expressionString) {
        if (expressionString == null || expressionString.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid expression: Expression cannot be empty.");
        }

        String[] tokens = expressionString.split(" ");
        int operandCount = 0;

        for (String token : tokens) {
            if (SUPPORTED_OPERATORS.contains(token)) {
                if (token.equals("NOT")) {
                    // NOT is a unary operator, requires one operand
                    if (operandCount < 1) {
                        throw new IllegalArgumentException("Operator 'NOT' requires one operand but none was found.");
                    }
                } else {
                    // All other operators are binary, require two operands
                    if (operandCount < 2) {
                        throw new IllegalArgumentException(
                                "Operator '" + token + "' requires two operands but only " + operandCount + " found.");
                    }
                    operandCount--; // Each binary operation reduces the operand count by one
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
*/
/* 
    private void validateExpression(String expressionString) {
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
*/
/* 
    private boolean isValidVariable(String token) {
        return Pattern.matches("[A-Z]", token); // Checks if the token is a single uppercase letter
    }
*/
/* 
    private String convertPostfixToInfix(String expressionString) {
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
*/
/* 
    private String convertPostfixToInfix(String expressionString) {
        Stack<String> stack = new Stack<>();
        String[] tokens = expressionString.split(" ");

        for (String token : tokens) {
            if (isValidVariable(token)) {
                // Push variables directly to the stack
                stack.push(token);
            } else {
                // Operators require popping one or two operands and forming an infix expression
                if (token.equals("NOT")) {
                    // Unary operator NOT
                    if (stack.isEmpty()) {
                        throw new IllegalArgumentException("Invalid postfix expression for unary operator 'NOT'.");
                    }
                    String operand = stack.pop();
                    String result = "(NOT " + operand + ")";
                    stack.push(result);
                } else {
                    // Binary operators like AND, OR, NAND, NOR, XOR, XNOR
                    if (stack.size() < 2) {
                        throw new IllegalArgumentException(
                                "Invalid postfix expression for binary operator '" + token + "'.");
                    }
                    String operand2 = stack.pop();
                    String operand1 = stack.pop();
                    String result = "(" + operand1 + " " + token + " " + operand2 + ")";
                    stack.push(result);
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
*/
    /**
     * Converts an infix expression to a postfix expression.
     * 
     * @param infix The infix expression to convert.
     * @return The postfix notation of the given infix expression.
     */
    /* 
    private String convertInfixToPostfix(String infix) {
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
    */
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
