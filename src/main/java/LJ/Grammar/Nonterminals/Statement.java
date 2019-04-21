package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Equal;
import LJ.Grammar.Terminals.Punctuation;
import LJ.Grammar.Terminals.TypePunctuation;

import java.util.ArrayList;
import java.util.List;

public class Statement implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        List<Symbol> prod = new ArrayList<>();
        prod.add(new ID());
        rules.add(prod);

        prod.clear();
        prod.add(new ID());
        prod.add(new Equal());
        prod.add(new NTNumber());
        prod.add(new Punctuation(TypePunctuation.SEMICOLON));
        rules.add(prod);

        prod.clear();
        prod.add(new ID());
        prod.add(new Equal());
        prod.add(new NTString_Const());
        prod.add(new Punctuation(TypePunctuation.SEMICOLON));
        rules.add(prod);

        prod.clear();
        prod.add(new ID());
        prod.add(new Equal());
        prod.add(new Method());
        prod.add(new Punctuation(TypePunctuation.SEMICOLON));
        rules.add(prod);

        // todo continue work
    }

    @Override
    public List<Symbol> getFirst() {
        List<Symbol> a = new ArrayList<>();

        for (List<Symbol> rule : rules) {
            a.add(rule.get(0));
        }

        return a;
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
