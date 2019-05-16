package LJ.Parser.AST;

public class ForkInitVar implements ForkInit {
    NodeExpression expression;

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }
}
