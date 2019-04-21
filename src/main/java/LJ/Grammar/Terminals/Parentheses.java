package LJ.Grammar.Terminals;

public class Parentheses implements Terminal {
    private TypeParentheses type;

    public Parentheses(TypeParentheses type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        switch (type) {
            case L_PARENT:
                return "(";
            case R_PARENT:
                return ")";
            case L_BRACE:
                return "{";
            case R_BRACE:
                return "}";
            case L_SQUARE:
                return "[";
            case R_SQUARE:
                return "]";
        }

        return null;
    }
}
