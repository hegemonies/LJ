package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NTID implements Nonterminal {
    private static final List<Symbol> rules = new ArrayList<>();

    static {
        rules.add(new ID());
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
