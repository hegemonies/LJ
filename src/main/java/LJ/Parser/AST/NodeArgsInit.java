package LJ.Parser.AST;

import LJ.Lexer.Token;

public class NodeArgsInit implements Node {
    private Token dataType;
    private Token id;
    private TypeInit typeInit; // var or array

    public void setDataType(Token dataType) {
        this.dataType = dataType;
    }

    public void setId(Token id) {
        this.id = id;
    }

    public void setTypeInit(TypeInit typeInit) {
        this.typeInit = typeInit;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = String.format("\"args%d\" ", index++);

        sb.append(String.format("%s [label=\"DataType=%s\nid=%s\nType=%s\"];\n",
                nameNode,
                dataType.getValue(),
                id.getValue(),
                typeInit));

        sb.append(String.format("%s -> %s;\n", rootNode, nameNode));

        return index;
    }
}
