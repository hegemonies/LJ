package LJ.Grammar.Nonterminals;

import LJ.Grammar.E;
import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.TModeAccess;
import LJ.Grammar.Terminals.TypeTModAccess;

import java.util.Arrays;
import java.util.List;

public class ModAccessClass implements Nonterminal {
    private final static Symbol[] terminals = { new TModeAccess(TypeTModAccess.PUBLIC),
            new TModeAccess(TypeTModAccess.PRIVATE),
            new E()};

    @Override
    public List<Symbol> getFirst() {
        return Arrays.asList(terminals);
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
