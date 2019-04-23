package LJ.Grammar.Terminals;

public class New implements Terminal {
    private static final String value = "new";

    @Override
    public String getValue() {
        return value;
    }
}
