package LJ.Parser.AST.Value;

import LJ.Lexer.Token;
import LJ.Parser.AST.ArrayMember.ArrayMember;

public class CallArrayMember extends GenericValue {
    // value is id
    ArrayMember arrayMember;

    public void setArrayMember(ArrayMember arrayMember) {
        this.arrayMember = arrayMember;
    }
}
