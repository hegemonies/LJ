package LJ.Parser.AST;

import LJ.Lexer.Token;

public class NodeArgsInit implements Node {
    Token dataType;
    Token id;
    TypeInit typeInit; // var or array

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
    public String visit(String nameRootNode, int index) {
        StringBuilder sb = new StringBuilder();

        String nameNode = "\"args" + index + "\"";

        sb.append(nameNode);
        sb.append("[" + "DataType=" + dataType.getValue() + "\n");
        sb.append("id=" + id.getValue() + "\n");
        sb.append("TypeInit=" + typeInit);
        sb.append("];\n");

        sb.append(nameRootNode + " -> " + nameNode + ";");

        return sb.toString();
    }
}
