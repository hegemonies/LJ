package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Brackets;
import LJ.Grammar.Terminals.TClass;

import java.util.Arrays;
import java.util.List;

public class NTClass implements Nonterminal {
    private List<Symbol> rules = Arrays.asList(new ModAccessClass(),
                                            new TClass(),
                                            new ID(),
                                            new Brackets(Brackets.Braces));

    @Override
    public <T extends Symbol> List<T> getFirst() {
        return null;
    }

    @Override
    public <T extends Symbol> List<T> getFollow() {
        return null;
    }
}
