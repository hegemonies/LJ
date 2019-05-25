package LJ.Parser.AST.ArrayMember;

import LJ.Parser.AST.Value.Number;

public class ArrayMemberNumber extends ArrayMember {
    private Number numberMember;

    public ArrayMemberNumber(Number parseNumber) {
        this.numberMember = numberMember;
    }

    public Number getNumberMember() {
        return numberMember;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        index = numberMember.visit(rootNode, index, sb);

        return index;
    }
}
