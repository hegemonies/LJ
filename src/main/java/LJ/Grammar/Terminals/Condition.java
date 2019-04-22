package LJ.Grammar.Terminals;

public class Condition implements Terminal {
    private TypeCondition type;

    public Condition(TypeCondition type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        switch (type) {
            case LESS:
                return "<";
            case GREATER:
                return ">";
            case EXCLAIM:
                return "!";
            case EQUALEQUAL:
                return "==";
            case LESSEQUAL:
                return "<=";
            case GREATEREQUAL:
                return ">=";
            case EXCLAIMEQUAL:
                return "!=";
        }

        return null;
    }
}
