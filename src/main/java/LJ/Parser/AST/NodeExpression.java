package LJ.Parser.AST;

public class NodeExpression extends NodeStatement {
    private BinaryOper arithmetic;
    private NodeExprFork exprFork;

    public void setArithmetic(BinaryOper arithmetic) {
        this.arithmetic = arithmetic;
    }

    public void setExprFork(NodeExprFork exprFork) {
        this.exprFork = exprFork;
    }
}
