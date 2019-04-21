package LJ.Grammar.Nonterminals;

import LJ.Grammar.E;
import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.TModeAccess;
import LJ.Grammar.Terminals.TypeTModAccess;

import java.util.Arrays;
import java.util.List;

class ModAccess implements Nonterminal {
    private final static Symbol[] terminals = { new TModeAccess(TypeTModAccess.PUBLIC),
            new TModeAccess(TypeTModAccess.PRIVATE),
            new TModeAccess(TypeTModAccess.PROTECTED),
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
