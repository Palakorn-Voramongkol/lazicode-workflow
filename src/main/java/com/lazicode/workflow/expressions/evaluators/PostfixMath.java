package com.lazicode.workflow.expressions.evaluators;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class PostfixMath {

    public static Double eval(String expression, Map<String, Double> values) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = expression.split(" ");

        for (String token : tokens) {
            if (values.containsKey(token)) {
                // Check if the variable is missing a value
                Double value = values.get(token);
                if (value == null) return Double.NaN;
                stack.push(value);
            } else {
                switch (token) {
                    case "+":
                        stack.push(stack.pop() + stack.pop());
                        break;
                    case "-":
                        if (stack.size() == 1) {
                            stack.push(-stack.pop());
                        } else {
                            double b = stack.pop();
                            double a = stack.pop();
                            stack.push(a - b);
                        }
                        break;
                    case "*":
                        stack.push(stack.pop() * stack.pop());
                        break;
                    case "/":
                        double divisor = stack.pop();
                        if (divisor == 0) return Double.NaN; // handle division by zero as NaN
                        stack.push(stack.pop() / divisor);
                        break;
                    case "%":
                        double mod = stack.pop();
                        stack.push(stack.pop() % mod);
                        break;
                    case "^":
                        double exponent = stack.pop();
                        double base = stack.pop();
                        stack.push(Math.pow(base, exponent));
                        break;
                    default:
                        try {
                            stack.push(Double.parseDouble(token));
                        } catch (NumberFormatException e) {
                            return Double.NaN; // return NaN if an unknown token is encountered
                        }
                }
            }
        }

        return stack.size() == 1 ? stack.pop() : Double.NaN;
    }

    public static void main(String[] args) {
        Map<String, Double> values = new HashMap<>();
        values.put("A", 2.0);
        values.put("B", 3.0);
        values.put("C", null); // Represents NA

        String expression = "A B + C *"; // Equivalent to "(A + B) * C"

        Double result = eval(expression, values);

        if (result.isNaN()) {
            System.out.println("Result: NA (Some variables are missing values)");
        } else {
            System.out.println("Result: " + result);
        }
    }
}
