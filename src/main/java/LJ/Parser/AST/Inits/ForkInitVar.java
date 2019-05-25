package LJ.Parser.AST.Inits;

import LJ.Parser.AST.Statement.NodeExpression;
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
    public int visit(String rootName, int index, StringBuilder sb) {
        return expression.visit(rootName, index, sb);
    }
}
