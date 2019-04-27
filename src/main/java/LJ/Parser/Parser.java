package LJ.Parser;

import LJ.Lexer.ListLexer;

public class Parser {
    // todo AST
    private ListLexer listLexer;

    public Parser(ListLexer listLexer) {
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
