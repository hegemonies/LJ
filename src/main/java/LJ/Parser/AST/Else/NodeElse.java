package LJ.Parser.AST.Else;

import LJ.Lexer.Token;
import LJ.Parser.AST.Node;

public class NodeElse implements Node {
    Token elseToken;

    public void setElseToken(Token elseToken) {
        this.elseToken = elseToken;
    }
}
