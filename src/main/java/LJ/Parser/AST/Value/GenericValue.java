package LJ.Parser.AST.Value;

import LJ.Lexer.Token;
import LJ.Parser.AST.Node;

public class GenericValue implements Node {
    private Token value;

    public void setValue(Token value) {
        this.value = value;
    }
}
