package LJ.Grammar.Terminals;

public class Punctuation implements Terminal {
    private TypePunctuation type;

    public Punctuation(TypePunctuation type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        switch (type) {
            case DOT:
                return ".";
            case COMMA:
                return ",";
            case SEMICOLON:
                return ";";
        }

        return null;
    }
}
