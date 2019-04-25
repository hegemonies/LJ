package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Parentheses;
import LJ.Grammar.Terminals.TypeParentheses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Arithmetic implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(new Arithmetic(), new NTOperator(), new Arithmetic()));
        rules.add(Arrays.asList(new Parentheses(TypeParentheses.L_PARENT), new Arithmetic(),new Parentheses(TypeParentheses.L_PARENT)));
        rules.add(Collections.singletonList(new NTID()));
        rules.add(Collections.singletonList(new NTNumber()));
    }

    @Override
    public List<Symbol> getFirst() {
        return null;
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
