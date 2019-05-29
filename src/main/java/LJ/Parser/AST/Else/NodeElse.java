package LJ.Parser.AST.Else;

import LJ.Lexer.Token;
import LJ.Parser.AST.Node;

public abstract class NodeElse implements Node {
    private Token elseToken;

    public void setElseToken(Token elseToken) {
        this.elseToken = elseToken;
    }
}
