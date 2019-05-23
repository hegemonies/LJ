package LJ.Parser;

import LJ.Lexer.ListLexer;
import LJ.Lexer.Token;
import LJ.Parser.AST.*;
import LJ.Parser.AST.ArrayMember.ArrayMember;
import LJ.Parser.AST.ArrayMember.ArrayMemberID;
import LJ.Parser.AST.ArrayMember.ArrayMemberNumber;
import LJ.Parser.AST.Else.NodeElse;
import LJ.Parser.AST.Else.NodeIfElse;
import LJ.Parser.AST.Else.NodeJustElse;
import LJ.Parser.AST.Inits.*;
import LJ.Parser.AST.Operator.Operator;
import LJ.Parser.AST.Value.Number;
import LJ.Parser.ParserException.CriticalProductionException;
import LJ.Parser.ParserException.OptionalProductionException;

import java.util.ArrayList;
import java.util.List;

/**
 * todo: what need to refactor?
 * expression
 * arithmetic
 * exprValue
 * ...
  */

public class Parser {
    private ListLexer listLexer;
    HomoASTNode homoRoot;
    NodeClass root;

    public Parser(ListLexer listLexer) {
        this.listLexer = listLexer;
    }

    public void go() {
        try {
            listLexer.match("program");
        } catch (CriticalProductionException e) {
            e.printStackTrace();
        }
    }

    public void showTree() {
//        System.out.println(homoRoot.toStringTree());
        System.out.println(homoRoot._toStringTreeRoot());
    }

    /**
     * <Program>: <class>
     */
    private void parseProgram() throws CriticalProductionException {
        root = parseClass();
    }

    /**
     * <class>: <modAccessClass> class <id> {
     *         <initList>
     *         <mainMethod>
     *     }
     * @throws CriticalProductionException
     */
    private NodeClass parseClass() throws CriticalProductionException {
        NodeClass node = new NodeClass();

        node.setModAccessToken(listLexer.getLookahead());
        parseModAccessClass();

        listLexer.match("class");

        node.setIdToken(listLexer.getLookahead());
        listLexer.match("id");

        listLexer.match("l_brace");

        List<NodeInit> initList = new ArrayList<>();
        parseInitList(initList);
        for (NodeInit init : initList) {
            node.addInit(init);
        }

        node.setMainMethod(parseMainMethod());

        listLexer.match("r_brace");

        return node;
    }

    /**
     * <modAccessClass>: public | private
     * @throws CriticalProductionException
     */
    private void parseModAccessClass() throws CriticalProductionException {
        listLexer.matchOneOf("public", "private");
    }

    /**
     * <initList>: <initInsideClass> <initList> | E
     * @throws CriticalProductionException
     */
    private void parseInitList(List<NodeInit> list) throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();

