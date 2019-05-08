package LJ.Parser;

import LJ.Lexer.ListLexer;
import LJ.Parser.AST.GenericASTNode;
import LJ.Parser.ParserException.CriticalProductionException;
import LJ.Parser.ParserException.OptionalProductionException;

public class Parser {
    private ListLexer listLexer;
    GenericASTNode root;

    public Parser(ListLexer listLexer) {
        this.listLexer = listLexer;
    }

    public void go() {
        try {
            root = new GenericASTNode(listLexer.getLookahead());
            listLexer.match("program");
            root.addChild(parseProgram());
        } catch (CriticalProductionException e) {
            e.printStackTrace();
        }
    }

    /**
     * <Program>: <class>
     */
    private GenericASTNode parseProgram() throws CriticalProductionException {
        return parseClass();
    }

    /**
     * <class>: <modAccessClass> class <id> {
     *         <initList>
     *         <mainMethod>
     *     }
     * @throws CriticalProductionException
     */
    private GenericASTNode parseClass() throws CriticalProductionException {
        GenericASTNode node = new GenericASTNode();

        node.addChild(parseModAccessClass());

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("class");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("id");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        node.addChild(parseInitList());
        node.addChild(parseMainMethod());

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        return node;
    }

    /**
     * <modAccessClass>: public | private
     * @throws CriticalProductionException
     */
    private GenericASTNode parseModAccessClass() throws CriticalProductionException {
        GenericASTNode node = new GenericASTNode(listLexer.getLookahead());
        listLexer.matchOneOf("public", "private");

        return node;
    }

    /**
     * <initList>: <init> <initList> | E
     * @throws CriticalProductionException
     */
    private GenericASTNode parseInitList() throws CriticalProductionException { // todo dont forget about this
        String curTokenType = listLexer.getLookahead().getType();
        GenericASTNode node = new GenericASTNode();

        if (curTokenType.equals("int") ||
                curTokenType.equals("char")) {
            node.addChild(parseInit());
        } else {
            return node;
        }

        parseInitList();
    }

    /**
     * <argsInitListChanger>: <argsInitList> | E
     * @throws OptionalProductionException
     */
    private GenericASTNode parseArgsInitListChanger() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();

        if (curTokenType.equals("int") ||
                curTokenType.equals("char")) {
            GenericASTNode node = new GenericASTNode();
            node.addChild(parseArgsInitList());
        }

