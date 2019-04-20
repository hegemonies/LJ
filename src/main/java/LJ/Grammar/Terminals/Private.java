package LJ.Grammar.Terminals;

public class Private implements Terminal {
    private final static String value = "private";

    @Override
    public String getValue() {
        return value;
    }
}
