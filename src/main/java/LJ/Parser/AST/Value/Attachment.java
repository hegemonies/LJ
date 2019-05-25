package LJ.Parser.AST.Value;

import LJ.Parser.AST.Statement.NodeExpression;

public class Attachment extends GenericValue { // todo refactor
    private NodeExpression expression;

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }
}
