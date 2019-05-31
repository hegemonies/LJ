package LJ.IdentifierTable;

import LJ.Parser.AST.Inits.*;
import LJ.Parser.AST.Node;
import LJ.Parser.AST.NodeClass;

import java.util.HashMap;
import java.util.Map;

/**
 * todo may be need remove WrapperAST
 */

public class Table {
    private Map<String, GenericUnit> mainTable = new HashMap<>();

    public void go(NodeClass root) {
        next(root);
    }

    Map<String, GenericUnit> next(Node node) { // todo need to end this method
        if (node instanceof NodeClass) {
            for (NodeInit nodeInit : ((NodeClass) node).getInitList()) {
                if (nodeInit.getForkInit() != null) {
                    ForkInit tmpForkInit = nodeInit.getForkInit();

                    if (tmpForkInit instanceof ForkInitVar ||
                            tmpForkInit instanceof ForkInitArray) {
                        mainTable.put(nodeInit.getId().getValue(), new IDUnit(nodeInit));
                    } else if (tmpForkInit instanceof ForkInitFunc) {
                        Table newTable = new Table();
                        mainTable.put(nodeInit.getId().getValue(), new TableUnit(newTable.next(nodeInit)));
                    }
                } else {
                    mainTable.put(nodeInit.getId().getValue(), new IDUnit(nodeInit));
                }
            }
        } else {

        }

        return mainTable;
    }
}
