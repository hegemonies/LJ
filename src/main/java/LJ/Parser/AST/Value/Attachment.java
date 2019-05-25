package LJ.Parser.AST.Value;

import LJ.Parser.AST.Statement.NodeExpression;

public class Attachment extends GenericValue {
    private NodeExpression expression;

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"=%d\"", index++);

        index = expression.visit(thisNode, index, sb);

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
