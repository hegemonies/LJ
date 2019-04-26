package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RValue implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Collections.singletonList(new NTNumber()));
        rules.add(Collections.singletonList(new NTStringConst()));
        rules.add(Collections.singletonList(new NTID()));
    }

    @Override
    public List<Symbol> getFirst() {
        List<Symbol> a = new ArrayList<>();

        for (List<Symbol> rule : rules) {
            a.add(rule.get(0));
        }

        return a;
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
