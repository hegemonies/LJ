package LJ.Parser.AST.Inits;

import LJ.Lexer.Token;
import LJ.Parser.AST.Statement.NodeStatement;

public class NodeInit extends NodeStatement {
    private Token dataType;
    private Token id;
    private ForkInit forkInit;

    public void setDataType(Token dataType) {
        this.dataType = dataType;
    }

    public void setId(Token id) {
        this.id = id;
    }

    public void setForkInit(ForkInit forkInit) {
        this.forkInit = forkInit;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"init%d\"", index++);

        sb.append(String.format("%s [label=\"INIT\\nDataType=%s\\nid=%s\\nType=%s\"];\n",
                thisNode,
                dataType.getValue(),
                id.getValue(),
                forkInit.getType()));

        index = forkInit.visit(thisNode, index, sb);

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
