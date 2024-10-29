package com.lazicode.workflow.exceptions;

public class InvalidOperatorPlacementException extends IllegalArgumentException {
    public InvalidOperatorPlacementException(String operator, String previousOperator) {
        super("Invalid operator placement: " + operator + " after " + (previousOperator != null ? previousOperator : "start of expression"));
    }
}
