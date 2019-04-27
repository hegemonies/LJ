package LJ.Parser;

import LJ.Grammar.Nonterminals.Nonterminal;
import LJ.Grammar.Nonterminals.Program;
import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Terminal;
import LJ.Lexer.ListLexer;

import java.util.List;

public class Parser {
    // todo AST
    private ListLexer listLexer;

    public Parser(ListLexer listLexer) {
        this.listLexer = listLexer;
    }

    public void go() {
        listLexer.match("program");
        walk(new Program());
    }

    private void walk(Nonterminal rule) {
        List<Symbol> productions = rule.getFirst();

        while (productions.iterator().hasNext()) {
            Symbol next = productions.iterator().next();
            if (next.getClass().equals(Nonterminal.class)) {
                walk(next);
            } else {
                listLexer.match(((Terminal) next).getValue());
            }
        }
    }

    private void walk(Terminal rule) {

    }

    private void walk(Symbol rule) {
        Class rule_interface = rule.getClass().getInterfaces()[0];

        if (rule_interface.equals(Nonterminal.class)) {
            walk((Nonterminal) rule);
        } else if (rule_interface.equals(Terminal.class)) {
            walk((Terminal) rule);
        }
    }

    public static void main(String... args) {
        System.out.println(new Program().getClass().toString());
        System.out.println(new Program().getClass().getInterfaces()[0].toString());
        System.out.println(new Program().getClass().getInterfaces()[0].equals(Nonterminal.class));
    }
}
