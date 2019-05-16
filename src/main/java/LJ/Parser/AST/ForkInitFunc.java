package LJ.Parser.AST;

import java.util.ArrayList;
import java.util.List;

public class ForkInitFunc implements ForkInit {
    List<NodeArgsInit> nodeArgsInitList = new ArrayList<>(); // optional
    List<NodeStatement> statementList = new ArrayList<>(); // too

    public void addArgInit(NodeArgsInit node) {
        nodeArgsInitList.add(node);
    }

    public void addStatement(NodeStatement node) {
        statementList.add(node);
    }
}
