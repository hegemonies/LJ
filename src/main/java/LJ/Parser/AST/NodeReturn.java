package LJ.Parser.AST;

import LJ.Lexer.Token;

public class NodeReturn implements Node {
    Token returnToken;
    NodeExpression expression;

    public void setReturnToken(Token returnToken) {
        this.returnToken = returnToken;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }
}
