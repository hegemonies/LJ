package LJ.Grammar.Terminals;

public class Quotes implements Terminal {
    private TypeQuotes type;

    public Quotes(TypeQuotes type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        switch (type) {
            case SINGLE:
                return "\'";
            case DOUBLE:
                return "\"";
        }

        return null;
    }
}
