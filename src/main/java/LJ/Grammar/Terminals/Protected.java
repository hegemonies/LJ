package LJ.Grammar.Terminals;

public class Protected implements Terminal {
    private final static String value = "protected";

    @Override
    public String getValue() {
        return value;
    }
}
