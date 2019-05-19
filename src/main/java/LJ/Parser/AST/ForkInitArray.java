package LJ.Parser.AST;

import LJ.Lexer.Token;

public class ForkInitArray extends ForkInit {
    Token dataType;
    Token index;

    public void setDataType(Token dataType) {
        this.dataType = dataType;
    }

    public void setIndex(Token index) {
        this.index = index;
    }

    @Override
    public void chooseType() {
        type = TypeInit.ARRAY;
    }
}
