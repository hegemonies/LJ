package LJ.Grammar.Nonterminals;

import LJ.Grammar.E;
import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Punctuation;
import LJ.Grammar.Terminals.TypePunctuation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StatementList implements Nonterminal {
    private final static List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(new Statement(), new Punctuation(TypePunctuation.SEMICOLON), new StatementList()));
        rules.add(Collections.singletonList(new E()));
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
