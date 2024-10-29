package com.lazicode.workflow.interfaces;

import com.lazicode.workflow.node.Node;
import java.util.LinkedHashSet;

/**
 * Interface defining container capabilities for nodes that can hold child nodes.
 * Maintains the sequence of nodes.
 */
public interface ContainerAble {
    /**
     * Adds a child node to this container.
     *
     * @param node The child node to add.
     */
    void addNode(Node node);

    /**
     * Removes a child node from this container.
     *
     * @param node The child node to remove.
     */
    void removeNode(Node node);

    /**
     * Retrieves all child nodes of this container in sequence.
     *
     * @return A LinkedHashSet of child nodes.
     */
    LinkedHashSet<Node> getNodes();

    /**
     * Checks if the container has any child nodes.
     *
     * @return True if there are child nodes; otherwise, false.
     */
    boolean hasNodes();
}
