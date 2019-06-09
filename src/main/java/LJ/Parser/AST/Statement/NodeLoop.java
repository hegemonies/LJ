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

    public NodeExpression getExpression() {
        return expression;
    }

    public void addStatement(NodeStatement statement) {
        statementList.add(statement);
    }

    public List<NodeStatement> getStatementList() {
        return statementList;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "WHILE";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                labelNameNode,
                nameNode));

        index = expression.visit(labelNameNode, index, sb);

        for (NodeStatement statement : statementList) {
            if (statement != null) {
                index = statement.visit(labelNameNode, index, sb);
            }
        }

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
