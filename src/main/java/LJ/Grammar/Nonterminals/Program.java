package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Program implements Nonterminal {
    private final static List<Symbol> rules = new ArrayList<>();

    static {
        rules.add(new NTClass());
    }

    @Override
    public List<Symbol> getFirst() {
        return rules;
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}