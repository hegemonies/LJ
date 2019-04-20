package LJ.Grammar.Nonterminals;

import LJ.Grammar.E;
import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Private;
import LJ.Grammar.Terminals.Protected;
import LJ.Grammar.Terminals.Public;

import java.util.Arrays;
import java.util.List;

class ModAccess implements Nonterminal {
    private final static Symbol[] terminals = { new Public(), new Private(), new Protected(), new E()};

    @Override
    public List<Symbol> getFirst() {
        return Arrays.asList(terminals);
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }

}
