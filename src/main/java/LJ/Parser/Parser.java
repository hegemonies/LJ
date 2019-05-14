package LJ.Parser;

import LJ.Lexer.Lexer;
import LJ.Lexer.ListLexer;
import LJ.Parser.AST.ASTNode;
import LJ.Parser.ParserException.CriticalProductionException;
import LJ.Parser.ParserException.OptionalProductionException;

// todo: refactor AST on HeterogenAST

public class Parser {
    private ListLexer listLexer;
    ASTNode root;

    public Parser(ListLexer listLexer) {
        this.listLexer = listLexer;
    }

    public void go() {
        try {
            root = new ASTNode(listLexer.getLookahead());
            listLexer.match("program");
            root.addChild(parseProgram());
        } catch (CriticalProductionException e) {
            e.printStackTrace();
        }
    }

    public void showTree() {
        System.out.println(root.toStringTree());
    }

    /**
     * <Program>: <class>
     */
    private ASTNode parseProgram() throws CriticalProductionException {
        return parseClass();
    }

    /**
     * <class>: <modAccessClass> class <id> {
     *         <initList>
     *         <mainMethod>
     *     }
     * @throws CriticalProductionException
     */
    private ASTNode parseClass() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        node.addChild(parseModAccessClass());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("class");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("id");

//        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        node.addChild(parseInitList());
        node.addChild(parseMainMethod());

//        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        return node;
    }

    /**
     * <modAccessClass>: public | private
     * @throws CriticalProductionException
     */
    private ASTNode parseModAccessClass() throws CriticalProductionException {
        ASTNode node = new ASTNode(listLexer.getLookahead());
        listLexer.matchOneOf("public", "private");

        return node;
    }

    /**
     * <initList>: <init> <initList> | E
     * @throws CriticalProductionException
     */
    private ASTNode parseInitList() throws CriticalProductionException { // todo dont forget about this
        String curTokenType = listLexer.getLookahead().getType();
        ASTNode node = new ASTNode();

        if (curTokenType.equals("int") ||
                curTokenType.equals("char")) {
            node.addChild(parseInit());
        } else {
            return node;
        }

        node.addChild(parseInitList());

        return node;
    }

    /**
     * <argsInitListChanger>: <argsInitList> | E
     * @throws OptionalProductionException
     */
    private ASTNode parseArgsInitListChanger() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();

        if (curTokenType.equals("int") ||
                curTokenType.equals("char")) {
            ASTNode node = new ASTNode();
            node.addChild(parseArgsInitList());
        }

        return null;
    }

    /**
     * <argsInitList>:  <argInit> | <argsInitList>, <argsInitList>
     * @throws CriticalProductionException
     */
    private ASTNode parseArgsInitList() throws CriticalProductionException {
        ASTNode node = new ASTNode();
        node.addChild(parseArgInit());

        if (listLexer.getLookahead().getType().equals("comma")) {
            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("comma");

            node.addChild(parseArgsInitList());
        }

        return node;
    }

    /**
     * <argInit>:
     *     <nativeDataType><rvalueFork> <id>
     * @throws CriticalProductionException
     */
    private ASTNode parseArgInit() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        node.addChild(parseNativeDataType());
        node.addChild(parseRValueFork());
        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("id");

        return node;
    }

    /**
     * <rvalueFork>: [] | E
     * @throws CriticalProductionException
     */
    private ASTNode parseRValueFork() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        ASTNode node = new ASTNode();

        if (curTypeToken.equals("l_square")) {
            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("l_square");

            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("r_square");
        }

        return node;
    }

    /**
     * <init>: <nativeDataType> <id> <forkInit>
     * @throws CriticalProductionException
     */
    private ASTNode parseInit() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        node.addChild(parseNativeDataType());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("id");

        node.addChild(parseForkInit());

        return node;
    }

    /**
     * <nativeDataType>: int | char
     * @throws CriticalProductionException
     */
    private ASTNode parseNativeDataType() throws CriticalProductionException {
        ASTNode node = new ASTNode(listLexer.getLookahead());
        listLexer.matchOneOf("int", "char");

        return node;
    }

    /**
     * <forkInit>: <forInitFunc> |
     *     <forkInitVar> |
     *     <forInitArray>
     */
    private ASTNode parseForkInit() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();
        ASTNode node = new ASTNode();

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
    private ASTNode parseForkInitFunc() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("l_paren");

        node.addChild(parseArgsInitListChanger());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("r_paren");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        node.addChild(parseStatementList());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        return node;
    }

    /**
     * <forkInitVar>: = <valueExpr>;
     * @return
     */
    private ASTNode parseForkInitVar() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("equal");

        node.addChild(parseValueExpr());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("semicolon");

        return node;
    }

    /**
     * <forkInitArray>: <arrayMember> = new <nativeDataType><arrayMember>;
     * @return
     */
    private ASTNode parseForkInitArray() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        node.addChild(parseArrayMember());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("equal");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("new");

        node.addChild(parseNativeDataType());
        node.addChild(parseArrayMember());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("semicolon");

        return node;
    }

    /**
     * <valueExpr>: <vExpr> |
     *     <number> |
     *     <str_const>
     * @throws CriticalProductionException
     */
    private ASTNode parseValueExpr() throws CriticalProductionException {
        boolean matchAny = false;
        String curTypeToken = listLexer.getLookahead().getType();
        ASTNode node = new ASTNode();

        if (curTypeToken.equals("id")) {
            node.addChild(parseVExpr());
            matchAny = true;
        }

        if (curTypeToken.equals("numeric_constant") ||
                curTypeToken.equals("minus")) {
            node.addChild(parseNumber());
            matchAny = true;
        }

        if (curTypeToken.equals("str_literal")) {
            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("str_literal");
            matchAny = true;
        }

        if (!matchAny) {
            throw new CriticalProductionException("expecting <" + "id or numeric_constant or str_literal"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }

        return node;
    }

    /**
     * <str_const>: str_literal
     * @throws CriticalProductionException
     */
    private ASTNode parseStrConst() throws CriticalProductionException {
        ASTNode node = new ASTNode(listLexer.getLookahead());
        listLexer.match("str_literal");

        return node;
    }

    /**
     * <number>: <sign> numeric_constant
     * @throws CriticalProductionException
     */
    private ASTNode parseNumber() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        if (listLexer.getLookahead().getType().equals("minus")) {
            node.setToken(listLexer.getLookahead());
        }
        parseSign();

        if (node.isNull()) {
            node.setToken(listLexer.getLookahead());
        }
        listLexer.match("numeric_constant");

        return node;
    }

    /**
     * <sign>: - | E
     * @throws CriticalProductionException
     */
    private void parseSign() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("minus")) {
            listLexer.match("minus");
        }
    }

    /**
     * <vExpr>: <id> <vExprChange>
     * @throws CriticalProductionException
     */
    private ASTNode parseVExpr() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("id");

        node.addChild(parseVExprChanger());

        return node;
    }

    /**
     * <vExprChanger>: <arrayMember> |
     *      (<argsCallListChanger>) |
     *      E
     * @throws OptionalProductionException
     */
    private ASTNode parseVExprChanger() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        ASTNode node = new ASTNode();

        if (curTypeToken.equals("l_square")) {
            node.addChild(parseArrayMember());
        } else if (curTypeToken.equals("l_paren")) {
            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("l_paren");

            node.addChild(parseArgsCallListChanger());

            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("r_paren");
        }

        return node;
    }

    /**
     * <argsCallListChanger>: <argsCallList> | E
     * @throws OptionalProductionException
     */
    private ASTNode parseArgsCallListChanger() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        ASTNode node = new ASTNode();

        if (curTypeToken.equals("id") ||
                curTypeToken.equals("numeric_constant") ||
                curTypeToken.equals("minus") ||
                curTypeToken.equals("str_literal")) {
            node.addChild(parseArgsCallList());
        }

        return node;
    }

    /**
     * <argsCallList>: <valueExpr> | <argsCallList>, <argsCallList>
     * @throws CriticalProductionException
     */
    private ASTNode parseArgsCallList() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        node.addChild(parseValueExpr());

        if (listLexer.getLookahead().getType().equals("comma")) {
            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("comma");

            node.addChild(parseArgsCallList());
        }

        return node;
    }

    /**
     * <arrayMember>:
     *     [<arrayMemberFork>]
     * @throws OptionalProductionException
     */
    private ASTNode parseArrayMember() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("l_square");

        node.addChild(parseArrayMemberFork());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("r_square");

        return node;
    }

    /**
     * <arrayMemberFork>:
     *     <number> |
     *     <id>
     * @throws CriticalProductionException
     */
    private ASTNode parseArrayMemberFork() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        ASTNode node = new ASTNode();

        if (curTypeToken.equals("numeric_constant")) {
            node.addChild(parseNumber());
        } else if (curTypeToken.equals("id")) {
            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("id");
        } else {
            throw new CriticalProductionException("expecting <id or numeric_constant"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }

        return node;
    }

    /**
     * <statementList>: <statement> <statementList> | E
     */
    private ASTNode parseStatementList() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();
        ASTNode node = new ASTNode();


        if (curTokenType.equals("l_brace") ||
                curTokenType.equals("while") ||
                curTokenType.equals("if") ||
                curTokenType.equals("numeric_constant") ||
                curTokenType.equals("minus") ||
                curTokenType.equals("str_literal") ||
                curTokenType.equals("id") ||
                curTokenType.equals("int") ||
                curTokenType.equals("char") ||
                curTokenType.equals("return") ||
                curTokenType.equals("semicolon")) {
            node.addChild(parseStatement());
            node.addChild(parseStatementList());
            // st = parseStatement
            // return st ++ parseStatementList()
        }
        //else {
        //    return node;
        //}

        //node.addChild(parseStatementList());

        return node;
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
    private ASTNode parseStatement() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        ASTNode node = new ASTNode();


        if (curTypeToken.equals("l_brace")) {
            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("l_brace");

            node.addChild(parseStatement());

            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("l_brace");
        } else if (curTypeToken.equals("while")) {
            node.addChild(parseLoop());
        } else if (curTypeToken.equals("if")) {
            node.addChild(parseConditional());
        } else if (curTypeToken.equals("id") ||
                curTypeToken.equals("numeric_constant") ||
                curTypeToken.equals("minus") ||
                curTypeToken.equals("str_literal")) {
            node.addChild(parseExpression());
        } else if (curTypeToken.equals("int") ||
                curTypeToken.equals("char")) {
            node.addChild(parseInit());
        } else if (curTypeToken.equals("semicolon")) {
            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("semicolon");
        } else if (curTypeToken.equals("return")) {
            node.addChild(parseReturn());
        }

        return node;
    }

    /**
     * <return>: return <expression>;
     * @throws CriticalProductionException
     */
    private ASTNode parseReturn() throws CriticalProductionException {
        ASTNode node = new ASTNode(listLexer.getLookahead());
        listLexer.match("return");

        node.addChild(parseExpression());

        return node;
    }

    /**
     * <expression2>:
     *     <arithmetic> <valueFork> <expressionOptionOperator>
     * @throws CriticalProductionException
     */
    private ASTNode parseExpression() throws CriticalProductionException {
        ASTNode node = new ASTNode();
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("id") ||
                curTypeToken.equals("numeric_constant") ||
                curTypeToken.equals("str_literal") ||
                curTypeToken.equals("l_paren")) {

            parseArithmetic();
            parseValueFork();
            parseExpressionOptionOperator();
        } else {
            throw new CriticalProductionException("expecting <" + "id or numeric_constant or str_literal or l_paren"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }

        return node;
    }

    /**
     * <valueFork>:
     *     <condition> <expression> |
     *     ; |
     *     E
     * @throws CriticalProductionException
     */
    private ASTNode parseValueFork() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();
        ASTNode node = new ASTNode();

        if (curTokenType.equals("less") ||
                curTokenType.equals("greater") ||
                curTokenType.equals("equalequal") ||
                curTokenType.equals("exclaimequal") ||
                curTokenType.equals("equal")) {
            node.addChild(parseConditions());
            node.addChild(parseExpression());
        } else if (curTokenType.equals("semicolon")) {
            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("semicolon");
        }

        return node;
    }

    /**
     * <expressionOptionOperator>:
     *     <logicOperator> <expression> |
     *     <condition> <expression> |
     *     E
     * @throws CriticalProductionException
     */
    private void parseExpressionOptionOperator() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("ampamp") ||
                curTypeToken.equals("pipepipe")) {
            parseLogicOperator();
            parseExpression();
        } else if (curTypeToken.equals("less") ||
                curTypeToken.equals("greater") ||
                curTypeToken.equals("equalequal") ||
                curTypeToken.equals("exclaimequal")) {
            parseConditions();
            parseExpression();
        }
    }

    /**
     * <arithmetic>:
     *      <secondPrior>
     * @throws CriticalProductionException
     */
    private void parseArithmetic() throws CriticalProductionException { // todo first work here
        parseSecondPrior();
    }

    /**
     * <secondPrior>:
     *      <firstPrior><secondPrior_>
     * @throws CriticalProductionException
     */
    private void parseSecondPrior() throws CriticalProductionException {
        parseFirstPrior();
        parseSecondPrior_();
    }

    /**
     * <secondPrior_>:
     *      <secondPriorOper><firstPrior><secondPrior_> |
     *      E
     * @throws CriticalProductionException
     */
    private void parseSecondPrior_() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        if (curTypeToken.equals("plus") ||
                curTypeToken.equals("minus")) {
            parseSecondPriorOper();
            parseFirstPrior();
            parseSecondPrior_();
        }
    }

    /**
     * <firstPrior>:
     *      <group><firstPrior_>
     * @throws CriticalProductionException
     */
    private void parseFirstPrior() throws CriticalProductionException {
        parseGroup();
        parseFirstPrior_();
    }

    /**
     * <firstPrior_>:
     *      <firstPriorOper><group><firstPrior_> |
     *      E
     * @throws CriticalProductionException
     */
    private void parseFirstPrior_() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("star") ||
                curTypeToken.equals("slash")) {
            parseFirstPriorOper();
            parseGroup();
            parseFirstPrior_();
        }
    }

    /**
     * <group>:
     *      (<arithmetic> <valueFork>) |
     *      <valueExpr>
     * @throws CriticalProductionException
     */
    private void parseGroup() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("l_paren")) {
            listLexer.match("l_paren");
            parseArithmetic();
            parseValueFork();
            listLexer.match("r_paren");
        } else if (curTypeToken.equals("id") ||
                    curTypeToken.equals("numeric_constant") ||
                    curTypeToken.equals("str_literal")) {
            parseValueExpr();
        } else {
            throw new CriticalProductionException("expecting <" + "l_paren or id or numeric_constant or str_literal"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }
    }

    /**
     * <secondPriorOper>: + | - | =
     * @throws CriticalProductionException
     */
    private void parseSecondPriorOper() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("plus")) {
            listLexer.match("plus");
        } else if (curTypeToken.equals("minus")) {
            listLexer.match("minus");
        } else if (curTypeToken.equals("equal")) {
            listLexer.match("equal");
        } else {
            throw new CriticalProductionException("expecting <" + "plus or minus"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }
    }

    /**
     * <firstPriorOper>: * | -
     * @throws CriticalProductionException
     */
    private void parseFirstPriorOper() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("star")) {
            listLexer.match("star");
        } else if (curTypeToken.equals("slash")) {
            listLexer.match("slash");
        } else {
            throw new CriticalProductionException("expecting <" + "star or slash"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }
    }

    /**
     * <operator>: + | - | * | / | =
     * @throws CriticalProductionException
     */
    private void parseOperator() throws CriticalProductionException {
        listLexer.matchOneOf("plus", "minus", "star", "slash", "equal");
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
    private ASTNode parseConditional() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("if");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("l_paren");

        node.addChild(parseExpression());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("r_paren");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        node.addChild(parseStatementList());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        node.addChild(parseElseFork());

        return node;
    }

    /**
     * <elseFork>:
     *     else <elseFork1> |
     *     E
     * @throws CriticalProductionException
     */
    private ASTNode parseElseFork() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        if (listLexer.getLookahead().getType().equals("else")) {
            node.addChild(new ASTNode(listLexer.getLookahead()));
            listLexer.match("else");
        } else {
            return node;
        }

        node.addChild(parseElseFork1());

        return node;
    }

    /**
     * <elseFork1>:
     *     <conditional> |
     *     { <statementList> } |
     *     E
     * @throws CriticalProductionException
     */
    private ASTNode parseElseFork1() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        if (listLexer.getLookahead().getType().equals("if")) {
            node.addChild(parseConditional());
        }

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        node.addChild(parseStatementList());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        return node;
    }

    /**
     * <loop>: while (<expression>) {
     *         <statementList>
     *     }
     * @throws CriticalProductionException
     */
    private ASTNode parseLoop() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("while");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("l_paren");

        node.addChild(parseExpression());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("r_paren");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        node.addChild(parseStatementList());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        return node;
    }

    /**
     * <condition>: < | > | == | != | <= | >=
     */
    private ASTNode parseConditions() throws CriticalProductionException {
        ASTNode node = new ASTNode(listLexer.getLookahead());

        listLexer.matchOneOf("less",
                "greater",
                "equalequal",
                "exclaimequal",
                "lessequal",
                "greaterequal",
                "equal");

        return node;
    }

    /**
     * <mainMethod>: public static void main(String[] args) {
     *         <statementList>
     *     }
     * @throws CriticalProductionException
     */
    private ASTNode parseMainMethod() throws CriticalProductionException {
        ASTNode node = new ASTNode();

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("public");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("static");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("void");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.matchTypeAndCheckValue("id", "main");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("l_paren");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.matchTypeAndCheckValue("id", "String");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("l_square");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("r_square");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("id");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("r_paren");

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        node.addChild(parseStatementList());

        node.addChild(new ASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        return node;
    }
}
