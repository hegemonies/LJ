package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.L_Brace;
import LJ.Grammar.Terminals.TClass;

import java.util.Arrays;
import java.util.List;

public class NTClass implements Nonterminal {
    private List<Symbol> rules = Arrays.asList(new ModAccessClass(), new TClass(), new ID(), new L_Brace(),
                                            new ModAccess());

    @Override
    public List<Symbol> getFirst() {
        return null;
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
