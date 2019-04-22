package LJ.Grammar.Terminals;

public class LogicOperator implements Terminal {
    private TypeLogicOperator type;

    public LogicOperator(TypeLogicOperator type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        switch (type) {
            case AND:
                return "&&";
            case OR:
                return "||";
        }

        return null;
    }
}
