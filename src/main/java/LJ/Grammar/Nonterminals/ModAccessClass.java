package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Private;
import LJ.Grammar.Terminals.Public;

import java.util.Arrays;
import java.util.List;

public class ModAccessClass implements Nonterminal {
    private final static Symbol[] terminals = { new Public(), new Private() };

    @Override
    public List<Symbol> getFirst() {
        return Arrays.asList(terminals);
    }

    @Override
    public <T extends Symbol> List<T> getFollow() {
        return null;
    }
}
