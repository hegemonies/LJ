package LJ.Parser.AST;

import LJ.Lexer.Token;

public class NodeArgsInit implements Node {
    Token dataType;
    Token id;
    TypeInit typeInit; // var or array

    public void setDataType(Token dataType) {
        this.dataType = dataType;
    }

    public void setId(Token id) {
        this.id = id;
    }

    public void setTypeInit(TypeInit typeInit) {
        this.typeInit = typeInit;
    }
}
