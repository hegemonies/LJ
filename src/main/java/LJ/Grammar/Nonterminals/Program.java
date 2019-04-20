package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Program implements Nonterminal {
    private final static List<Nonterminal> rules = new ArrayList<>();

    static {
        rules.add(new NTClass());
    }

    @Override
    public List<Symbol> getFirst() {
        return null;
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
