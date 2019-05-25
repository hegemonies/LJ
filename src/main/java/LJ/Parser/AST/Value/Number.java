package LJ.Parser.AST.Value;

public class Number extends GenericValue {
    boolean isNegative;

    public void setNegative(boolean isNegative) {
        this.isNegative = isNegative;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"number%d\"", index++);

        sb.append(String.format("%s [label=\"%s%s\"];\n",
                thisNode,
                isNegative ? "-" : "",
                getValue()));

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
