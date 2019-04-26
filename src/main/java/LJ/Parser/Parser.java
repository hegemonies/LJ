package LJ.Parser;

import LJ.Lexer.ListLexer;

public class Parser {
    // todo AST
    private ListLexer listLexer;

    public Parser(ListLexer listLexer) {
        this.listLexer = listLexer;
    }

    public void go() {
        if (listLexer.getLookahead())
    }
}
