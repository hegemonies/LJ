package LJ.Parser;

import LJ.Lexer.ListLexer;
import LJ.Parser.ParserException.CriticalProductionException;
import LJ.Parser.ParserException.OptionalProductionException;

public class Parser {
    // todo AST
    private ListLexer listLexer;

    public Parser(ListLexer listLexer) {
        this.listLexer = listLexer;
    }

    public void go() {
        try {
            listLexer.match("program");
        } catch (Exception e) {
            e.printStackTrace();
        }
        parseProgram();
    }

    private void parseProgram() {
        try {
            parseModAccessClass();
            listLexer.match("class");
            listLexer.match("id");
            listLexer.match("l_brace");
            try {
                parseInitList();
            } catch (OptionalProductionException exc) { }
            parseMainMethod();
            listLexer.match("r_brace");
        }  catch (CriticalProductionException exc) {
            exc.printStackTrace();
        }
    }

    private void parseModAccessClass() throws CriticalProductionException {
        listLexer.matchOneOf("public", "private");
    }

    private void parseInitList() throws OptionalProductionException {
        try {
            parseInit();
            parseInitList();
        } catch (Exception e) {
            throw new OptionalProductionException(e.getMessage());
        }
    }

    private void parseInit() throws CriticalProductionException {
        parseNativeDataType();
        listLexer.match("id");
        parseForkInit();
    }

    private void parseNativeDataType() throws CriticalProductionException {
        listLexer.matchOneOf("int", "char");
    }

    private void parseForkInit() { //todo refactor
        parseForkInitFunc();
        parseForkInitVar();
        parseForkInitArray();
    }

    private boolean parseForkInitFunc() {
        try {
            listLexer.match("l_paren");
            try {
                parseArgsInitListChanger();
            } catch (OptionalProductionException e) { }
            listLexer.match("r_paren");
            listLexer.match("l_brace");
            parseStatementList();
            listLexer.match("r_brace");
        } catch (CriticalProductionException e) {
            return false;
        }

        return true;
    }

    private void parseStatementList() {
        // todo statementList
    }

    private void parseArgsInitListChanger() throws OptionalProductionException {
        parseArgsInitList();
    }

    private void parseArgsInitList() {
        // todo argsInitList
    }

    private boolean parseForkInitVar() {
        // todo forkInitVar
    }

    private boolean parseForkInitArray() {
        // todo forInitArray
    }

    private void parseMainMethod() throws CriticalProductionException {

    }
}
