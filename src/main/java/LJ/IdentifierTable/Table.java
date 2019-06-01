package LJ.IdentifierTable;

import LJ.Parser.AST.Else.NodeJustElse;
import LJ.Parser.AST.Inits.*;
import LJ.Parser.AST.Node;
import LJ.Parser.AST.NodeClass;
import LJ.Parser.AST.NodeMainMethod;
import LJ.Parser.AST.Statement.NodeConditional;
import LJ.Parser.AST.Statement.NodeLoop;
import LJ.Parser.AST.Statement.NodeStatement;

import java.util.HashMap;
import java.util.Map;


/**
 * TODO add a method to print a table. I wonder how it will be?
 */

public class Table implements GenericUnit {
    private Map<String, GenericUnit> mainTable = new HashMap<>();

    public Map<String, GenericUnit> getMainTable() {
        return mainTable;
    }

    public void go(NodeClass root) {
        next(root);
    }

    Map<String, GenericUnit> next(Node node) { // todo may be it work
        if (node instanceof NodeClass) {
            for (NodeInit nodeInit : ((NodeClass) node).getInitList()) {
                addInitNode(nodeInit);
            }
            next(((NodeClass) node).getMainMethod());
        } else if (node instanceof ForkInitFunc) {
            for (NodeStatement statement : ((ForkInitFunc) node).getStatementList()) {
                addStatement(statement);
            }
        } else if (node instanceof NodeConditional) {
            for (NodeStatement statement : ((NodeConditional) node).getStatementList()) {
                addStatement(statement);
            }

            if (((NodeConditional) node).getElseNode() != null) {
                next(((NodeConditional) node).getElseNode());
            }
        } else if (node instanceof NodeLoop) {
            for (NodeStatement statement : ((NodeLoop) node).getStatementList()) {
                addStatement(statement);
            }
        } else if (node instanceof NodeJustElse) {
            for (NodeStatement statement : ((NodeJustElse) node).getStatementList()) {
                addStatement(statement);
            }
        } else if (node instanceof NodeMainMethod) {
            for (NodeStatement statement : ((NodeMainMethod) node).getStatementList()) {
                addStatement(statement);
            }
        }

        return mainTable;
    }

    private void addInitNode(NodeInit nodeInit) {
        if (nodeInit.getForkInit() != null) {
            ForkInit tmpForkInit = nodeInit.getForkInit();

            if (tmpForkInit instanceof ForkInitVar ||
                    tmpForkInit instanceof ForkInitArray) {
                mainTable.put(nodeInit.getId().getValue(), new IDUnit(nodeInit));
            } else if (tmpForkInit instanceof ForkInitFunc) {
                Table newTable = new Table();
                newTable.next(tmpForkInit);
                mainTable.put(nodeInit.getId().getValue(), newTable);
            }
        } else {
            mainTable.put(nodeInit.getId().getValue(), new IDUnit(nodeInit));
        }
    }

    private void addStatement(NodeStatement statement) {
        if (statement instanceof NodeInit) {
            addInitNode((NodeInit) statement);
        } else if (statement instanceof NodeConditional ||
                statement instanceof NodeLoop) {
            next(statement);
        }
    }

    public void printTable() {
        printTable(this);
    }

    private void printTable(Table table) {
        System.out.println("\n\tIdentifier Table:");

        for (String key : table.getMainTable().keySet()) {
            if (table.getMainTable().get(key) instanceof IDUnit) {
                System.out.println(String.format("%s %s",
                        key,
                        table.getMainTable().get(key)));
            } else if (table.getMainTable().get(key) instanceof Table) {
                printTable((Table) table.getMainTable().get(key));
            }
        }
    }
}
