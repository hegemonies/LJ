package LJ.Parser.AST.Statement;

import LJ.Lexer.Token;
import LJ.Parser.AST.Else.NodeElse;

import java.util.ArrayList;
import java.util.List;

public class NodeConditional extends NodeStatement {
    private Token ifToken;
    private NodeExpression expression;
    private List<NodeStatement> statementList = new ArrayList<>();
    private NodeElse elseNode;

    public void setIfToken(Token ifToken) {
        this.ifToken = ifToken;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    public void setElseNode(NodeElse elseNode) {
        this.elseNode = elseNode;
    }

    public void addStatement(NodeStatement statement) {
        statementList.add(statement);
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"%s%d\"", "IF", index++);

        sb.append(String.format("%s [label=\"IF\"];\n", thisNode));

        index = expression.visit(thisNode, index, sb);

        for (NodeStatement statement : statementList) {
            index = statement.visit(thisNode, index, sb);
        }

        index = elseNode.visit(thisNode, index, sb);

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
