package LJ.Parser.AST;

public interface Node {
    int visit(String rootNode, int index, StringBuilder sb);
}
