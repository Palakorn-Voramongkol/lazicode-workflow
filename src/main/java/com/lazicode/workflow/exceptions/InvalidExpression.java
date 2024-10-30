package com.lazicode.workflow.exceptions;

/**
 * Custom exception to represent an invalid expression in the workflow.
 * This exception provides several constructors to handle different scenarios, such as:
 * - Default error message
 * - Custom error message
 * - Wrapping an underlying exception with a custom message
 * - Wrapping an underlying exception as the cause without a custom message
 */
public class InvalidExpression extends Exception {

    /**
     * Default constructor.
     * Initializes the exception with a default message, "Invalid expression."
     *
     * Example usage:
     * throw new InvalidExpressionException();
     */
    public InvalidExpression() {
        super("Invalid expression.");
    }

    /**
     * Constructor with a custom error message.
     * Useful for providing specific context about the invalid expression.
     *
     * @param message Custom error message describing the issue.
     *
     * Example usage:
     * throw new InvalidExpressionException("Expression is null or empty.");
     */
    public InvalidExpression(String message) {
        super(message);
    }

    /**
     * Constructor with a custom message and a cause.
     * Useful when an invalid expression error occurs as a result of another exception.
     * Allows both a custom message and the original exception to be included in the stack trace.
     *
     * @param message Custom error message.
     * @param cause   The original exception that caused this invalid expression error.
     *
     * Example usage:
     * try {
     *     parseExpression("invalid_expression");
     * } catch (NumberFormatException e) {
     *     throw new InvalidExpressionException("Failed to parse expression due to invalid number format.", e);
     * }
     */
    public InvalidExpression(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a cause only.
     * Useful for wrapping the original exception that caused the invalid expression error.
     * This constructor allows you to propagate the underlying exception without additional context.
     *
     * @param cause The original exception that caused this invalid expression error.
     *
     * Example usage:
     * try {
     *     evaluateExpression("10 / 0");
     * } catch (ArithmeticException e) {
     *     throw new InvalidExpressionException(e);
     * }
     */
    public InvalidExpression(Throwable cause) {
        super(cause);
    }
}
