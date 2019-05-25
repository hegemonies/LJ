package LJ.Parser.AST.Statement;

import LJ.Lexer.Token;

public class NodeReturn extends NodeStatement {
    Token returnToken;
    NodeExpression expression;

    public void setReturnToken(Token returnToken) {
        this.returnToken = returnToken;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }
}
