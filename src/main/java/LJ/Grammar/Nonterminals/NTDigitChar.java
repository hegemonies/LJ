package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.DigitChar;
import LJ.Grammar.Terminals.TypeDigitChar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NTDigitChar implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(new DigitChar(TypeDigitChar.PLUS)));
        rules.add(Arrays.asList(new DigitChar(TypeDigitChar.MINUS)));
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
