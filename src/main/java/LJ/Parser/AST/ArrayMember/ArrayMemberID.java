package LJ.Parser.AST.ArrayMember;

import LJ.Lexer.Token;

public class ArrayMemberID extends ArrayMember {
    private Token id;

    public ArrayMemberID(Token id) {
        this.id = id;
    }

    public Token getId() {
        return id;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"id%d\"", index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                thisNode,
                id.getValue()));

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
