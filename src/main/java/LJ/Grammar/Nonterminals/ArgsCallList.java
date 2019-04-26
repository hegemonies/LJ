package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Punctuation;
import LJ.Grammar.Terminals.TypePunctuation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArgsCallList implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(new ArgsCallList(), new Punctuation(TypePunctuation.COMMA), new ArgsCallList()));
        rules.add(Collections.singletonList(new RValue()));
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
