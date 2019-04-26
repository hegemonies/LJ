package LJ.Grammar.Nonterminals;

import LJ.Grammar.E;
import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Punctuation;
import LJ.Grammar.Terminals.TypePunctuation;
import com.sun.org.apache.xpath.internal.Arg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArgsInitList implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(new ArgsInitList(), new Punctuation(TypePunctuation.COMMA), new ArgsInitList()));
        rules.add(Arrays.asList(new NTDataType(), new RValue()));
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
