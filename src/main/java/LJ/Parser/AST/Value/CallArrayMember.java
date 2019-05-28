package LJ.Parser.AST.Value;

import LJ.Parser.AST.ArrayMember.ArrayMember;

public class CallArrayMember extends GenericValue {
    // value is id
    ArrayMember arrayMember;

    public void setArrayMember(ArrayMember arrayMember) {
        this.arrayMember = arrayMember;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"CallArrayMember%d\"", index);

        sb.append(String.format("%s [label=\"id=%s\"];\n",
                thisNode,
                value.getValue()));

        index = arrayMember.visit(thisNode, index, sb);

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
