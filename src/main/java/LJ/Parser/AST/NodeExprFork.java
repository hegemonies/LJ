package LJ.Parser.AST;

import LJ.Parser.AST.Operator.Operator;
import LJ.Parser.AST.Statement.NodeExpression;

public class NodeExprFork implements Node { // todo refactor (mb remove this class)
    private NodeExpression lExpression;
    private Operator operator;
    private NodeExpression rExpression;

    public void setlExpression(NodeExpression lExpression) {
        this.lExpression = lExpression;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setrExpression(NodeExpression rExpression) {
        this.rExpression = rExpression;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        return index;
    }
}
