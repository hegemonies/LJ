package LJ.Parser.AST.Inits;

import LJ.Lexer.Token;
import LJ.Parser.AST.Inits.ForkInit;
import LJ.Parser.AST.Node;
import LJ.Parser.AST.TypeInit;

public class NodeInitInsideClass implements Node {
    Token dataType;
    Token id;
    TypeInit typeInit;
    ForkInit forkInit;

    public void setDataType(Token dataType) {
        this.dataType = dataType;
    }

    public void setId(Token id) {
        this.id = id;
    }

    public void setTypeInit(TypeInit typeInit) {
        this.typeInit = typeInit;
    }

    public void setForkInit(ForkInit forkInit) {
        this.forkInit = forkInit;
    }
}
