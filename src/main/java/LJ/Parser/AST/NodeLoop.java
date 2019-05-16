package LJ.Parser.AST;

import LJ.Lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class NodeLoop {
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
}
