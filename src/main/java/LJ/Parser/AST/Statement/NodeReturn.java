package LJ.Parser.AST.Statement;

import LJ.Lexer.Token;

public class NodeReturn extends NodeStatement {
    private Token returnToken;
    private NodeExpression expression;

    public void setReturnToken(Token returnToken) {
        this.returnToken = returnToken;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"RETURN%d\"", index++);

        index = expression.visit(thisNode, index, sb);

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
