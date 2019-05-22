package LJ.Parser.AST;

import LJ.Parser.AST.Operator.Operator;

public class NodeExprFork implements Node {
    Operator operator;
    NodeExpression expression;

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }
}
