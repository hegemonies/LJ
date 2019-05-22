package LJ.Parser.AST.ArrayMember;

import LJ.Lexer.Token;

public class ArrayMemberID extends ArrayMember {
    Token id;

    public ArrayMemberID(Token id) {
        this.id = id;
    }
}
