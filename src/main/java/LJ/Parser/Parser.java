package LJ.Parser;

import LJ.Lexer.ListLexer;
import LJ.Parser.ParserException.CriticalProductionException;
import LJ.Parser.ParserException.OptionalProductionException;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * <Program>: <class>
     */
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

    /**
     * <modAccessClass>: public | private
     * @throws CriticalProductionException
     */
    private void parseModAccessClass() throws CriticalProductionException {
        listLexer.matchOneOf("public", "private");
    }

    /**
     * <argsInitList>: <nativeDataType> <rvalue> | <argsInitList>, <argsInitList>
     * @throws OptionalProductionException
     */
    private void parseInitList() throws OptionalProductionException { // todo comeback and check this
        try {
            parseInit();
            parseInitList();
        } catch (Exception e) {
            throw new OptionalProductionException(e.getMessage());
        }
    }

    /**
     * <init>: <nativeDataType> <id> <forkInit>
     * @throws CriticalProductionException
     */
    private void parseInit() throws CriticalProductionException {
        parseNativeDataType();
        listLexer.match("id");
        parseForkInit();
    }

    /**
     * <nativeDataType>: int | char
     * @throws CriticalProductionException
     */
    private void parseNativeDataType() throws CriticalProductionException {
        listLexer.matchOneOf("int", "char");
    }

    /**
     * <forkInit>: <forInitFunc> |
     *     <forkInitVar> |
     *     <forInitArray>
     */
    private void parseForkInit() throws CriticalProductionException {
        boolean matchAny = parseForkInitFunc();

        if (!matchAny) {
            matchAny = parseForkInitVar();
        }

        if (!matchAny) {
            matchAny = parseForkInitArray();
        }

        if (!matchAny) {
            throw new CriticalProductionException("Found wrong init token");
        }
    }

    /**
     * <forInitFunc>: (<argsInitListChanger>) {
     *                    <statementList>
     *                }
     * @return
     */
    private boolean parseForkInitFunc() {
        try {
            listLexer.match("l_paren");
            try {
                parseArgsInitListChanger();
            } catch (OptionalProductionException e) { }
            listLexer.match("r_paren");
            listLexer.match("l_brace");
            try {
                parseStatementList();
            } catch (OptionalProductionException e) { }
            listLexer.match("r_brace");
        } catch (CriticalProductionException e) {
            return false;
        }

        return true;
    }

    /**
     * <forkInitVar>: = <valueExpr>;
     * @return
     */
    private boolean parseForkInitVar() {
        try {
            listLexer.match("equal");
            parseValueExpr();
            listLexer.match("semi");
        } catch (CriticalProductionException exc) {
            return false;
        }

        return true;
    }

    /**
     * <forkInitArray>: <arrayMember> = new <nativeDataType><arrayMember>;
     * @return
     */
    private boolean parseForkInitArray() {
        try {
            parseArrayMember();
            listLexer.match("equal");
            listLexer.match("new");
            parseNativeDataType();
            parseArrayMember();
            listLexer.match("semi");
        } catch (CriticalProductionException exc) {
            return false;
        }

        return true;
    }

    /**
     * <valueExpr>: <vExpr> |
     *     <number> |
     *     <str_const>
     * @throws CriticalProductionException
     */
    private void parseValueExpr() throws CriticalProductionException { //todo refactor for if
        boolean matchAny = parseVExpr();

        if (!matchAny) {
            matchAny = parseNumber();
        }

        if (!matchAny) {
            matchAny = parseStrConst();
        }

        if (!matchAny) {
            throw new CriticalProductionException("Found wrong value expression");
        }
    }

    /**
     * <str_const>: /-\(-_-)/-\
     * @return
     */
    private boolean parseStrConst() {
        try {
            listLexer.match("str_literal");
        } catch (CriticalProductionException e) {
            return false;
        }

        return true;
    }

    /**
     * <number>: <number_head><number_tail>
     * <number_tail>: [0-9]<number_tail> | E
     * <number_head>: [1-9]
     * @return
     */
    private boolean parseNumber() {
        try {
            listLexer.match("numeric_constant");
        } catch (CriticalProductionException e) {
            return false;
        }

        return true;
    }

    /**
     * <vExpr>: <id> <vExprChange>
     * @return
     */
    private boolean parseVExpr() {
        try {
            listLexer.match("id");

            try {
                parseVExprChanger();
            } catch (OptionalProductionException e) { }

        } catch (CriticalProductionException e) {
            return false;
        }

        return true;
    }

    /**
     * <vExprChanger>: <arrayMember> |
     *      () |
     *      (<argsCallListChanger>) |
     *      E
     * @throws OptionalProductionException
     */
    private void parseVExprChanger() throws OptionalProductionException {
        boolean matchAny = false;

        try {
            parseArrayMember();
            matchAny = true;
        } catch (CriticalProductionException e) { }

        if (!matchAny) {
            try {
                listLexer.match("l_paren");

                try {
                    parseArgsCallListChanger();
                } catch (OptionalProductionException e) { }

                listLexer.match("r_paren");
            } catch (CriticalProductionException exc) {
                throw new OptionalProductionException();
            }
        }
    }

    /**
     * <argsCallListChanger>: <argsCallList> | E
     * @throws OptionalProductionException
     */
    private void parseArgsCallListChanger() throws OptionalProductionException {
        try {
            parseArgsCallList();
        } catch (CriticalProductionException exc) {
            throw new OptionalProductionException();
        }
    }

    /**
     * <argsCallList>: <valueExpr> | <argsCallList>, <argsCallList>
     * @throws CriticalProductionException
     */
    private void parseArgsCallList() throws CriticalProductionException { // todo check this again
        parseValueExpr();

        listLexer.match(",");
        parseArgsCallList();
    }

    /**
     * <arrayMember>: [<number>]
     * @throws OptionalProductionException
     */
    private void parseArrayMember() throws CriticalProductionException {
        listLexer.match("l_square");
        parseNumber();
        listLexer.match("r_square");
    }

    /**
     * <statementList>: <statement> <statementList> | E
     */
    private void parseStatementList() throws OptionalProductionException {
        try {
            parseStatement();
            parseStatementList(); // todo ???
        } catch (CriticalProductionException e) {
            throw new OptionalProductionException();
        }
    }

    /**
     * <statement>: { <statement> } |
     *     <loop> |
     *     <conditional> |
     *     <expression>; |
     *     <init>;
     */
    private void parseStatement() throws CriticalProductionException { // todo check this again
        boolean matchAny = false;

        try {
            listLexer.match("l_brace");
            parseStatement();
            listLexer.match("l_brace");

            matchAny = true;
        } catch (CriticalProductionException exc) { }

        if (!matchAny) {
            try {
                parseLoop();
                matchAny = true;
            } catch (CriticalProductionException e) { }
        }

        if (!matchAny) {
            try {
                parseConditional();
                matchAny = true;
            } catch (CriticalProductionException exc) { }
        }

        if (!matchAny) {
            try {
                parseExpression();
                listLexer.match("semi");
                matchAny = true;
            } catch (CriticalProductionException exc) { }
        }

        if (!matchAny) {
            try {
                parseInit();
                matchAny = true;
            } catch (CriticalProductionException exc) { }
        }

        if (!matchAny) {
            throw new CriticalProductionException("Found wrong statement?");
        }
    }

    private void parseExpression() throws CriticalProductionException {
        // todo write expression
    }

    /**
     * <conditional>: if (<expression>) {
     *         <statementList>
     *     }
     * @throws CriticalProductionException
     */
    private void parseConditional() throws CriticalProductionException {
        // todo write conditional
    }

    /**
     * <loop>: while (<expression>) {
     *         <statementList>
     *     }
     * @throws CriticalProductionException
     */
    private void parseLoop() throws CriticalProductionException {
        listLexer.match("while");
        listLexer.match("l_paren");
        parseExpression();
        listLexer.match("r_paren");
        listLexer.match("l_brace");
        try {
            parseStatementList();
        } catch (OptionalProductionException e) { }
        listLexer.match("r_brace");
    }

    private void parseArgsInitListChanger() throws OptionalProductionException {
        parseArgsInitList();
    }

    private void parseArgsInitList() {
        // todo argsInitList
    }

    /**
     * <condition>: < | > | == | != | <= | >=
     */
    private void parseConditions() throws CriticalProductionException {
        listLexer.matchOneOf("less", "greater", "equalequal", "exclaimequal", "lessequal", "greaterequal");
    }

    /**
     * <mainMethod>: public static void main(String[] args) {
     *         <statementList>
     *     }
     * @throws CriticalProductionException
     */
    private void parseMainMethod() throws CriticalProductionException {
        listLexer.match("public");
        listLexer.match("static");
        listLexer.match("void");
        listLexer.matchTypeAndCheckValue("id", "main");
        listLexer.match("l_paren");
        listLexer.matchTypeAndCheckValue("id", "String");
        listLexer.match("l_square");
        listLexer.match("r_square");
        listLexer.match("id");
        listLexer.match("r_paren");
        listLexer.match("l_brace");
        try {
            parseStatementList();
        } catch (OptionalProductionException e) { }
        listLexer.match("r_brace");
    }
}