        if (curTokenType.equals("int") ||
                curTokenType.equals("char")) {
            list.add(parseInitInsideClass());
            parseInitList(list);
        }
    }

    /**
     * <argsInitListChanger>: <argsInitList> | E
     * @throws OptionalProductionException
     */
    private List<NodeArgsInit> parseArgsInitListChanger() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();

        if (curTokenType.equals("int") ||
                curTokenType.equals("char")) {
            List<NodeArgsInit> list = new ArrayList<>();
            parseArgsInitList(list);
            return list;
        }

        return null;
    }

    /**
     * <argsInitList>:  <argInit> | <argsInitList>, <argsInitList>
     * @throws CriticalProductionException
     */
    private void parseArgsInitList(List<NodeArgsInit> list) throws CriticalProductionException {
        list.add(parseArgInit());

        if (listLexer.getLookahead().getType().equals("comma")) {
            listLexer.match("comma");
            parseArgsInitList(list);
        }
    }

    /**
     * <argInit>:
     *     <nativeDataType><rvalueFork> <id>
     * @throws CriticalProductionException
     */
    private NodeArgsInit parseArgInit() throws CriticalProductionException {
        NodeArgsInit node = new NodeArgsInit();

        node.setDataType(listLexer.getLookahead());
        parseNativeDataType();
        boolean isArray = parseRValueFork();
        if (isArray) {
            node.setTypeInit(TypeInit.ARRAY);
        } else {
            node.setTypeInit(TypeInit.VAR);
        }

        node.setId(listLexer.getLookahead());
        listLexer.match("id");

        return node;
    }

    /**
     * <rvalueFork>: [] | E
     * @throws CriticalProductionException
     */
    private boolean parseRValueFork() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("l_square")) {
            listLexer.match("l_square");
            listLexer.match("r_square");
            return true;
        }

        return false;
    }

    /**
     * <initInsideClass>:
     *     <nativeDataType> <firstForkInitInsideClass>
     * @return
     * @throws CriticalProductionException
     */
    private NodeInit parseInitInsideClass() throws CriticalProductionException {
        NodeInit node = new NodeInit();

        node.setDataType(listLexer.getLookahead());
        parseNativeDataType();

        List<Token> id = new ArrayList<>(1);
        node.setForkInit(parseFirstForkInitInsideClass(id));
        node.setId(id.get(0));

        return node;
    }

    /**
     * <firstForkInitInsideClass>:
     *     [] <id> <forkInitArray> |
     *     <id> <secondForkInitInsideClass>
     * @return
     * @throws CriticalProductionException
     */
    private ForkInit parseFirstForkInitInsideClass(List<Token> id) throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        ForkInit forkInit;

        if (curTypeToken.equals("l_square")) {
            listLexer.match("l_square");
            listLexer.match("r_square");
            id.add(listLexer.getLookahead());
            listLexer.match("id");
            forkInit = parseForkInitArray();
        } else if (curTypeToken.equals("id")) {
            id.add(listLexer.getLookahead());
            listLexer.match("id");
            forkInit = parseSecondForkInitInsideClass();
        } else {
            throw new CriticalProductionException("expecting <" + "l_square or id"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }

        forkInit.chooseType();

        return forkInit;
    }

    /**
     * <secondForkInitInsideClass>:
     *     <forkInitFunc> |
     *     <forkInitVar>
     * @return
     * @throws CriticalProductionException
     */
    private ForkInit parseSecondForkInitInsideClass() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        ForkInit forkInit;

        if (curTypeToken.equals("l_paren")) {
            forkInit = parseForkInitFunc();
        } else if (curTypeToken.equals("equal")) {
            forkInit = parseForkInitVar();
        } else {
            throw new CriticalProductionException("expecting <" + "l_paren or equal"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }

        return forkInit;
    }

    /**
     * <initInsideFunc>:
     *     <nativeDataType> <firstForkInitInsideFunc>
     * <firstForkInitInsideFunc>:
     *      [] <id> <forkInitArray> |
     *      <id> <forkInitVar>
     * @return
     * @throws CriticalProductionException
     */
    /**
     * <firstForkInitInsideFunc>:
     *      *     [] <id> <forkInitArray> |
     *      *     <id> <forkInitVar>
     */
    private NodeInit parseInitInsideFunc() throws CriticalProductionException {
        NodeInit node = new NodeInit();
        node.setDataType(listLexer.getLookahead());
        parseNativeDataType();

        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("l_square")) {
            listLexer.match("l_square");
            listLexer.match("r_square");

            node.setId(listLexer.getLookahead());
            listLexer.match("id");

            ForkInit forkInit = parseForkInitArray();
            forkInit.chooseType();
            node.setForkInit(forkInit);
        } else if (curTypeToken.equals("id")) {
            node.setId(listLexer.getLookahead());
            listLexer.match("id");

            ForkInit forkInit = parseForkInitVar();
            forkInit.chooseType();
            node.setForkInit(forkInit);
        } else {
            throw new CriticalProductionException("expecting <" + "l_square or id"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }

        return node;
    }

    /**
     * <nativeDataType>: int | char
     * @throws CriticalProductionException
     */
    private void parseNativeDataType() throws CriticalProductionException {
        listLexer.matchOneOf("int", "char");
    }

    /**
     * <forInitFunc>: (<argsInitListChanger>) {
     *                    <statementList>
     *                }
     * @return
     */
    private ForkInit parseForkInitFunc() throws CriticalProductionException {
        ForkInitFunc node = new ForkInitFunc();

        listLexer.match("l_paren");

        List<NodeArgsInit> listArgs = parseArgsInitListChanger();
        if (listArgs != null) {
            for (NodeArgsInit arg : listArgs) {
                node.addArgInit(arg);
            }
        }

        listLexer.match("r_paren");

        listLexer.match("l_brace");

        List<NodeStatement> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for (NodeStatement statement : statementList) {
            node.addStatement(statement);
        }

        listLexer.match("r_brace");

        return node;
    }

    /**
     * <forkInitVar>:
     *      = <expression>
     * @return
     */
    private ForkInit parseForkInitVar() throws CriticalProductionException {
        ForkInitVar node = new ForkInitVar();

        listLexer.match("equal");

        node.setExpression(parseExpression());

        return node;
    }

    /**
     * <forkInitArray>:
     *      = new <nativeDataType><arrayMember>;
     * @return
     * @throws CriticalProductionException
     */
    private ForkInitArray parseForkInitArray() throws CriticalProductionException {
        ForkInitArray node = new ForkInitArray();

        listLexer.match("equal");
        listLexer.match("new");

        node.setDataType(listLexer.getLookahead());
        parseNativeDataType();

        node.setIndex(parseArrayMember());

        listLexer.match("semicolon");

        return node;
    }

    /**
     * <valueExpr>: <vExpr> |
     *     <number> |
     *     <str_const>
     * @throws CriticalProductionException
     */
    private HomoASTNode parseValueExpr() throws CriticalProductionException {
        boolean matchAny = false;
        String curTypeToken = listLexer.getLookahead().getType();
        HomoASTNode node = new HomoASTNode();

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
            node.addChild(new HomoASTNode(listLexer.getLookahead()));
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
    private HomoASTNode parseStrConst() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode(listLexer.getLookahead());
        listLexer.match("str_literal");

        return node;
    }

    /**
     * <number>: <sign> numeric_constant
     * @throws CriticalProductionException
     */
    private Number parseNumber() throws CriticalProductionException {
        Number node = new Number();

        boolean isNegativeNumber = parseSign();
        node.setNegative(isNegativeNumber);

        node.setValue(listLexer.getLookahead());
        listLexer.match("numeric_constant");

        return node;
    }

    /**
     * <sign>: - | E
     * @throws CriticalProductionException
     */
    private boolean parseSign() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("minus")) {
            listLexer.match("minus");
            return true;
        }

        return false;
    }

    /**
     * <vExpr>: <id> <vExprChange>
     * @throws CriticalProductionException
     */
    private HomoASTNode parseVExpr() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode();

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
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
    private HomoASTNode parseVExprChanger() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        HomoASTNode node = new HomoASTNode();

        if (curTypeToken.equals("l_square")) {
            node.addChild(parseArrayMember());
        } else if (curTypeToken.equals("l_paren")) {
            node.addChild(new HomoASTNode(listLexer.getLookahead()));
            listLexer.match("l_paren");

            node.addChild(parseArgsCallListChanger());

            node.addChild(new HomoASTNode(listLexer.getLookahead()));
            listLexer.match("r_paren");
        }

        return node;
    }

    /**
     * <argsCallListChanger>: <argsCallList> | E
     * @throws OptionalProductionException
     */
    private HomoASTNode parseArgsCallListChanger() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        HomoASTNode node = new HomoASTNode();

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
    private HomoASTNode parseArgsCallList() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode();

        node.addChild(parseValueExpr());

        if (listLexer.getLookahead().getType().equals("comma")) {
            node.addChild(new HomoASTNode(listLexer.getLookahead()));
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
    private ArrayMember parseArrayMember() throws CriticalProductionException {
        listLexer.match("l_square");

        ArrayMember node = parseArrayMemberFork();

        listLexer.match("r_square");

        return node;
    }

    /**
     * <arrayMemberFork>:
     *     <number> |
     *     <id>
     * @throws CriticalProductionException
     */
    private ArrayMember parseArrayMemberFork() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        ArrayMember arrayMember = null;

        if (curTypeToken.equals("numeric_constant") ||
                curTypeToken.equals("minus")) {
            arrayMember = new ArrayMemberNumber(parseNumber());
        } else if (curTypeToken.equals("id")) {
            arrayMember = new ArrayMemberID(listLexer.getLookahead());
            listLexer.match("id");
        } else {
            throw new CriticalProductionException("expecting <id or numeric_constant"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }

        return arrayMember;
    }

    /**
     * <statementList>: <statement> <statementList> | E
     */
    private void parseStatementList(List<NodeStatement> list) throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();

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
            list.add(parseStatement());
            parseStatementList(list);
        }
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
    private NodeStatement parseStatement() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        NodeStatement node = null;

        if (curTypeToken.equals("l_brace")) {
            listLexer.match("l_brace");
            node = parseStatement();
            listLexer.match("l_brace");
        } else if (curTypeToken.equals("while")) {
            node = parseLoop();
        } else if (curTypeToken.equals("if")) {
            node = parseConditional();
        } else if (curTypeToken.equals("id") ||
                curTypeToken.equals("numeric_constant") ||
                curTypeToken.equals("minus") ||
                curTypeToken.equals("str_literal")) {
            node = parseExpression();
        } else if (curTypeToken.equals("int") ||
                curTypeToken.equals("char")) {
            node = parseInitInsideFunc();
        } else if (curTypeToken.equals("semicolon")) {
            listLexer.match("semicolon");
        } else if (curTypeToken.equals("return")) {
            node = parseReturn();
        }

        return node;
    }

    /**
     * <return>: return <expression>;
     * @throws CriticalProductionException
     */
    private NodeReturn parseReturn() throws CriticalProductionException {
        NodeReturn node = new NodeReturn();

        node.setReturnToken(listLexer.getLookahead());
        listLexer.match("return");

        node.setExpression(parseExpression());

        return node;
    }

    /**
     * <expression2>:
     *     <arithmetic> <valueFork> <expressionOptionOperator>
     * @throws CriticalProductionException
     */
    private NodeExpression parseExpression() throws CriticalProductionException {
        NodeExpression node = new NodeExpression();
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("id") ||
                curTypeToken.equals("numeric_constant") ||
                curTypeToken.equals("str_literal") ||
                curTypeToken.equals("minus") ||
                curTypeToken.equals("l_paren")) {
            node.setArithmetic(parseArithmetic());
            node.setExprFork(parseExprFork());
        } else {
            throw new CriticalProductionException("expecting <" + "id or numeric_constant or str_literal or l_paren"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }

        return node;
    }

    /**
     * <exprFork>:
     *     <logicOperator> <expression> |
     *     <condition> <expression> |
     *     ; |
     *     E
     * @throws CriticalProductionException
     */
    private NodeExprFork parseExprFork() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        NodeExprFork node = new NodeExprFork();

        if (curTypeToken.equals("ampamp") ||
                curTypeToken.equals("pipepipe")) {
            node.setOperator(parseLogicOperator());
            node.setExpression(parseExpression());
        } else if (curTypeToken.equals("less") ||
                curTypeToken.equals("greater") ||
                curTypeToken.equals("equalequal") ||
                curTypeToken.equals("exclaimequal") ||
                curTypeToken.equals("equal")) {
            node.setOperator(parseConditions());
            node.setExpression(parseExpression());
        } else if (curTypeToken.equals("semicolon")) {
            listLexer.match("semicolon");
        }

        return node;
    }

    /**
     * <arithmetic>:
     *      <secondPrior>
     * @throws CriticalProductionException
     */
    private void parseArithmetic() throws CriticalProductionException {
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
            parseExprFork();
            listLexer.match("r_paren");
        } else if (curTypeToken.equals("id") ||
                    curTypeToken.equals("numeric_constant") ||
                    curTypeToken.equals("minus") ||
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
     * <logicOperator>: && | ||
     * @throws CriticalProductionException
     */
    private Operator parseLogicOperator() throws CriticalProductionException {
        Operator node = new Operator(listLexer.getLookahead());
        listLexer.matchOneOf("ampamp", "pipepipe");
        return node;
    }

    /**
     * <conditional>:
     *      if (<expression>) {
     *         <statementList>
     *      } <else>
     * @throws CriticalProductionException
     */
    private NodeConditional parseConditional() throws CriticalProductionException {
        NodeConditional node = new NodeConditional();

        node.setIfToken(listLexer.getLookahead());
        listLexer.match("if");

        listLexer.match("l_paren");

        node.setExpression(parseExpression());

        listLexer.match("r_paren");
        listLexer.match("l_brace");

        List<NodeStatement> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for (NodeStatement statement : statementList) {
            node.addStatement(statement);
        }

        listLexer.match("r_brace");

        node.setElseNode(parseElse());

        return node;
    }

    /**
     * <else>:
     *     else <elseFork> |
     *     E
     * @throws CriticalProductionException
     */
    /**
     * <elseFork>:
     *     <conditional> |
     *     { <statementList> } |
     *     E
     */
    private NodeElse parseElse() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("else")) {
            Token elseToken = listLexer.getLookahead();
            listLexer.match("else");

            curTypeToken = listLexer.getLookahead().getType();

            if (curTypeToken.equals("if")) {
                NodeIfElse nodeIfElse = new NodeIfElse();
                nodeIfElse.setElseToken(elseToken);
                nodeIfElse.setConditional(parseConditional());

                return nodeIfElse;
            } else if (curTypeToken.equals("l_brace")) {
                NodeJustElse nodeJustElse = new NodeJustElse();
                nodeJustElse.setElseToken(elseToken);
                listLexer.match("l_brace");

                List<NodeStatement> statementList = new ArrayList<>();
                parseStatementList(statementList);
                for (NodeStatement statement : statementList) {
                    nodeJustElse.addStatement(statement);
                }

                listLexer.match("r_brace");

                return nodeJustElse;
            }
        }

        return null;
    }

    /**
     * <loop>: while (<expression>) {
     *         <statementList>
     *     }
     * @throws CriticalProductionException
     */
    private NodeLoop parseLoop() throws CriticalProductionException {
        NodeLoop node = new NodeLoop();

        node.setWhileToken(listLexer.getLookahead());
        listLexer.match("while");

        listLexer.match("l_paren");

        node.setExpression(parseExpression());

        listLexer.match("r_paren");
        listLexer.match("l_brace");

        List<NodeStatement> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for (NodeStatement statement : statementList) {
            node.addStatement(statement);
        }

        listLexer.match("r_brace");

        return node;
    }

    /**
     * <condition>: < | > | == | != | <= | >=
     */
    private Operator parseConditions() throws CriticalProductionException {
        Operator node = new Operator(listLexer.getLookahead());

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
    private NodeMainMethod parseMainMethod() throws CriticalProductionException {
        NodeMainMethod node = new NodeMainMethod();

        node.setPublicToken(listLexer.getLookahead());
        listLexer.match("public");

        node.setStaticToken(listLexer.getLookahead());
        listLexer.match("static");

        node.setVoidToken(listLexer.getLookahead());
        listLexer.match("void");

        node.setIdMainToken(listLexer.getLookahead());
        listLexer.matchTypeAndCheckValue("id", "main");

        listLexer.match("l_paren");

        node.setGettingDataTypeToken(listLexer.getLookahead());
        listLexer.matchTypeAndCheckValue("id", "String");

        listLexer.match("l_square");
        listLexer.match("r_square");

        node.setIdArgsToken(listLexer.getLookahead());
        listLexer.match("id");

        listLexer.match("r_paren");

        listLexer.match("l_brace");

        List<NodeStatement> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for (NodeStatement statement : statementList) {
            node.addStatement(statement);
        }

        listLexer.match("r_brace");

        return node;
    }
}
