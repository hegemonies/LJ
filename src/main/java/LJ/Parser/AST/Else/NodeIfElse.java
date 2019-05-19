package LJ.Parser.AST.Else;

import LJ.Parser.AST.NodeConditional;

public class NodeIfElse extends NodeElse {
    NodeConditional conditional;

    public void setConditional(NodeConditional conditional) {
        this.conditional = conditional;
    }
}
