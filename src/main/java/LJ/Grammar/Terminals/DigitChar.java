package LJ.Grammar.Terminals;

public class DigitChar implements Terminal {
    private TypeDigitChar type;

    public DigitChar(TypeDigitChar type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        switch (type) {
            case PLUS:
                return "+";
            case MINUS:
                return "-";
        }

        return null;
    }
}
