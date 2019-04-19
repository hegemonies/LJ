package LJ.Grammar.Nonterminals;

import java.util.Arrays;
import java.util.List;

class ModAccess implements Nonterminal {
    private final static String[] terminals = { "public", "private", "protected" };

    public static List<String> getFirst() {
        return Arrays.asList(terminals);
    }

    @Override
    public <T extends Nonterminal> T getFollow() {
        return null;
    }
}
