
package node;

import org.json.JSONObject;
import java.util.HashSet;
import java.util.Set;

public abstract class Node implements InputAble, OutputAble, JSONPersistable {
    private String name;
    private Set<Node> connections;
    private Expression expression;
    private int maxInput;
    private int maxOutput;
    private int minInput;
    private int minOutput;

    public Node(String name) {
        this.name = name;
        this.connections = new HashSet<>();
        this.maxInput = Integer.MAX_VALUE;
        this.maxOutput = Integer.MAX_VALUE;
        this.minInput = 0;
        this.minOutput = 0;
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

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", expression=" + expression +
                ", maxInput=" + maxInput +
                ", maxOutput=" + maxOutput +
                ", minInput=" + minInput +
                ", minOutput=" + minOutput +
                '}';
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("maxInput", maxInput);
        json.put("maxOutput", maxOutput);
        json.put("minInput", minInput);
        json.put("minOutput", minOutput);
        json.put("expression", expression != null ? expression.toJSON() : null);
        return json;
    }
}
