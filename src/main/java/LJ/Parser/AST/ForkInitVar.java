package LJ.Parser.AST;

public class ForkInitVar extends ForkInit {
    NodeExpression expression;

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    @Override
    public void chooseType() {
        type = TypeInit.VAR;
    }
}
