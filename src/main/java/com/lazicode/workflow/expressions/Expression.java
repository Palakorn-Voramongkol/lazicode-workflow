
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
    private Object cachedResult;

    public Expression(String expressionString) {
        this.expressionString = expressionString;
        this.variables = extractVariables(expressionString);
        this.variableValues = new HashMap<>();
        this.cachedResult = null;
    }

    public String getExpressionString() {
        return expressionString;
    }

    public Set<String> getVariables() {
        return variables;
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

    public abstract void setVariable(String variable, Object value);

    public Object getVariable(String variable) {
        if (variableValues.containsKey(variable)) {
            return variableValues.get(variable);
        } else {
            throw new IllegalArgumentException("Variable " + variable + " has not been set.");
        }
    }

    protected void setVariableValue(String variable, Object value) {
        if (variables.contains(variable)) {
            variableValues.put(variable, value);
            cachedResult = null;
        } else {
            throw new IllegalArgumentException("Variable " + variable + " is not part of the expression.");
        }
    }

    public Object calculate() {
        if (cachedResult != null) {
            return cachedResult;
        }

        try {
            cachedResult = performCalculation();
            return cachedResult;
        } catch (IllegalArgumentException e) {
            cachedResult = null;
            throw e;
        }
    }

    // Protected getter
    protected Object getCachedResult() {
        return cachedResult;
    }

    // Protected setter
    protected void setCachedResult(Object cachedResult) {
        this.cachedResult = cachedResult;
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
                ", cachedResult=" + cachedResult +
                '}';
    }
}
