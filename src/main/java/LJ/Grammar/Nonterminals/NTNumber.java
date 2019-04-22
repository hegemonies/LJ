package LJ.Grammar.Nonterminals;

import LJ.Grammar.E;
import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.IRNumbers;
import LJ.Grammar.Terminals.Numbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NTNumber implements Nonterminal {
    private static List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(new Numbers(IRNumbers.ONE), new NTNumber()));
        rules.add(Arrays.asList(new Numbers(IRNumbers.TWO), new NTNumber()));
        rules.add(Arrays.asList(new Numbers(IRNumbers.THREE), new NTNumber()));
        rules.add(Arrays.asList(new Numbers(IRNumbers.FOUR), new NTNumber()));
        rules.add(Arrays.asList(new Numbers(IRNumbers.FIVE), new NTNumber()));
        rules.add(Arrays.asList(new Numbers(IRNumbers.SIX), new NTNumber()));
        rules.add(Arrays.asList(new Numbers(IRNumbers.SEVEN), new NTNumber()));
        rules.add(Arrays.asList(new Numbers(IRNumbers.EIGHT), new NTNumber()));
        rules.add(Arrays.asList(new Numbers(IRNumbers.NINE), new NTNumber()));
        rules.add(Arrays.asList(new Numbers(IRNumbers.ZERO), new NTNumber()));

        rules.add(Collections.singletonList(new E()));
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
