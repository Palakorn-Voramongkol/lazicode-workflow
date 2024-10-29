package com.lazicode.workflow.tasks;

import com.lazicode.workflow.expressions.Expression;
import com.lazicode.workflow.interfaces.ContainerAble;
import com.lazicode.workflow.node.Node;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * Abstract class representing a Task node in a workflow system.
 * A Task can contain multiple child nodes, maintaining their sequence.
 */
public abstract class Task extends Node implements ContainerAble {
    private LinkedHashSet<Node> nodes;

    /**
     * Constructs a Task with the specified name.
     *
     * @param name The name of the Task.
     */
    public Task(String name) {
        super(name);
        this.nodes = new LinkedHashSet<>();
    }

    /**
     * Constructs a Task with the specified name and expression.
     *
     * @param name        The name of the Task.
     * @param expression  The expression governing the Task's logic.
     * @param minInput    The minimum number of input connections.
     * @param maxInput    The maximum number of input connections.
     * @param minOutput   The minimum number of output connections.
     * @param maxOutput   The maximum number of output connections.
     */
    public Task(String name, Expression expression, int minInput, int maxInput, int minOutput, int maxOutput) {
        super(name, expression, minInput, maxInput, minOutput, maxOutput);
        this.nodes = new LinkedHashSet<>();
    }

    /**
     * Adds a child node to this Task.
     *
     * @param node The child node to add.
     * @throws IllegalArgumentException If the child node is null, already a child, or creates a cycle.
     */
    @Override
    public void addNode(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Child node cannot be null.");
        }
        if (nodes.contains(node)) {
            throw new IllegalArgumentException("Child node '" + node.getName() + "' is already added.");
        }
        if (createsCycle(node)) {
            throw new IllegalArgumentException("Adding node '" + node.getName() + "' would create a cycle.");
        }
        nodes.add(node);
        this.connect(node); // Establish a bidirectional connection
    }

    /**
     * Removes a child node from this Task.
     *
     * @param node The child node to remove.
     * @throws IllegalArgumentException If the child node is null or not a child.
     */
    @Override
    public void removeNode(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Child node cannot be null.");
        }
        if (!nodes.contains(node)) {
            throw new IllegalArgumentException("Child node '" + node.getName() + "' is not a child of this Task.");
        }
        nodes.remove(node);
        this.disconnect(node); // Remove the bidirectional connection
    }

    /**
     * Retrieves all child nodes of this Task in sequence.
     *
     * @return A LinkedHashSet of child nodes.
     */
    @Override
    public LinkedHashSet<Node> getNodes() {
        return nodes;
    }

    /**
     * Checks if this Task has any child nodes.
     *
     * @return True if there are child nodes; otherwise, false.
     */
    @Override
    public boolean hasNodes() {
        return !nodes.isEmpty();
    }



    /**
     * Converts the Task and its child nodes to a JSON representation.
     *
     * @return JSONObject containing Task and its children details.
     */
    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        JSONArray nodesArray = new JSONArray();
        for (Node node : nodes) {
            nodesArray.put(node.toJSON());
        }
        json.put("nodes", nodesArray);
        return json;
    }

    /**
     * Provides a string representation of the Task, including its child nodes.
     *
     * @return String description of the Task.
     */
    @Override
    public String toString() {
        String nodeNames = nodes.stream()
                .map(Node::getName)
                .collect(Collectors.joining(", "));
        return super.toString() +
                ", Task{" +
                "nodes=[" + nodeNames + "]" +
                '}';
    }

    /**
     * Checks if adding the specified node would create a cyclic dependency.
     *
     * @param node The node to check.
     * @return True if a cycle would be created; otherwise, false.
     */
    private boolean createsCycle(Node node) {
        if (node instanceof Task) {
            Task taskNode = (Task) node;
            return taskNode.containsAncestor(this);
        }
        return false;
    }

    /**
     * Recursively checks if this Task is an ancestor of the specified node.
     *
     * @param node The node to check against.
     * @return True if this Task is an ancestor; otherwise, false.
     */
    private boolean containsAncestor(Node node) {
        if (this == node) {
            return true;
        }
        for (Node parent : this.getConnections()) {
            if (parent instanceof Task) {
                if (((Task) parent).containsAncestor(node)) {
                    return true;
                }
            }
        }
        return false;
    }
}
