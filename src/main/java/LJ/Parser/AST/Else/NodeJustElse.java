package LJ.Parser.AST.Else;

import LJ.Parser.AST.NodeStatement;

import java.util.ArrayList;
import java.util.List;

public class NodeJustElse extends NodeElse {
    List<NodeStatement> statementList = new ArrayList<>();

    public void addStatement(NodeStatement statement) {
        statementList.add(statement);
    }
}
