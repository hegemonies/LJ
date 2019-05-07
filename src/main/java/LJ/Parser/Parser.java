package LJ.Parser;

import LJ.Lexer.ListLexer;
import LJ.Parser.AST.Node;
import LJ.Parser.ParserException.CriticalProductionException;
import LJ.Parser.ParserException.OptionalProductionException;

import java.util.Objects;

public class Parser { // todo edit parser expression, valueExpr, statement
    private ListLexer listLexer;
//    Node root = new Node(Objects.requireNonNull(listLexer).getLookahead());
//    Node currentNode = root;

    public Parser(ListLexer listLexer) {
        this.listLexer = listLexer;
    }

    public void go() {
        try {
            listLexer.match("program");
            parseProgram();
        } catch (CriticalProductionException e) {
            e.printStackTrace();
        }
    }

    /**
     * <Program>: <class>
     */
    private void parseProgram() throws CriticalProductionException {
        parseClass();
    }

    /**
     * <class>: <modAccessClass> class <id> {
     *         <initList>
     *         <mainMethod>
     *     }
     * @throws CriticalProductionException
     */
    private void parseClass() throws CriticalProductionException {
        parseModAccessClass();
        listLexer.match("class");
        listLexer.match("id");
        listLexer.match("l_brace");
        parseInitList();
        parseMainMethod();
        listLexer.match("r_brace");
    }

    /**
     * <modAccessClass>: public | private
     * @throws CriticalProductionException
     */
    private void parseModAccessClass() throws CriticalProductionException {
        listLexer.matchOneOf("public", "private");
    }

    /**
     * <initList>: <init> <initList> | E
     * @throws CriticalProductionException
     */
    private void parseInitList() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();

        if (curTokenType.equals("int") ||
                curTokenType.equals("char")) {
            parseInit();
        } else {
            return;
        }

