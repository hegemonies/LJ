package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Expression implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(new Statement(), new NTCondition(), new Statement()));
        rules.add(Arrays.asList(new Expression(), new NTLogicOperator(), new Expression()));
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
