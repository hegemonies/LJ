package LJ.Parser.AST;

import LJ.Lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Token token;
    private List<Node> children;

    public Node() {}

    public Node(Token token) {
        this.token = token;
    }

    public void addChild(Node t) {
        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(t);
    }

    private boolean isNull() {
        return token == null;
    }

    public String toString() {
        return token != null ? token.toString() : "nil";
    }

    public String toStringTree() {
        if (children == null || children.size() == 0) {
            return this.toString();
        }

        StringBuilder buf = new StringBuilder();

        if (!isNull()) {
            buf.append("(");
            buf.append(this.toString());
            buf.append(' ');
        }

        for (int i = 0; i < children.size(); i++) {
            Node t = (Node)children.get(i);
            if (i > 0) buf.append(' ');
            buf.append(t.toStringTree());
        }

        if (!isNull()) {
            buf.append(")");
        }

        return buf.toString();
    }
}
