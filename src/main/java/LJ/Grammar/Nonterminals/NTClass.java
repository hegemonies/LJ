package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Parentheses;
import LJ.Grammar.Terminals.TClass;
import LJ.Grammar.Terminals.TypeParentheses;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NTClass implements Nonterminal {
    private List<Symbol> rules = Arrays.asList(new ModAccessClass(), new TClass(), new NTID(), new Parentheses(TypeParentheses.L_BRACE),
                                            new ModAccess());

    @Override
    public List<Symbol> getFirst() {
        return Collections.singletonList(rules.get(0));
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
