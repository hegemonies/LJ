package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Loop;
import LJ.Grammar.Terminals.Parentheses;
import LJ.Grammar.Terminals.TypeLoop;
import LJ.Grammar.Terminals.TypeParentheses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NTLoop implements Nonterminal {
    private final static List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(
                new Loop(TypeLoop.WHILE), new Parentheses(TypeParentheses.L_PARENT), new Expression(), new Parentheses(TypeParentheses.R_PARENT),
                new Parentheses(TypeParentheses.L_BRACE),
                new StatementInitList(),
                new StatementList(),
                new MethodList(),
                new LoopList(),
                new Parentheses(TypeParentheses.R_BRACE)));
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
