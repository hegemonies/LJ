package LJ.Grammar.Nonterminals;

import java.util.ArrayList;
import java.util.List;

public class Program implements Nonterminal {
    private final static List<Nonterminal> rules = new ArrayList<>();

    static {
        rules.add(new NTClass(rules));
    }

    @Override
    public <T extends Nonterminal> T getFirst() {
        return null;
    }

    @Override
    public <T extends Nonterminal> T getFollow() {
        return null;
    }
}
