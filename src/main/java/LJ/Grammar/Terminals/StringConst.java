package LJ.Grammar.Terminals;

public class StringConst implements Terminal {
    private String value;

    public StringConst(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
