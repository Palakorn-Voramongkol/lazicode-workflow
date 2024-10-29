
package com.lazicode.workflow.interfaces;

import com.lazicode.workflow.node.Node;

public interface InputAble {
    void connect(Node otherNode);
    void disconnect(Node otherNode);
}
