package com.lazicode.workflow.node;

import org.json.JSONObject;
import com.lazicode.workflow.expressions.Expression;
import com.lazicode.workflow.interfaces.InputAble;
import com.lazicode.workflow.interfaces.JSONPersistable;
import com.lazicode.workflow.interfaces.OutputAble;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors; // Import Collectors here

/**
 * Abstract class representing a Node in a workflow system.
 * Each Node can have input and output connections to other nodes, 
 * governed by rules defined by `InputAble`, `OutputAble`, and `JSONPersistable` interfaces.
 * Nodes also contain expressions to define logical or mathematical operations.
 */
public abstract class Node implements InputAble, OutputAble, JSONPersistable {
    private String name;
    private Set<Node> connections;
    private Expression expression;
    private int maxInput;
    private int maxOutput;
    private int minInput;
    private int minOutput;
    private String creationTime; // Stores the node creation time in UTC

    public Node(String name) {
        this.name = name;
        this.connections = new HashSet<>();
        this.maxInput = Integer.MAX_VALUE;
        this.maxOutput = Integer.MAX_VALUE;
        this.minInput = 0;
        this.minOutput = 0;
        this.creationTime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }

    public Node(String name, Expression expression, int minInput, int maxInput, int minOutput, int maxOutput) {
        if (!expression.isValid()) {
            throw new IllegalArgumentException("Invalid or incomplete expression provided.");
        }
        this.name = name;
        this.connections = new HashSet<>();
        this.expression = expression;
        this.minInput = minInput;
        this.maxInput = maxInput;
        this.minOutput = minOutput;
        this.maxOutput = maxOutput;
        this.creationTime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }
    

    @Override
    public void connect(Node otherNode) {
        if (otherNode == null || otherNode == this) {
            return;
        }
        this.connections.add(otherNode);
        otherNode.connections.add(this);
    }

    @Override
    public void disconnect(Node otherNode) {
        if (otherNode == null) {
            return;
        }
        this.connections.remove(otherNode);
        otherNode.connections.remove(this);
    }

    @Override
    public Set<Node> getConnections() {
        return connections;
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }

    public int getMaxInput() {
        return maxInput;
    }

    public int getMaxOutput() {
        return maxOutput;
    }

    public int getMinInput() {
        return minInput;
    }

    public int getMinOutput() {
        return minOutput;
    }

    /**
     * Returns the names of all nodes connected to this node.
     * 
     * @return A Set of names of connected nodes.
     */
    public Set<String> getConnectedNodeNames() {
        Set<String> connectedNodeNames = new HashSet<>();
        for (Node connectedNode : connections) {
            connectedNodeNames.add(connectedNode.getName());
        }
        return connectedNodeNames;
    }

    /**
     * String representation of the Node, including its name, expression,
     * input/output connection limits, creation time, and connection details.
     *
     * @return String description of the Node.
     */
    @Override
    public String toString() {
        String incomingConnections = connections.stream()
                .filter(node -> node.getConnections().contains(this))
                .map(Node::getName)
                .collect(Collectors.joining(", "));

        String outgoingConnections = connections.stream()
                .map(Node::getName)
                .collect(Collectors.joining(", "));

        return "Node{" +
                "name='" + name + '\'' +
                ", expression=" + expression +
                ", maxInput=" + maxInput +
                ", maxOutput=" + maxOutput +
                ", minInput=" + minInput +
                ", minOutput=" + minOutput +
                ", creationTime=" + creationTime +
                ", incomingConnections=[" + incomingConnections + "]" +
                ", outgoingConnections=[" + outgoingConnections + "]" +
                '}';
    }

    /**
     * Converts the Node to a JSON representation, 
     * suitable for persistence or data transfer.
     * Includes node name, input/output limits, creation time, expression, 
     * and connected node details.
     *
     * @return JSONObject containing node details.
     */
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("maxInput", maxInput);
        json.put("maxOutput", maxOutput);
        json.put("minInput", minInput);
        json.put("minOutput", minOutput);
        json.put("creationTime", creationTime);
        json.put("expression", expression != null ? expression.toJSON() : null);
        json.put("incomingConnections", connections.stream()
                .filter(node -> node.getConnections().contains(this))
                .map(Node::getName)
                .collect(Collectors.toList()));
        json.put("outgoingConnections", connections.stream()
                .map(Node::getName)
                .collect(Collectors.toList()));
        return json;
    }
}
