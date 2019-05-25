package LJ.Parser.AST.Else;

import LJ.Parser.AST.Statement.NodeConditional;

public class NodeIfElse extends NodeElse {
    private NodeConditional conditional;

    public void setConditional(NodeConditional conditional) {
        this.conditional = conditional;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"ELSE%d\"", index++);

        index = conditional.visit(thisNode, index, sb);

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
