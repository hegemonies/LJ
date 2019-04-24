package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.*;

import java.util.ArrayList;
import java.util.List;

public class Statement implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        List<Symbol> prod = new ArrayList<>();
        prod.add(new NTID());
        rules.add(prod);

        prod.clear();
        prod.add(new NTID());
        prod.add(new Operator(TypeOperator.EQUAL));
        prod.add(new NTNumber());
        prod.add(new Punctuation(TypePunctuation.SEMICOLON));
        rules.add(prod);

        prod.clear();
        prod.add(new NTID());
        prod.add(new Operator(TypeOperator.EQUAL));
        prod.add(new NTStringConst());
        prod.add(new Punctuation(TypePunctuation.SEMICOLON));
        rules.add(prod);

        prod.clear();
        prod.add(new NTID());
        prod.add(new Operator(TypeOperator.EQUAL));
        prod.add(new Method());
        prod.add(new Punctuation(TypePunctuation.SEMICOLON));
        rules.add(prod);

        prod.clear();
        prod.add(new NTID());
        prod.add(new Operator(TypeOperator.EQUAL));
        prod.add(new NTID());
        prod.add(new Punctuation(TypePunctuation.SEMICOLON));
        rules.add(prod);

        prod.add(new NTID());
        prod.add(new Parentheses(TypeParentheses.L_SQUARE));
        prod.add(new NTNumber());
        prod.add(new Parentheses(TypeParentheses.R_SQUARE));
        rules.add(prod);

        prod.clear();
        prod.add(new NTID());
        prod.add(new Parentheses(TypeParentheses.L_SQUARE));
        prod.add(new NTNumber());
        prod.add(new Parentheses(TypeParentheses.R_SQUARE));
        prod.add(new Operator(TypeOperator.EQUAL));
        prod.add(new NTNumber());
        prod.add(new Punctuation(TypePunctuation.SEMICOLON));
        rules.add(prod);

        prod.clear();
        prod.add(new NTID());
        prod.add(new Parentheses(TypeParentheses.L_SQUARE));
        prod.add(new NTNumber());
        prod.add(new Parentheses(TypeParentheses.R_SQUARE));
        prod.add(new Operator(TypeOperator.EQUAL));
        prod.add(new NTStringConst());
        prod.add(new Punctuation(TypePunctuation.SEMICOLON));
        rules.add(prod);

        prod.clear();
        prod.add(new NTID());
        prod.add(new Parentheses(TypeParentheses.L_SQUARE));
        prod.add(new NTNumber());
        prod.add(new Parentheses(TypeParentheses.R_SQUARE));
        prod.add(new Operator(TypeOperator.EQUAL));
        prod.add(new Method());
        prod.add(new Punctuation(TypePunctuation.SEMICOLON));
        rules.add(prod);

        prod.clear();
        prod.add(new NTID());
        prod.add(new Parentheses(TypeParentheses.L_SQUARE));
        prod.add(new NTNumber());
        prod.add(new Parentheses(TypeParentheses.R_SQUARE));
        prod.add(new Operator(TypeOperator.EQUAL));
        prod.add(new NTID());
        prod.add(new Punctuation(TypePunctuation.SEMICOLON));
        rules.add(prod);
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
