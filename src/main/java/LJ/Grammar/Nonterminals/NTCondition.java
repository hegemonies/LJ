package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Condition;
import LJ.Grammar.Terminals.TypeCondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NTCondition implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Collections.singletonList(new Condition(TypeCondition.LESS)));
        rules.add(Collections.singletonList(new Condition(TypeCondition.GREATER)));
        rules.add(Collections.singletonList(new Condition(TypeCondition.EXCLAIM)));
        rules.add(Collections.singletonList(new Condition(TypeCondition.EQUALEQUAL)));
        rules.add(Collections.singletonList(new Condition(TypeCondition.LESSEQUAL)));
        rules.add(Collections.singletonList(new Condition(TypeCondition.GREATEREQUAL)));
        rules.add(Collections.singletonList(new Condition(TypeCondition.EXCLAIMEQUAL)));
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
