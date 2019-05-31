package LJ.Parser.AST.Statement;

import LJ.Parser.AST.Node;

public abstract class NodeStatement implements Node {
    abstract public int visit(String rootNode, int index, StringBuilder sb);
}
