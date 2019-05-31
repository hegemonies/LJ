package LJ.IdentifierTable;

import LJ.Parser.AST.Inits.NodeInit;

class IDUnit implements GenericUnit {
    private NodeInit node;

    IDUnit(NodeInit node) {
        this.node = node;
    }

    public NodeInit getNode() {
        return node;
    }
}
