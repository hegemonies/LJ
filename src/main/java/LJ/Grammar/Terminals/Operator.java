package LJ.Grammar.Terminals;

public class Operator implements Terminal {
    private TypeOperator type;

    public Operator(TypeOperator type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        switch (type) {
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case STAR:
                return "*";
            case SLASH:
                return "/";
            case PERCENT:
                return "%";
            case EQUAL:
                return "=";
        }

        return null;
    }
}
