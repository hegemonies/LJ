package LJ.Parser.AST.Value;

import LJ.Lexer.Token;
import LJ.Parser.AST.Node;

public class GenericValue implements Node {
    protected Token value;

    public void setValue(Token value) {
        this.value = value;
    }

    public Token getValue() {
        return value;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        return index;
    }
}
