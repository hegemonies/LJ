package LJ.IdentifierTable;

import LJ.IdentifierTable.CustomerException.SemanticException;
import LJ.Parser.AST.ArrayMember.ArrayMember;
import LJ.Parser.AST.ArrayMember.ArrayMemberID;
import LJ.Parser.AST.Else.NodeJustElse;
import LJ.Parser.AST.Inits.*;
import LJ.Parser.AST.Node;
import LJ.Parser.AST.NodeArgsInit;
import LJ.Parser.AST.NodeClass;
import LJ.Parser.AST.NodeMainMethod;
import LJ.Parser.AST.Statement.*;
import LJ.Parser.AST.Value.Attachment;
import LJ.Parser.AST.Value.CallArrayMember;
import LJ.Parser.AST.Value.FuncCall;
import LJ.Parser.AST.Value.GenericValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Table implements GenericUnit {
    private String nameTable;
    private Map<String, GenericUnit> mainTable = new HashMap<>();
    private Table parentTable = null;

    public void setParentTable(Table parentTable) {
        this.parentTable = parentTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public Map<String, GenericUnit> getMainTable() {
        return mainTable;
    }

    public void go(NodeClass root) throws SemanticException {
        nameTable = "GLOBAL_TABLE";
        next(root);
    }

    void next(Node node) throws SemanticException {
        if (node instanceof NodeClass) {
            for (NodeInit nodeInit : ((NodeClass) node).getInitList()) {
                addInitNode(nodeInit);
            }

            Table newTable = new Table();
            newTable.setNameTable("MAIN_METHOD");
            newTable.setParentTable(this);
            newTable.next(((NodeClass) node).getMainMethod());
            mainTable.put("MainMethod", newTable);
        } else if (node instanceof ForkInitFunc) {
            for (NodeArgsInit nodeArgsInit : ((ForkInitFunc) node).getNodeArgsInitList()) {
                addArgInitNode(nodeArgsInit);
            }
            for (NodeStatement statement : ((ForkInitFunc) node).getStatementList()) {
                if (statement != null) {
                    addStatement(statement);
                }
            }
        } else if (node instanceof NodeConditional) {
            for (NodeStatement statement : ((NodeConditional) node).getStatementList()) {
                if (statement != null) {
                    addStatement(statement);
                }
            }

            if (((NodeConditional) node).getElseNode() != null) {
                next(((NodeConditional) node).getElseNode());
            }
        } else if (node instanceof NodeLoop) {
            for (NodeStatement statement : ((NodeLoop) node).getStatementList()) {
                if (statement != null) {
                    addStatement(statement);
                }
            }
        } else if (node instanceof NodeJustElse) {
            for (NodeStatement statement : ((NodeJustElse) node).getStatementList()) {
                if (statement != null) {
                    addStatement(statement);
                }
            }
        } else if (node instanceof NodeMainMethod) {
            for (NodeStatement statement : ((NodeMainMethod) node).getStatementList()) {
                if (statement != null) {
                    addStatement(statement);
                }
            }
        }
    }

    private void addArgInitNode(NodeArgsInit nodeArgsInit) throws SemanticException {
        String tmpNameID = nodeArgsInit.getId().getValue();
        if (!containsKey(tmpNameID)) {
            mainTable.put(tmpNameID, new ArgID(nodeArgsInit));
        } else {
            throw new SemanticException(String.format("%s already init in talbe <%s>",
                    tmpNameID,
                    nameTable));
        }
    }

    private void addInitNode(NodeInit nodeInit) throws SemanticException {
        ForkInit tmpForkInit = nodeInit.getForkInit();

        if (tmpForkInit instanceof ForkInitVar ||
                tmpForkInit instanceof ForkInitArray) {
            String tmpNameID = nodeInit.getId().getValue();

            if (!containsKey(tmpNameID)) {
                mainTable.put(tmpNameID, new ID(nodeInit));
            } else {
                throw new SemanticException(String.format("%s already init in talbe <%s>",
                                                    tmpNameID,
                                                    nameTable));
            }
        } else if (tmpForkInit instanceof ForkInitFunc) {
            String tmpNameID = nodeInit.getId().getValue();

            if (!containsKey(tmpNameID)) {
                Table newTable = new Table();
                newTable.setNameTable(tmpNameID);
                newTable.setParentTable(this);
                newTable.next(tmpForkInit);
                mainTable.put(tmpNameID, newTable);
            } else {
                throw new SemanticException(String.format("%s already init in table <%s>",
                                                            tmpNameID,
                                                            nameTable));
            }
        }
    }

    private boolean containsKey(String ckey) {
        for (String key : mainTable.keySet()) {
            GenericUnit gu = mainTable.get(key);

            if (key.equals(ckey)) {
                return true;
            }
        }

        boolean result = false;

        if (parentTable != null) {
            result = parentTable.containsKey(ckey);
        }

        return result;
    }

    private void addStatement(NodeStatement statement) throws SemanticException {
        if (statement instanceof NodeInit) {
            addInitNode((NodeInit) statement);
        } else {
            if (statement instanceof NodeConditional ||
                    statement instanceof NodeLoop) {
                Table newTable = new Table();
                newTable.setNameTable(statement.toString()
                                        .split("@")[0]);
                newTable.setParentTable(this);
                newTable.next(statement);
                mainTable.put(statement.toString(), newTable);
            }

            if (!checkExpression(statement)) {
                throw new SemanticException(String.format("%s not init in table <%s>",
                                                        statement,
                                                        nameTable));
            }
        }
    }

    private boolean checkExpression(NodeStatement statement) {
        boolean result = false;
        NodeExpression expr = null;

        if (statement instanceof NodeLoop) {
            expr = ((NodeLoop) statement).getExpression();
        } else if (statement instanceof NodeConditional) {
            expr = ((NodeConditional) statement).getExpression();
        } else if (statement instanceof NodeReturn) {
            expr = ((NodeReturn) statement).getExpression();
        } else if (statement instanceof NodePrintln) {
            expr = ((NodePrintln) statement).getExpression();
        } else if (statement instanceof NodeExpression) {
            expr = (NodeExpression) statement;
        }

        if (expr != null) {
            GenericValue gv;

            if ((gv = expr.getlValue()) != null) {
                if (!gv.getValue().getType().equals("str_literal") &&
                        !gv.getValue().getType().equals("numeric_constant")) {
                    result = containsKey(gv.getValue().getValue());
                }
                result = checkGenericValue(gv);
            }
            if ((gv = expr.getrValue()) != null) {
                if (!gv.getValue().getType().equals("str_literal") &&
                        !gv.getValue().getType().equals("numeric_constant")) {
                    result = containsKey(gv.getValue().getValue());
                }
                result = checkGenericValue(gv);
            }

            NodeExpression tmpExpr;
            if ((tmpExpr = expr.getlExpression()) != null) {
                result = checkExpression(tmpExpr);
            }
            if ((tmpExpr = expr.getrExpression()) != null) {
                result = checkExpression(tmpExpr);
            }
        }

        return result;
    }

    private boolean checkGenericValue(GenericValue gv) {
        boolean result = false;

        if (gv instanceof Attachment) {
            result = checkExpression(((Attachment) gv).getExpression());
        } else if (gv instanceof CallArrayMember) {
            ArrayMember am = ((CallArrayMember) gv).getArrayMember();

            if (am instanceof ArrayMemberID) {
                result = containsKey(((ArrayMemberID) am).getId().getValue());
            }
        } else if (gv instanceof FuncCall) {
            for (GenericValue genericValue : ((FuncCall) gv).getArgsCall()) {
                result = checkGenericValue(genericValue);
            }
        } else if (gv != null) {
            result = true;
        }

        return result;
    }

    public void printTable() {
        printTable(this, "GLOBAL_TABLE");
    }

    private void printTable(Table table, String nameTable) {
        System.out.println(String.format("\n\tTable %s:",
                nameTable));

        Set<String> table_keys = new TreeSet<>();

        for (String key : table.getMainTable().keySet()) {
            if (table.getMainTable().get(key) instanceof ID ||
                    table.getMainTable().get(key) instanceof ArgID) {
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
