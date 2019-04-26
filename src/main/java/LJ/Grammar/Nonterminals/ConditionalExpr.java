package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.If;
import LJ.Grammar.Terminals.Parentheses;
import LJ.Grammar.Terminals.TypeParentheses;

import java.util.ArrayList;
import java.util.List;

public class ConditionalExpr implements Nonterminal {
    private static final List<Symbol> rules = new ArrayList<>();

    static {
        rules.add(new If());
        rules.add(new Parentheses(TypeParentheses.L_PARENT));
        rules.add(new Expression());
        rules.add(new Parentheses(TypeParentheses.R_PARENT));
        rules.add(new Parentheses(TypeParentheses.L_BRACE));
        rules.add(new StatementInit());
        rules.add(new Parentheses(TypeParentheses.R_BRACE));
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
