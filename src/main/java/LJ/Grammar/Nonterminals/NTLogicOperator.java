package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.LogicOperator;
import LJ.Grammar.Terminals.TypeLogicOperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NTLogicOperator implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Collections.singletonList(new LogicOperator(TypeLogicOperator.AND)));
        rules.add(Collections.singletonList(new LogicOperator(TypeLogicOperator.OR)));
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
