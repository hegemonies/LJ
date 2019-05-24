package LJ.Parser.AST.Value;

import LJ.Parser.AST.NodeExpression;

public class Attachment extends GenericValue {
    private NodeExpression expression;

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }
}
