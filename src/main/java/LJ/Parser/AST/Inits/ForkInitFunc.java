package LJ.Parser.AST.Inits;

import LJ.Parser.AST.NodeArgsInit;
import LJ.Parser.AST.Statement.NodeStatement;
import LJ.Parser.AST.TypeInit;

import java.util.ArrayList;
import java.util.List;

public class ForkInitFunc extends ForkInit {
    private List<NodeArgsInit> nodeArgsInitList = new ArrayList<>(); // optional
    private List<NodeStatement> statementList = new ArrayList<>(); // too

    public void addArgInit(NodeArgsInit node) {
        nodeArgsInitList.add(node);
    }

    public void addStatement(NodeStatement node) {
        statementList.add(node);
    }

    @Override
    public void chooseType() {
        type = TypeInit.FUNC;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        if (nodeArgsInitList.size() > 0) {
            String argsNode = String.format("\"%s%d\"", "ARGS", index++);

            for (NodeArgsInit nodeArgsInit : nodeArgsInitList) {
                index = nodeArgsInit.visit(argsNode, index++, sb);
            }

            sb.append(String.format("%s -> %s;\n", rootNode, argsNode));
        }

        if (statementList.size() > 0) {
            String statementNode = String.format("\"%s%d\"", "STATEMENTS", index++);

            for (NodeStatement statement : statementList) {
                index = statement.visit(statementNode, index++, sb);
            }

            sb.append(String.format("%s -> %s;\n", rootNode, statementNode));
        }

        return index;
    }
}
