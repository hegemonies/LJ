package LJ.Parser.AST;

import LJ.Lexer.Token;

public class NodeNumber implements Node {
    boolean isPositive;
    Token number;

    public void setPositive(boolean positive) {
        isPositive = positive;
    }

    public void setNumber(Token number) {
        this.number = number;
    }
}