        parseInitList();
    }

    /**
     * <argsInitListChanger>: <argsInitList> | E
     * @throws OptionalProductionException
     */
    private void parseArgsInitListChanger() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();

        if (curTokenType.equals("int") ||
                curTokenType.equals("char")) {
            parseArgsInitList();
        }
    }

    /**
     * <argsInitList>:  <argInit> | <argsInitList>, <argsInitList>
     * @throws CriticalProductionException
     */
    private void parseArgsInitList() throws CriticalProductionException {
        parseArgInit();

        boolean matchComma = false;
        try {
            listLexer.match("comma");
            matchComma = true;
        } catch (CriticalProductionException ignored) { }

        if (matchComma) {
            parseArgsInitList();
        }
    }

    /**
     * <argInit>: <nativeDataType> <rvalue>
     */
    private void parseArgInit() throws CriticalProductionException {
        parseNativeDataType();
        parseRValue();
    }

    /**
     * <rvalue>: <number> | <str_const> | <id>
     * @throws CriticalProductionException
     */
    private void parseRValue() throws CriticalProductionException {
        boolean matchAny = parseNumber();

        if (!matchAny) {
            matchAny = parseStrConst();
        }

        if (!matchAny) {
            listLexer.match("id");
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
        String curTokenType = listLexer.getLookahead().getType();

        if (curTokenType.equals("l_paren")) {
            parseForkInitFunc();
        } else if (curTokenType.equals("equal")) {
            parseForkInitVar();
        } else if (curTokenType.equals("l_square")) {
            parseForkInitArray();
        }
    }

    /**
     * <forInitFunc>: (<argsInitListChanger>) {
     *                    <statementList>
     *                }
     * @return
     */
    private void parseForkInitFunc() throws CriticalProductionException {
        listLexer.match("l_paren");
        parseArgsInitListChanger();
        listLexer.match("r_paren");
        listLexer.match("l_brace");
        parseStatementList();
        listLexer.match("r_brace");
    }

    /**
     * <forkInitVar>: = <valueExpr>;
     * @return
     */
    private void parseForkInitVar() throws CriticalProductionException {
        listLexer.match("equal");
        parseValueExpr();
        listLexer.match("semi");
    }

    /**
     * <forkInitArray>: <arrayMember> = new <nativeDataType><arrayMember>;
     * @return
     */
    private void parseForkInitArray() throws CriticalProductionException {
        parseArrayMember();
        listLexer.match("equal");
        listLexer.match("new");
        parseNativeDataType();
        parseArrayMember();
        listLexer.match("semi");
    }

    /**
     * <valueExpr>: <vExpr> |
     *     <number> |
     *     <str_const>
     * @throws CriticalProductionException
     */
    private void parseValueExpr() throws CriticalProductionException {
        if (listLexer.getLookahead().getType().equals("id")) {
            parseVExpr();
        }

        boolean matchAny = parseNumber();

        if (!matchAny) {
            matchAny = parseStrConst();
        }

        if (!matchAny) {
            throw new CriticalProductionException("Found wrong value expression");
        }
    }

    /**
     * <str_const>: str_literal
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
     * <number>: numeric_constant
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
    private void parseVExpr() throws CriticalProductionException {
        listLexer.match("id");
        parseVExprChanger();
    }

    /**
     * <vExprChanger>: <arrayMember> |
     *      (<argsCallListChanger>) |
     *      E
     * @throws OptionalProductionException
     */
    private void parseVExprChanger() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("l_square")) {
            parseArrayMember();
        } else if (curTypeToken.equals("l_paren")) {
            listLexer.match("l_paren");
            parseArgsCallListChanger();
            listLexer.match("r_paren");
        }
    }

    /**
     * <argsCallListChanger>: <argsCallList> | E
     * @throws OptionalProductionException
     */
    private void parseArgsCallListChanger() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        if (curTypeToken.equals("id") ||
            curTypeToken.equals("numeric_constant") ||
                curTypeToken.equals("str_literal")) {
            parseArgsCallList();
        }
    }

    /**
     * <argsCallList>: <valueExpr> | <argsCallList>, <argsCallList>
     * @throws CriticalProductionException
     */
    private void parseArgsCallList() throws CriticalProductionException {
        parseValueExpr();

        boolean matchComma = false;

        try {
            listLexer.match(",");
            matchComma = true;
        } catch (CriticalProductionException exc) { }

        if(matchComma) {
            parseArgsCallList();
        }
    }

    /**
     * <arrayMember>: [<number>]
     * @throws OptionalProductionException
     */
    private void parseArrayMember() throws CriticalProductionException {
        listLexer.match("l_square");
        listLexer.match("numeric_constant");
        listLexer.match("r_square");
    }

    /**
     * <statementList>: <statement> <statementList> | E
     */
    private void parseStatementList() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();

        if (curTokenType.equals("l_brace") ||
                curTokenType.equals("while") ||
                curTokenType.equals("if") ||
                curTokenType.equals("numeric_constant") ||
                curTokenType.equals("str_literal") ||
                curTokenType.equals("id") ||
                curTokenType.equals("int") ||
                curTokenType.equals("char") ||
                curTokenType.equals("return")) {
            parseStatement();
        } else {
            return;
        }

        parseStatementList();
    }

    /**
     * <statement>: { <statement> } |
     *     <loop> |
     *     <conditional> |
     *     <expression>; |
     *     <init> |
     *
     */
    private void parseStatement() throws CriticalProductionException { // todo work this
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("l_brace")) {
            listLexer.match("l_brace");
            parseStatement();
            listLexer.match("l_brace");
        } else if (curTypeToken.equals("while")) {
            parseLoop();
        } else if (curTypeToken.equals("if")) {
            parseConditional();
        } else if (curTypeToken.equals("id") ||
                curTypeToken.equals("numeric_constant") ||
                curTypeToken.equals("str_literal")) {
            parseExpression();
            listLexer.match("semi"); // todo check this for correction
        } else if (curTypeToken.equals("int") ||
                curTypeToken.equals("char")) {
            parseInit();
        } else if (curTypeToken.equals("return")) {
            parseReturn();
        }
    }

    /**
     * <return>: return <expression>;
     * @throws CriticalProductionException
     */
    private void parseReturn() throws CriticalProductionException {
        listLexer.match("return");
        parseExpression();
    }

    /**
     * <expression>: <valueExpr> <condition> <valueExpr> |
     *     <expression> <expressionOptionOperator> |
     *     <id> <expressionOption> |
     *     <number> |
     *     <str_const>
     * @throws CriticalProductionException
     */
    private void parseExpression() throws CriticalProductionException { // todo: i dont like this function
        boolean matchAny = false;

        try {
            parseExpression();
            try {
                parseExpressionOptionOperator();
            } catch (CriticalProductionException exc) {}

            matchAny = true;
        } catch (CriticalProductionException exc) { }

        if (!matchAny) {
            try {
                parseExpression();
                parseLogicOperator();
                parseExpression();

                matchAny = true;
            } catch (CriticalProductionException exc) { }
        }

        if (!matchAny) {
            try {
                listLexer.match("id");
                try {
                    parseExpressionOption();
                } catch (OptionalProductionException e) { }

                matchAny = true;
            } catch (CriticalProductionException exc) { }
        }

        if (!matchAny) {
            matchAny = parseNumber();
        }

        if (!matchAny) {
            matchAny = parseStrConst();
        }

        if (!matchAny) {
            try {
                parseValueExpr();
                parseConditions();
                parseValueExpr();

                matchAny = true;
            } catch (CriticalProductionException exc) { }
        }

        if (!matchAny) {
            throw new CriticalProductionException("ERROR");
        }
    }

    /**
     * <expressionOption>: <arrayMember> |
     *     (<argsCallListChanger>) |
     *      = <expression> |
     *     E
     * @throws OptionalProductionException
     */
    private void parseExpressionOption() throws OptionalProductionException {
        boolean matchAny = false;

        try {
            parseArrayMember();
            matchAny = true;
        } catch (CriticalProductionException e) { }

        if (!matchAny) {
            try {
                listLexer.match("l_paren");
                parseArgsCallListChanger();
                listLexer.match("r_paren");

                matchAny = true;
            } catch (CriticalProductionException e) { }
        }

        if (!matchAny) {
            try {
                listLexer.match("equal");
                parseExpression();
            } catch (CriticalProductionException e) { }
        }

        if (!matchAny) {
            throw new OptionalProductionException();
        }
    }

    /**
     * <expressionOptionOperator>: <logicOperator> <expression> |
     *     <operator> <expression>
     * @throws CriticalProductionException
     */
    private void parseExpressionOptionOperator() throws CriticalProductionException { // todo write this
        boolean matchAny = false;

        try {
            parseLogicOperator();
            parseExpression();

            matchAny = true;
        } catch (CriticalProductionException exc) { }

        if (!matchAny) {
            parseOperator();
            parseExpression();
        }
    }

    /**
     * <operator>: + | - | * | / | %
     * @throws CriticalProductionException
     */
    private void parseOperator() throws CriticalProductionException {
        listLexer.matchOneOf("plus", "minus", "star", "slash", "percent");
    }

    /**
     * <logicOperator>: && | ||
     * @throws CriticalProductionException
     */
    private void parseLogicOperator() throws CriticalProductionException {
        listLexer.matchOneOf("ampamp", "pipepipe");
    }

    /**
     * <conditional>:
     *      if (<expression>) {
     *         <statementList>
     *      }
     * @throws CriticalProductionException
     */
    private void parseConditional() throws CriticalProductionException {
        listLexer.match("if");
        listLexer.match("l_paren");
        parseExpression();
        listLexer.match("r_paren");
        listLexer.match("l_brace");
        parseStatementList();
        listLexer.match("r_brace");
        parseElseFork();
    }

    /**
     * <elseFork>:
     *     else <elseFork1> |
     *     E
     * @throws CriticalProductionException
     */
    private void parseElseFork() throws CriticalProductionException {
        try {
            listLexer.match("else");
        } catch (CriticalProductionException exc) {
            return; // E
        }

        parseElseFork1();
    }

    /**
     * <elseFork1>:
     *     <conditional> |
     *     { <statementList> } |
     *     E
     * @throws CriticalProductionException
     */
    private void parseElseFork1() throws CriticalProductionException {
        if (listLexer.getLookahead().getType().equals("if")) {
            parseConditional();
        }

        listLexer.match("l_brace");
        parseStatementList();
        listLexer.match("r_brace");
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
        parseStatementList();
        listLexer.match("r_brace");
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
        parseStatementList();
        listLexer.match("r_brace");
    }
}
