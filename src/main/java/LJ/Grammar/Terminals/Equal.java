package LJ.Grammar.Terminals;

public class Equal implements Terminal {
    private final static String value = "=";

    @Override
    public String getValue() {
        return value;
    }
}
