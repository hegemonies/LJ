package LJ.Parser.AST.Else;

import LJ.Parser.AST.Statement.NodeStatement;

import java.util.ArrayList;
import java.util.List;

public class NodeJustElse extends NodeElse {
    List<NodeStatement> statementList = new ArrayList<>();

    public void addStatement(NodeStatement statement) {
        statementList.add(statement);
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"ELSE%d\"", index++);

        for (NodeStatement statement : statementList) {
            index = statement.visit(thisNode, index, sb);
        }

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
