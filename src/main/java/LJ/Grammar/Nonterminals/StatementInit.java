package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatementInit implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(new NTDataType(),
                new ID(),
                new Operator(TypeOperator.EQUAL),
                new NTDigitChar(),
                new NTNumber(),
                new Punctuation(TypePunctuation.SEMICOLON)));
        rules.add(Arrays.asList(new NTDataType(),
                new ID(),
                new Operator(TypeOperator.EQUAL),
                new NTString_Const(),
                new Punctuation(TypePunctuation.SEMICOLON)));
        rules.add(Arrays.asList(new NTDataType(),
                new Parentheses(TypeParentheses.L_SQUARE),
                new Parentheses(TypeParentheses.R_SQUARE),
                new ID(),
                new Operator(TypeOperator.EQUAL),
                new New(),
                new NTDataType(),
                new Parentheses(TypeParentheses.L_SQUARE),
                new NTNumber(),
                new Parentheses(TypeParentheses.R_SQUARE),
                new Punctuation(TypePunctuation.SEMICOLON)));
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
