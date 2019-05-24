package LJ.Parser.AST.Inits;

import LJ.Lexer.Token;
import LJ.Parser.AST.ArrayMember.ArrayMember;
import LJ.Parser.AST.TypeInit;

public class ForkInitArray extends ForkInit {
    Token dataType;
    ArrayMember index;

    public void setDataType(Token dataType) {
        this.dataType = dataType;
    }

    public void setIndex(ArrayMember index) {
        this.index = index;
    }

    @Override
    public void chooseType() {
        type = TypeInit.ARRAY;
    }
}