        return null;
    }

    /**
     * <argsInitList>:  <argInit> | <argsInitList>, <argsInitList>
     * @throws CriticalProductionException
     */
    private void parseArgsInitList() throws CriticalProductionException {
        parseArgInit();

        if (listLexer.getLookahead().getType().equals("comma")) {
            listLexer.match("comma");
            parseArgsInitList();
        }
    }

    /**
     * <argInit>:
     *     <nativeDataType><rvalueFork> <rvalue>
     * @throws CriticalProductionException
     */
    private void parseArgInit() throws CriticalProductionException {
        parseNativeDataType();
        parseRValueFork();
        parseRValue();
    }

    /**
     * <rvalue>:
     *     <number> |
     *     <str_const> |
     *     <id>
     * @throws CriticalProductionException
     */
    private void parseRValue() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("id")) {
            listLexer.match("id");
        } else if (curTypeToken.equals("numeric_constant")) {
            listLexer.match("numeric_constant");
        } else if (curTypeToken.equals("str_literal")) {
            listLexer.match("str_literal");
        } else {
            throw new CriticalProductionException("expecting <" + "id or numeric_constant or str_literal"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }
    }

    /**
     * <rvalueFork>: [] | E
     * @throws CriticalProductionException
     */
    private void parseRValueFork() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("l_square")) {
            listLexer.match("l_square");
            listLexer.match("r_square");
        }
    }

    /**
     * <init>: <nativeDataType> <id> <forkInit>
     * @throws CriticalProductionException
     */
    private GenericASTNode parseInit() throws CriticalProductionException {
        GenericASTNode node = new GenericASTNode();

        node.addChild(parseNativeDataType());

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("id");

        node.addChild(parseForkInit());

        return node;
    }

    /**
     * <nativeDataType>: int | char
     * @throws CriticalProductionException
     */
    private GenericASTNode parseNativeDataType() throws CriticalProductionException {
        GenericASTNode node = new GenericASTNode(listLexer.getLookahead());
        listLexer.matchOneOf("int", "char");

        return node;
    }

    /**
     * <forkInit>: <forInitFunc> |
     *     <forkInitVar> |
     *     <forInitArray>
     */
    private GenericASTNode parseForkInit() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();
        GenericASTNode node = new GenericASTNode();

        if (curTokenType.equals("l_paren")) {
            node.addChild(parseForkInitFunc());
        } else if (curTokenType.equals("equal")) {
            node.addChild(parseForkInitVar());
        } else if (curTokenType.equals("l_square")) {
            node.addChild(parseForkInitArray());
        }

        return node;
    }

    /**
     * <forInitFunc>: (<argsInitListChanger>) {
     *                    <statementList>
     *                }
     * @return
     */
    private GenericASTNode parseForkInitFunc() throws CriticalProductionException {
        GenericASTNode node = new GenericASTNode();

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("l_paren");

        node.addChild(parseArgsInitListChanger());

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("r_paren");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        node.addChild(parseStatementList());

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        return node;
    }

    /**
     * <forkInitVar>: = <valueExpr>;
     * @return
     */
    private GenericASTNode parseForkInitVar() throws CriticalProductionException {
        GenericASTNode node = new GenericASTNode();

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("equal");

        node.addChild(parseValueExpr());

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("semicolon");
    }

    /**
     * <forkInitArray>: <arrayMember> = new <nativeDataType><arrayMember>;
     * @return
     */
    private GenericASTNode parseForkInitArray() throws CriticalProductionException {
        GenericASTNode node = new GenericASTNode();

        node.addChild(parseArrayMember());

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("equal");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("new");

        node.addChild(parseNativeDataType());
        node.addChild(parseArrayMember());

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("semicolon");

        return node;
    }

    /**
     * <valueExpr>: <vExpr> |
     *     <number> |
     *     <str_const>
     * @throws CriticalProductionException
     */
    private void parseValueExpr() throws CriticalProductionException {
        boolean matchAny = false;
        if (listLexer.getLookahead().getType().equals("id")) {
            parseVExpr();
            matchAny = true;
        }

        if (!matchAny) {
            matchAny = parseNumber();
        }

        if (!matchAny) {
            matchAny = parseStrConst();
        }

        if (!matchAny) {
            throw new CriticalProductionException("expecting <" + "id or numeric_constant or str_literal"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
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
     * @throws CriticalProductionException
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
     * <arrayMember>:
     *     [<arrayMemberFork>]
     * @throws OptionalProductionException
     */
    private GenericASTNode parseArrayMember() throws CriticalProductionException {
        GenericASTNode node = new GenericASTNode();

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("l_square");

        node.addChild(parseArrayMemberFork());

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("r_square");

        return node;
    }

    /**
     * <arrayMemberFork>:
     *     <number> |
     *     <id>
     * @throws CriticalProductionException
     */
    private void parseArrayMemberFork() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("numeric_constant")) {
            listLexer.match("numeric_constant");
        } else if (curTypeToken.equals("id")) {
            listLexer.match("id");
        } else {
            throw new CriticalProductionException("expecting <id or numeric_constant"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }
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
                curTokenType.equals("return") ||
                curTokenType.equals("semicolon")) {
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
     *     <return> |
     *     ;
     * @throws CriticalProductionException
     */
    private void parseStatement() throws CriticalProductionException {
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
        } else if (curTypeToken.equals("int") ||
                curTypeToken.equals("char")) {
            parseInit();
        } else if (curTypeToken.equals("semicolon")) {
            listLexer.match("semicolon");
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
     * <expression>:
     *     <valueExpr> <valueFork> |
     *     <expression> <expressionOptionOperator>
     * @throws CriticalProductionException
     */
    private void parseExpression() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("id") ||
                curTypeToken.equals("numeric_constant") ||
                curTypeToken.equals("str_literal")) {
            parseValueExpr();
            parseValueFork();

            curTypeToken = listLexer.getLookahead().getType();
            if (curTypeToken.equals("ampamp") ||
                    curTypeToken.equals("pipepipe") ||
                    curTypeToken.equals("plus") ||
                    curTypeToken.equals("minus") ||
                    curTypeToken.equals("star") ||
                    curTypeToken.equals("slash") ||
                    curTypeToken.equals("percent")) {
                parseExpressionOptionOperator();
            }
        } else {
            throw new CriticalProductionException("expecting <" + "id or numeric_constant or str_literal"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }
    }

    /**
     * <valueFork>:
     *     <condition> <valueExpr> |
     *     ; |
     *     E
     * @throws CriticalProductionException
     */
    private void parseValueFork() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();

        if (curTokenType.equals("less") ||
                curTokenType.equals("greater") ||
                curTokenType.equals("equalequal") ||
                curTokenType.equals("exclaimequal") ||
                curTokenType.equals("equal")) {
            parseConditions();
            parseValueExpr();
        } else if (curTokenType.equals("semicolon")) {
            listLexer.match("semicolon");
        }
    }

    /**
     * <expressionOptionOperator>: <logicOperator> <expression> |
     *     <operator> <expression>
     * @throws CriticalProductionException
     */
    private void parseExpressionOptionOperator() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("ampamp") ||
                curTypeToken.equals("pipepipe")) {
            parseLogicOperator();
            parseExpression();
        } else if (curTypeToken.equals("plus") ||
                curTypeToken.equals("minus") ||
                curTypeToken.equals("star") ||
                curTypeToken.equals("slash") ||
                curTypeToken.equals("percent")) {
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
            return;
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
        listLexer.matchOneOf("less",
                "greater",
                "equalequal",
                "exclaimequal",
                "lessequal",
                "greaterequal",
                "equal");
    }

    /**
     * <mainMethod>: public static void main(String[] args) {
     *         <statementList>
     *     }
     * @throws CriticalProductionException
     */
    private GenericASTNode parseMainMethod() throws CriticalProductionException {
        GenericASTNode node = new GenericASTNode();

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("public");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("static");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("void");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.matchTypeAndCheckValue("id", "main");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("l_paren");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.matchTypeAndCheckValue("id", "String");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("l_square");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("r_square");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("id");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("r_paren");

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        node.addChild(parseStatementList());

        node.addChild(new GenericASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        return node;
    }
}
