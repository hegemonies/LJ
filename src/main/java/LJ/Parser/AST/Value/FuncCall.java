package LJ.Parser.AST.Value;

import java.util.ArrayList;
import java.util.List;

public class FuncCall extends GenericValue {
    // value is id
    private List<GenericValue> argsCall = new ArrayList<>();

    public void setArgsCall(List<GenericValue> argsCall) {
        this.argsCall = argsCall;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"FuncCall%d\"", index++);

        sb.append(String.format("%s [label=\"id=%s\"];\n",
                thisNode,
                value.getValue()));

        for (GenericValue argCall : argsCall) {
            index = argCall.visit(thisNode, index, sb);
        }

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
