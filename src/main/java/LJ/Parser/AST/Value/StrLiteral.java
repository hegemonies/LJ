package LJ.Parser.AST.Value;

public class StrLiteral extends GenericValue {
    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"StrLiteral%d\"", index++);

        sb.append(String.format("%s [label=\"id=\"];\n", value.getValue()));

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
