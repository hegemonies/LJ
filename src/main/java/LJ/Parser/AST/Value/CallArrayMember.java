package LJ.Parser.AST.Value;

import LJ.Parser.AST.ArrayMember.ArrayMember;

public class CallArrayMember extends GenericValue { // todo refactor
    // value is id
    ArrayMember arrayMember;

    public void setArrayMember(ArrayMember arrayMember) {
        this.arrayMember = arrayMember;
    }
}
