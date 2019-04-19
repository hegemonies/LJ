package LJ.Grammar.Terminals;

public class Brackets {
    private Bracket value;

    public Brackets(Bracket value) {
        this.value = value;
    }

    public Bracket getValue() {
        return value;
    }

    public enum Braces implements Bracket {
        LEFT_BRACE,
        RIGHT_BRACE
    }

    enum Parenthesis {
        LEFT_PARENTHESIS,
        RIGHT_PARENTHESIS
    }
}
