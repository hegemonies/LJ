package LJ.Parser;

import LJ.Grammar.Nonterminals.Nonterminal;
import LJ.Grammar.Nonterminals.Program;
import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.Terminal;
import LJ.Lexer.ListLexer;

import java.util.List;

public class Parser_ {
    // todo AST
    private ListLexer listLexer;

    public Parser_(ListLexer listLexer) {
        this.listLexer = listLexer;
    }

    public void go() {
        listLexer.match("program");
        parseProgram();
    }

    private void parseProgram() {
        parseModAccessClass();
    }

    private void parseModAccessClass() {
        if (listLexer.matchLite("public")) {

        } else if (listLexer.matchLite("private")) {

        }
    }


    // matchOfOne
}
