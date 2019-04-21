package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Quotes;
import LJ.Grammar.Terminals.StringConst;
import LJ.Grammar.Terminals.TypeQuotes;

import java.util.ArrayList;
import java.util.List;

public class NTString_Const implements Nonterminal {
    private final static List<Symbol> rules = new ArrayList<>();

    static {
        rules.add(new Quotes(TypeQuotes.DOUBLE));
        rules.add(new StringConst());
        rules.add(new Quotes(TypeQuotes.DOUBLE));
    }

    @Override
    public List<Symbol> getFirst() {
        return rules;
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
