package LJ.Parser.AST;

import LJ.Lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {
    private Token token = null;
    private List<ASTNode> children;
    private ASTNodeTypes nodeType;

    public ASTNode() {}

    public ASTNode(Token token) {
        this.token = token;
    }

    public ASTNode(Token token, ASTNodeTypes nodeType) {
        this.token = token;
        this.nodeType = nodeType;
    }

    public ASTNode(ASTNodeTypes nodeType) {
        this.nodeType = nodeType;
    }

    public void addChild(ASTNode t) {
        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(t);
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public boolean isNull() {
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
            buf.append('\t');
        }

        for (int i = 0; i < children.size(); i++) {
            ASTNode t = children.get(i);
            if (i > 0) buf.append('\n');
            try {
                buf.append(t.toStringTree());
            } catch (Exception exc) { }
        }

        if (!isNull()) {
            buf.append(")");
        }

        return buf.toString();
    }
}
