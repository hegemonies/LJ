package LJ.Parser.AST.Statement;

public class NodePrintln extends NodeStatement {
    private NodeExpression expression;

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "PRINTLN";
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
