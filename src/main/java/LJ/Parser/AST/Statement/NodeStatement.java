package LJ.Parser.AST.Statement;

public abstract class NodeStatement {
    abstract public int visit(String rootNode, int index, StringBuilder sb);
}
