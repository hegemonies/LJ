package LJ.Parser;

import LJ.Parser.AST.Inits.ForkInitArray;
import LJ.Parser.AST.Inits.ForkInitFunc;
import LJ.Parser.AST.Inits.ForkInitVar;
import LJ.Parser.AST.Inits.NodeInit;
import LJ.Parser.AST.Node;
import LJ.Parser.AST.NodeClass;
import LJ.Parser.AST.NodeMainMethod;


/**
 * Wrapper for bypass AST
 */
public class WrapperAST {
    private NodeClass root;
    private Node currentNode;

    public WrapperAST(NodeClass root) {
        this.root = root;
        currentNode = root;
    }

    Node getID() {
        currentNode = next(currentNode);
        return currentNode;
    }

    Node next(Node node) {
        if (node instanceof NodeClass) {
            for (NodeInit nodeInit : ((NodeClass) node).getInitList()) {
                isNeedNode(nodeInit);
            }

            isNeedNode(((NodeClass) node).getMainMethod());
        } else if (node instanceof NodeInit) {

        } else if (node instanceof NodeMainMethod) {

        }
    }

    boolean isNeedNode(Node node) {
        if (node instanceof ForkInitArray) {
            return true;
        } else if (node instanceof ForkInitFunc) {
            return true;
        } else if (node instanceof ForkInitVar) {
            return true;
        } else {
            return node instanceof NodeInit;
        }
    }
}
