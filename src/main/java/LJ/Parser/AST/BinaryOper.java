package LJ.Parser.AST;

import LJ.Parser.AST.Operator.Operator;

public class BinaryOper {
    private NodeExpression lval;
    private Operator oper;
    private NodeExpression rval;

    public void setLval(NodeExpression lval) {
        this.lval = lval;
    }

    public void setOper(Operator oper) {
        this.oper = oper;
    }

    public void setRval(NodeExpression rval) {
        this.rval = rval;
    }
}
