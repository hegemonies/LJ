package LJ.IdentifierTable;

import LJ.Parser.AST.NodeArgsInit;

public class ArgID implements GenericUnit {
    private NodeArgsInit node;

    public ArgID(NodeArgsInit node) {
        this.node = node;
    }

    public NodeArgsInit getNode() {
        return node;
    }
}
