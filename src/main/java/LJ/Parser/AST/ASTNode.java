package LJ.Parser.AST;

import LJ.Lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {
    private Token token = null;
    private List<ASTNode> childrens;
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
        if (childrens == null) {
            childrens = new ArrayList<>();
        }

        childrens.add(t);
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
        if (childrens == null || childrens.size() == 0) {
            return this.toString();
        }

        StringBuilder buf = new StringBuilder();

        if (!isNull()) {
            buf.append("(");
            buf.append(this.toString());
            buf.append('\t');
        }

        for (int i = 0; i < childrens.size(); i++) {
            ASTNode t = childrens.get(i);
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

    public String _toStringTreeRoot() {
        String string = "digraph AST {\n";
        
        string += _toString();
        
        string += "}";
        return string;
    }
    
    public String _toString() {
        StringBuilder sb = new StringBuilder();
        
        if (childrens != null) {
            for (ASTNode children : childrens) {
                if (children != null) {
                    sb.append("\"");

                    if (this.token == null) {
                        sb.append("null");
                    } else {
                        sb.append(this.token.getType());
                    }

                    sb.append("\"->\"");

                    if (children.token == null) {
                        sb.append("null");
                    } else {
                        sb.append(children.token.getType());
                    }
                    sb.append("\"");

                    sb.append("\n");

                    sb.append(children._toString());
                }
            }
        }

        
        return sb.toString();
    }
}
