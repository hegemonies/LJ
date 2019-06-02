package LJ.IdentifierTable;

import LJ.IdentifierTable.CustomerException.SemanticException;
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
import java.util.Set;
import java.util.TreeSet;

public class Table implements GenericUnit {
    private Map<String, GenericUnit> mainTable = new HashMap<>();

    public Map<String, GenericUnit> getMainTable() {
        return mainTable;
    }

    public void go(NodeClass root) throws SemanticException {
        next(root);
    }

    void next(Node node) throws SemanticException {
        if (node instanceof NodeClass) {
            for (NodeInit nodeInit : ((NodeClass) node).getInitList()) {
                addInitNode(nodeInit);
            }

            Table newTable = new Table();
            newTable.next(((NodeClass) node).getMainMethod());
            mainTable.put("MainMethod", newTable);
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
    }

    private void addInitNode(NodeInit nodeInit) throws SemanticException {
        ForkInit tmpForkInit = nodeInit.getForkInit();

        if (tmpForkInit instanceof ForkInitVar ||
                tmpForkInit instanceof ForkInitArray) {
            String tmpNameID = nodeInit.getId().getValue();

            if (!containsKey(this, tmpNameID)) {
                mainTable.put(tmpNameID, new IDUnit(nodeInit));
            } else {
                throw new SemanticException();
            }
        } else if (tmpForkInit instanceof ForkInitFunc) {
            String tmpNameID = nodeInit.getId().getValue();

            if (!containsKey(this, tmpNameID)) {
                Table newTable = new Table();
                newTable.next(tmpForkInit);
                mainTable.put(tmpNameID, newTable);
            } else {
                throw new SemanticException();
            }
        }
    }

    private boolean containsKey(Table table, String ckey) {
        for (String key : table.getMainTable().keySet()) {
            GenericUnit gu = table.getMainTable().get(key);
            if (gu instanceof IDUnit && key.equals(ckey)) {
                return false;
            } else if (gu instanceof Table) {
                ((Table) gu).containsKey((Table) gu, ckey);
            }
        }

        return true;
    }

    private void addStatement(NodeStatement statement) throws SemanticException {
        if (statement instanceof NodeInit) {
            addInitNode((NodeInit) statement);
        } else if (statement instanceof NodeConditional ||
                statement instanceof NodeLoop) {
            Table newTable = new Table();
            newTable.next(statement);
            mainTable.put(statement.toString(), newTable);
        }
    }

    public void printTable() {
        printTable(this, "Global");
    }

    private void printTable(Table table, String nameTable) {
        System.out.println(String.format("\n\tTable %s:",
                nameTable));

        Set<String> table_keys = new TreeSet<>();

        for (String key : table.getMainTable().keySet()) {
            if (table.getMainTable().get(key) instanceof IDUnit) {
                System.out.println(String.format("%s %s",
                        key,
                        table.getMainTable().get(key)));
            } else if (table.getMainTable().get(key) instanceof Table) {
                System.out.println(String.format("%s %s",
                        key,
                        table.getMainTable().get(key)));
                table_keys.add(key);
            }
        }

        for (String table_id : table_keys) {
            printTable((Table) table.getMainTable().get(table_id), table_id);
        }
    }
}
