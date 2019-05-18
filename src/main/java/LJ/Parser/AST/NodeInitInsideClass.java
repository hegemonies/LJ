package LJ.Parser.AST;

import LJ.Lexer.Token;

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
