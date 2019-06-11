package LJ.Parser.AST.Statement;

import LJ.Lexer.Token;

public class NodeScanln extends NodeStatement {
    private Token id;

    public void setId(Token id) {
        this.id = id;
    }

    public Token getId() {
        return id;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "SCANLN";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                labelNameNode,
                nameNode));

        String idNameNode = "ID";
        String idLabelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                labelNameNode,
                nameNode));

        sb.append(String.format("%s -> %s;\n", labelNameNode, idLabelNameNode));

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
