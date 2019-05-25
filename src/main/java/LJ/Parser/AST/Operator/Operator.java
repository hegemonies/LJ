package LJ.Parser.AST.Operator;

import LJ.Lexer.Token;

public class Operator {
    private Token value;

    public Operator(Token value) {
        this.value = value;
    }

    public Token getValue() {
        return value;
    }
}
