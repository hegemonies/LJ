package LJ.Parser.AST;

import LJ.Lexer.Token;

public class ForkInitArray implements ForkInit {
    Token dataType;
    Token index;

    public void setDataType(Token dataType) {
        this.dataType = dataType;
    }

    public void setIndex(Token index) {
        this.index = index;
    }
}
