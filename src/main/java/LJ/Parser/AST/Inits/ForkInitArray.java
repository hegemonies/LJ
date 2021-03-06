package LJ.Parser.AST.Inits;

import LJ.Lexer.Token;
import LJ.Parser.AST.ArrayMember.ArrayMember;
import LJ.Parser.AST.TypeInit;

public class ForkInitArray extends ForkInit {
    private Token dataType;
    private ArrayMember index;

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

    @Override
    public int visit(String rootName, int indexNode, StringBuilder sb) {
        String nameNode = "ARRAY";
        String labelNameNode = String.format("\"%s%d\"",
                "ARRAY",
                indexNode++);

        sb.append(String.format("%s [label=\"%s\\nDataType=%s\"];\n",
                labelNameNode,
                nameNode,
                dataType.getValue()));

        indexNode = this.index.visit(labelNameNode, indexNode, sb);

        sb.append(String.format("%s -> %s;\n", rootName, labelNameNode));

        return indexNode;
    }
}
