package LJ.Parser.AST;

import LJ.Lexer.Token;

public class NodeStatementAroundBrace extends NodeStatement {
    Token l_brace;
    NodeStatement statement;
    Token r_brace;

    public void setL_brace(Token l_brace) {
        this.l_brace = l_brace;
    }

    public void setStatement(NodeStatement statement) {
        this.statement = statement;
    }

    public void setR_brace(Token r_brace) {
        this.r_brace = r_brace;
    }
}
