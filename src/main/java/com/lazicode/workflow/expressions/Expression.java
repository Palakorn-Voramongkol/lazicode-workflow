
package com.lazicode.workflow.expressions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
