package LJ.Grammar.Terminals;

public class Quotes implements Terminal {
    private TypeQuotes type;

    public Quotes(TypeQuotes type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        if (type == TypeQuotes.SINGLE) {
            return "\'";
        } else if (type == TypeQuotes.DOUBLE) {
            return "\"";
        }

        return null;
    }
}
