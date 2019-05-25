package LJ.Parser.AST.Statement;

import LJ.Lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class NodeLoop extends NodeStatement {
    private Token whileToken;
    private NodeExpression expression;
    private List<NodeStatement> statementList = new ArrayList<>();

    public void setWhileToken(Token whileToken) {
        this.whileToken = whileToken;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    public void addStatement(NodeStatement statement) {
        statementList.add(statement);
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"WHILE%d\"", index++);

        index = expression.visit(thisNode, index, sb);

        for (NodeStatement statement : statementList) {
            index = statement.visit(thisNode, index, sb);
        }

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
