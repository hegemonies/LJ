package LJ.Parser.AST;

import LJ.Lexer.Token;

public class NodeElse implements Node {
    Token elseToken;
    NodeConditional conditional;

    public void setElseToken(Token elseToken) {
        this.elseToken = elseToken;
    }

    public void setConditional(NodeConditional conditional) {
        this.conditional = conditional;
    }
}
