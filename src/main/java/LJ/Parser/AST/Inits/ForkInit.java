package LJ.Parser.AST.Inits;

import LJ.Parser.AST.Node;
import LJ.Parser.AST.TypeInit;

public abstract class ForkInit implements Node {
    TypeInit type;
    public abstract void chooseType();
    public abstract int visit(String rootName, int index, StringBuilder sb);

    public TypeInit getType() {
        return type;
    }
}
