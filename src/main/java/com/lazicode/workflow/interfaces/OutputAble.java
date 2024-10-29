
package com.lazicode.workflow.interfaces;

import java.util.Set;

import com.lazicode.workflow.node.Node;

public interface OutputAble {
    Set<Node> getConnections();
}
