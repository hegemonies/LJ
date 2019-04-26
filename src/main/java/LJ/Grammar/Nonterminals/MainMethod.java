package LJ.Grammar.Nonterminals;

import LJ.Grammar.E;
import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.*;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainMethod implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(new TModeAccess(TypeTModAccess.PUBLIC),
                new TModeAccess(TypeTModAccess.STATIC),
                new DataType(TypeDataType.VOID),
                new ID("main"),
                new Parentheses(TypeParentheses.L_PARENT),
                new ID("String"),
                new Parentheses(TypeParentheses.L_SQUARE),
                new Parentheses(TypeParentheses.R_SQUARE),
                new ID("args"),
                new Parentheses(TypeParentheses.R_PARENT),
                new Parentheses(TypeParentheses.L_BRACE),
                new StatementList(),
                new Parentheses(TypeParentheses.R_BRACE)));
        rules.add(Collections.singletonList(new E()));
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
