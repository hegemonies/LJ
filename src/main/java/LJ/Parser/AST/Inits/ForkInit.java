package LJ.Parser.AST.Inits;

import LJ.Parser.AST.TypeInit;

public abstract class ForkInit {
    TypeInit type;
    public abstract void chooseType();
}
