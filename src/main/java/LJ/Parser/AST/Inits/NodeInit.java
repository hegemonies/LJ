package LJ.Parser.AST.Inits;

import LJ.Lexer.Token;
import LJ.Parser.AST.Inits.ForkInit;
import LJ.Parser.AST.Node;
import LJ.Parser.AST.NodeStatement;
import LJ.Parser.AST.TypeInit;

public class NodeInit extends NodeStatement {
    Token dataType;
    Token id;
    ForkInit forkInit;

    public void setDataType(Token dataType) {
        this.dataType = dataType;
    }

    public void setId(Token id) {
        this.id = id;
    }

    public void setForkInit(ForkInit forkInit) {
        this.forkInit = forkInit;
    }
}
