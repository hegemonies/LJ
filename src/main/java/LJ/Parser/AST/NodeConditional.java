package LJ.Parser.AST;

import LJ.Lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class NodeConditional implements Node {
    Token ifToken;
    NodeExpression expression;
    List<Node> statementList = new ArrayList<>();
    NodeElse elseNode;

    public void setIfToken(Token ifToken) {
        this.ifToken = ifToken;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    public void setElseNode(NodeElse elseNode) {
        this.elseNode = elseNode;
    }

    public void addStatement(Node statement) {
        statementList.add(statement);
    }
}
