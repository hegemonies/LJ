package LJ.Grammar.Terminals;

public class Equal implements Terminal {
    private static final String value = "=";

    @Override
    public String getValue() {
        return value;
    }
}
