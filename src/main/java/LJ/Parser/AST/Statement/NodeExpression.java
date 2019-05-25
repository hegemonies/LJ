package LJ.Parser.AST.Statement;

import LJ.Parser.AST.Operator.Operator;
import LJ.Parser.AST.Value.GenericValue;

public class NodeExpression extends NodeStatement {
    private GenericValue lValue;
    private NodeExpression lExpression;
    private Operator operator;
    private GenericValue rValue;
    private NodeExpression rExpression;

    public void setlValue(GenericValue lValue) {
        this.lValue = lValue;
    }

    public void setlExpression(NodeExpression lExpression) {
        this.lExpression = lExpression;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setrValue(GenericValue rValue) {
        this.rValue = rValue;
    }

    public void setrExpression(NodeExpression rExpression) {
        this.rExpression = rExpression;
    }

    public GenericValue getlValue() {
        return lValue;
    }

    public GenericValue getrValue() {
        return rValue;
    }

    public Operator getOperator() {
        return operator;
    }

    public NodeExpression getlExpression() {
        return lExpression;
    }

    public NodeExpression getrExpression() {
        return rExpression;
    }
}
