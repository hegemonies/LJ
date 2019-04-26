package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Parentheses;
import LJ.Grammar.Terminals.TypeParentheses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ValueExpr implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Collections.singletonList(new NTID()));
        rules.add(Arrays.asList(new NTID(),
                new Parentheses(TypeParentheses.L_SQUARE),
                new NTNumber(),
                new Parentheses(TypeParentheses.R_SQUARE)));
        rules.add(Arrays.asList(new NTID(),
                new Parentheses(TypeParentheses.L_PARENT),
                new Parentheses(TypeParentheses.R_PARENT)));
        rules.add(Collections.singletonList(new NTNumber()));
        rules.add(Collections.singletonList(new NTStringConst()));
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
