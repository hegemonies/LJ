package LJ.Grammar.Terminals;

import LJ.Grammar.Symbol;

public class L_Brace implements Terminal {
    private final static String value = "{";

    @Override
    public String getValue() {
        return value;
    }
}
