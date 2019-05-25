package LJ.Parser.AST.Inits;

import LJ.Parser.AST.NodeExpression;
import LJ.Parser.AST.TypeInit;

public class ForkInitVar extends ForkInit {
    NodeExpression expression;

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    @Override
    public void chooseType() {
        type = TypeInit.VAR;
    }

    @Override
    public String visit() {

        return null;
    }
}
