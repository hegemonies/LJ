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
        String thisNode = String.format("\"ARRAY%d\"", indexNode++);
        sb.append(String.format("%s [label=\"DataType=%s\"];\n",
                thisNode,
                dataType.getValue()));

        indexNode = this.index.visit(thisNode, indexNode, sb);

        sb.append(String.format("%s -> %s;\n", rootName, thisNode));

        return indexNode;
    }
}
