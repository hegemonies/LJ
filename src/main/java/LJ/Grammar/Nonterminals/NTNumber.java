package LJ.Grammar.Nonterminals;

import LJ.Grammar.E;
import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Numbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NTNumber implements Nonterminal {
    private static List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(new Numbers(), new NTNumber()));
        rules.add(Collections.singletonList(new E()));
    }

    @Override
    public List<Symbol> getFirst() {
        List<Symbol> a = new ArrayList<>();

        for (List<Symbol> rule : rules) {
            if (rule.get(0).getClass() == Nonterminal.class) {
                a.add(rule.get(0));
            }
        }

        return a;
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
