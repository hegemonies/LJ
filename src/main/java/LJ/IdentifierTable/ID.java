package LJ.IdentifierTable;

import LJ.Parser.AST.Inits.NodeInit;

class ID implements GenericUnit {
    private NodeInit node;

    ID(NodeInit node) {
        this.node = node;
    }

    public NodeInit getNode() {
        return node;
    }
}
