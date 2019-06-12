package LJ.Parser.AST.Value;

import LJ.Lexer.Token;
import LJ.Parser.AST.Node;

public class GenericValue implements Node {
    protected Token value;


    public void setValue(Token value) {
        this.value = value;
    }

    public Token getValue() {
        return value;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "ID";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\\n%s\"];\n",
                labelNameNode,
                nameNode,
                getValue().getValue()));

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
