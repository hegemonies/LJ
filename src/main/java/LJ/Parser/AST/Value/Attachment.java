package LJ.Parser.AST.Value;

import LJ.Parser.AST.Statement.NodeExpression;

public class Attachment extends GenericValue {
    private NodeExpression expression;

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "ATTACHMENT";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                labelNameNode,
                nameNode));

        index = expression.visit(labelNameNode, index, sb);

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
