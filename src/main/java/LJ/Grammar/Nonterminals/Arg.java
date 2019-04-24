package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Arg implements Nonterminal {
    private static final List<Symbol> rules = new ArrayList<>();

    static {
        rules.add(new ModAccess());
        rules.add(new NTID());
    }

    @Override
    public List<Symbol> getFirst() {
        return Collections.singletonList(rules.get(0));
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
