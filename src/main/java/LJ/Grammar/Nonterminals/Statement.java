package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Equal;

import java.util.ArrayList;
import java.util.List;

public class Statement implements Nonterminal {
    static List<List<Symbol>> productions = new ArrayList<>();

    static {
        List<Symbol> prod1 = new ArrayList<>();
        prod1.add(new ID());
        productions.add(prod1);

        List<Symbol> prod2 = new ArrayList<>();
        prod2.add(new ID());
        prod2.add(new Equal());
        prod2.add(new NTNumber());
        productions.add(prod2);
    }

    @Override
    public List<Symbol> getFirst() {
        return null;
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
