package LJ.Parser.AST.Inits;

import LJ.Lexer.Token;
import LJ.Parser.AST.Inits.ForkInit;
import LJ.Parser.AST.Node;
import LJ.Parser.AST.NodeStatement;
import LJ.Parser.AST.TypeInit;

public class NodeInit extends NodeStatement {
    Token dataType;
    Token id;
    ForkInit forkInit;

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
    public String visit(int index) {
        StringBuilder sb = new StringBuilder();

        String nameNode = "\"init" + index + "\"";

        sb.append(nameNode);
        sb.append("[label=\"");
        sb.append("DataType=" + dataType.getValue() + "\n");
        sb.append("id=" + id.getValue() + "\n");
        sb.append("Type=" + forkInit.getType() + "\n");
        sb.append("];");



        return sb.toString();
    }
}
