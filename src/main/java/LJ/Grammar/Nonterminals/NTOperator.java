package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Operator;
import LJ.Grammar.Terminals.TypeOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NTOperator implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(new Operator(TypeOperator.PLUS)));
        rules.add(Arrays.asList(new Operator(TypeOperator.MINUS)));
        rules.add(Arrays.asList(new Operator(TypeOperator.STAR)));
        rules.add(Arrays.asList(new Operator(TypeOperator.SLASH)));
        rules.add(Arrays.asList(new Operator(TypeOperator.PERCENT)));
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
