package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Parentheses;
import LJ.Grammar.Terminals.TypeParentheses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Method implements Nonterminal {
    private static final List<Symbol> rules = new ArrayList<>();

    static {
        rules.add(new ModAccess());
        rules.add(new NTID());
        rules.add(new Parentheses(TypeParentheses.L_PARENT));
        rules.add(new ArgsList());
        rules.add(new Parentheses(TypeParentheses.R_PARENT));
        rules.add(new Parentheses(TypeParentheses.L_BRACE));
        rules.add(new StatementList());
        rules.add(new MethodList());
        rules.add(new LoopList());
        rules.add(new Parentheses(TypeParentheses.R_BRACE));
    }

    @Override
    public List<Symbol> getFirst() {
        return Collections.singletonList(rules.get(0));
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
