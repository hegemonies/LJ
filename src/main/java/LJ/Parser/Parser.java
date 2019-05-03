package LJ.Parser;

import LJ.Lexer.ListLexer;
import LJ.Parser.AST.Node;
import LJ.Parser.ParserException.CriticalProductionException;
import LJ.Parser.ParserException.OptionalProductionException;

import java.util.Objects;

public class Parser {
    private ListLexer listLexer;
    Node root = new Node(Objects.requireNonNull(listLexer).getLookahead());
    Node currentNode = root;

    public Parser(ListLexer listLexer) {
        this.listLexer = listLexer;
    }

    public void go() {
        try {
            listLexer.match("program");
        } catch (CriticalProductionException e) {
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
//        listLexer.matchOneOf("public", "private");
        boolean matchAny = false;

        try {
            listLexer.match("public");
            matchAny = true;
        } catch (CriticalProductionException exc) { }

        if (!matchAny) {
            try {
                listLexer.match("private");
                matchAny = true;
            } catch (CriticalProductionException exc) { }
        }

        if (matchAny) {
            currentNode.addChild(new Node(listLexer.getLookback())); //todo check
        }

    }

    /**
     * <argsInitListChanger>: <argsInitList> | E
     * @throws OptionalProductionException
     */
    private void parseArgsInitListChanger() throws OptionalProductionException {
        try {
            parseArgsInitList();
        } catch (CriticalProductionException e) {
            throw new OptionalProductionException();
        }
    }

    /**
     * <initList>: <init> <initList> | E
     * @throws CriticalProductionException
     */
    private void parseInitList() throws OptionalProductionException {
        try {
            parseInit();
            parseInitList();
        } catch (CriticalProductionException e) {
            throw new OptionalProductionException();
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
            try {
                listLexer.match("id");
                matchAny = true;
            } catch (CriticalProductionException e) {
                matchAny = false;
            }
        }

        if (!matchAny) {
            throw new CriticalProductionException("ERROR");
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
    private void parseValueExpr() throws CriticalProductionException {
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
    private void parseArgsCallList() throws CriticalProductionException {
        parseValueExpr();

        try {
            listLexer.match(",");
            parseArgsCallList();
        } catch (CriticalProductionException exc) { }
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
            parseStatementList();
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
    private void parseStatement() throws CriticalProductionException {
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
     * <conditional>: if (<expression>) {
     *         <statementList>
     *     }
     * @throws CriticalProductionException
     */
    private void parseConditional() throws CriticalProductionException {
        listLexer.match("if");
        listLexer.match("l_paren");
        parseExpression();
        listLexer.match("r_paren");
        listLexer.match("l_brace");
        try {
            parseStatementList();
        } catch (OptionalProductionException e) { }
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
        try {
            parseStatementList();
        } catch (OptionalProductionException e) { }
        listLexer.match("r_brace");
    }

    /**
     * <argsInitList>:  <argInit> | <argsInitList>, <argsInitList>
     * @throws CriticalProductionException
     */
    private void parseArgsInitList() throws CriticalProductionException {
        parseArgInit();

        try {
            listLexer.match("comma");
            parseArgsInitList();
        } catch (CriticalProductionException exc) { }
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
