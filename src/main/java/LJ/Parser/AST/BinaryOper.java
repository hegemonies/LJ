package LJ.Parser.AST;

import LJ.Parser.AST.Operator.Operator;
import LJ.Parser.AST.Value.GenericValue;

public class BinaryOper {
    private GenericValue lval;
    private Operator oper;
    private GenericValue rval;

    public void setRval(GenericValue rval) {
        this.rval = rval;
    }

    public void setOper(Operator oper) {
        this.oper = oper;
    }

    public void setLval(GenericValue lval) {
        this.lval = lval;
    }
}
