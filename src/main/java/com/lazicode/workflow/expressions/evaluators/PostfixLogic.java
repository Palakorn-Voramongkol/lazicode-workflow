package com.lazicode.workflow.expressions.evaluators;

import java.util.Map;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class PostfixLogic {

    // Evaluates a postfix logical expression using recursive evaluation (no short-circuit)
    public static Boolean evalShortCircuit(String expression, Map<String, Boolean> values) {
        if (expression == null || expression.isEmpty()) {
            return false;  // Empty expression case
        }

        List<String> tokens = new ArrayList<>(Arrays.asList(expression.split("\\s+")));
        return evaluatePostfixHelper(tokens, values);
    }

    private static Boolean evaluatePostfixHelper(List<String> tokens, Map<String, Boolean> values) {
        if (tokens.isEmpty()) {
            return false;  // Empty expression case
        }

        String token = tokens.remove(tokens.size() - 1);

        if (values.containsKey(token)) {
            return values.get(token); // Return true, false, or null from values map
        }

        if (token.equals("NOT")) {
            Boolean operand = evaluatePostfixHelper(tokens, values);
            return (operand == null) ? null : !operand;
        }

        Boolean right = evaluatePostfixHelper(tokens, values);
        Boolean left = evaluatePostfixHelper(tokens, values);

        switch (token) {
            case "AND":
                if (Boolean.FALSE.equals(left) || Boolean.FALSE.equals(right)) {
                    return false;
                }
                if (left == null || right == null) {
                    return null;
                }
                return left && right;
            case "OR":
                if (Boolean.TRUE.equals(left) || Boolean.TRUE.equals(right)) {
                    return true;
                }
                if (left == null || right == null) {
                    return null;
                }
                return left || right;
            case "XOR":
                if (left == null || right == null) {
                    return null;
                }
                return left ^ right;
            case "NAND":
                if (left == null || right == null) {
                    return null;
                }
                return !(left && right);
            case "NOR":
                if (left == null || right == null) {
                    return null;
                }
                return !(left || right);
            case "XNOR":
                if (left == null || right == null) {
                    return null;
                }
                return !(left ^ right);
            default:
                throw new RuntimeException("Unsupported operator: " + token);
        }
    }

    // Evaluates a postfix logical expression using an iterative approach (no short-circuit)
    public static Boolean eval(String expression, Map<String, Boolean> values) {
        if (expression == null || expression.isEmpty()) {
            return false;  // Empty expression case
        }

        Stack<Boolean> stack = new Stack<>();
        String[] tokens = expression.split("\\s+");

        for (String token : tokens) {
            if (values.containsKey(token)) {
                // Operand: Push its value to the stack (true, false, or null)
                stack.push(values.get(token));
            } else {
                // Operator: Pop operands from the stack and evaluate
                switch (token.toUpperCase()) {
                    case "AND": {
                        Boolean right = stack.pop();
                        Boolean left = stack.pop();
                        if (left == null || right == null) {
                            stack.push(null);
                        } else {
                            stack.push(left && right);
                        }
                        break;
                    }
                    case "OR": {
                        Boolean right = stack.pop();
                        Boolean left = stack.pop();
                        if (left == null || right == null) {
                            stack.push(null);
                        } else {
                            stack.push(left || right);
                        }
                        break;
                    }
                    case "XOR": {
                        Boolean right = stack.pop();
                        Boolean left = stack.pop();
                        if (left == null || right == null) {
                            stack.push(null);
                        } else {
                            stack.push(left ^ right);
                        }
                        break;
                    }
                    case "NAND": {
                        Boolean right = stack.pop();
                        Boolean left = stack.pop();
                        if (left == null || right == null) {
                            stack.push(null);
                        } else {
                            stack.push(!(left && right));
                        }
                        break;
                    }
                    case "NOR": {
                        Boolean right = stack.pop();
                        Boolean left = stack.pop();
                        if (left == null || right == null) {
                            stack.push(null);
                        } else {
                            stack.push(!(left || right));
                        }
                        break;
                    }
                    case "XNOR": {
                        Boolean right = stack.pop();
                        Boolean left = stack.pop();
                        if (left == null || right == null) {
                            stack.push(null);
                        } else {
                            stack.push(!(left ^ right));
                        }
                        break;
                    }
                    case "NOT": {
                        Boolean operand = stack.pop();
                        if (operand == null) {
                            stack.push(null);
                        } else {
                            stack.push(!operand);
                        }
                        break;
                    }
                    default:
                        throw new RuntimeException("Unsupported operator: " + token);
                }
            }
        }

        // Final result should be the only item left in the stack
        return stack.size() == 1 ? stack.pop() : null;
    }
}
