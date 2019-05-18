package LJ.Parser;

import LJ.Lexer.ListLexer;
import LJ.Lexer.Token;
import LJ.Parser.AST.*;
import LJ.Parser.ParserException.CriticalProductionException;
import LJ.Parser.ParserException.OptionalProductionException;

import java.util.ArrayList;
import java.util.List;


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

        List<HomoASTNode> initList = new ArrayList<>();
        parseInitList(initList); // todo refactor this
        for (HomoASTNode init : initList) {
            node.addChild(init);
        }

        node.setMainMethod(parseMainMethod());

        listLexer.match("r_brace");

        return node;
    }

    /**
     * <modAccessClass>: public | private
     * @throws CriticalProductionException
     */
    private HomoASTNode parseModAccessClass() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode(listLexer.getLookahead());
        listLexer.matchOneOf("public", "private");

        return node;
    }

    /**
     * <initList>: <initInsideClass> <initList> | E
     * @throws CriticalProductionException
     */
    private void parseInitList(List<NodeInitInsideClass> list) throws CriticalProductionException {
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
    private HomoASTNode parseArgsInitListChanger() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();

        if (curTokenType.equals("int") ||
                curTokenType.equals("char")) {
            HomoASTNode node = new HomoASTNode();
            node.addChild(parseArgsInitList());
        }

        return null;
    }

    /**
     * <argsInitList>:  <argInit> | <argsInitList>, <argsInitList>
     * @throws CriticalProductionException
     */
    private HomoASTNode parseArgsInitList() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode();
        node.addChild(parseArgInit());

        if (listLexer.getLookahead().getType().equals("comma")) {
            node.addChild(new HomoASTNode(listLexer.getLookahead()));
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
    private HomoASTNode parseArgInit() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode();

        node.addChild(parseNativeDataType());
        node.addChild(parseRValueFork());
        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("id");

        return node;
    }

    /**
     * <rvalueFork>: [] | E
     * @throws CriticalProductionException
     */
    private HomoASTNode parseRValueFork() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        HomoASTNode node = new HomoASTNode();

        if (curTypeToken.equals("l_square")) {
            node.addChild(new HomoASTNode(listLexer.getLookahead()));
            listLexer.match("l_square");

            node.addChild(new HomoASTNode(listLexer.getLookahead()));
            listLexer.match("r_square");
        }

        return node;
    }

    /**
     * <initInsideClass>:
     *     <nativeDataType> <firstForkInitInsideClass>
     * @return
     * @throws CriticalProductionException
     */
    private NodeInitInsideClass parseInitInsideClass() throws CriticalProductionException {
        NodeInitInsideClass node = new NodeInitInsideClass();

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

        return forkInit;
    }

    /**
     * <secondForkInitInsideClass>:
     *     <forkInitFunc> |
     *     <forkInitVar>
     * @return
     * @throws CriticalProductionException
     */
    private HomoASTNode parseSecondForkInitInsideClass() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        HomoASTNode node = new HomoASTNode();

        if (curTypeToken.equals("l_paren")) {
            node.addChild(parseForkInitFunc());
        } else if (curTypeToken.equals("equal")) {
            node.addChild(parseForkInitVar());
        } else {
            throw new CriticalProductionException("expecting <" + "l_paren or equal"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }

        return node;
    }

    /**
     * <initInsideFunc>:
     *     <nativeDataType> <firstForkInitInsideFunc>
     * @return
     * @throws CriticalProductionException
     */
    private void parseInitInsideFunc(List<HomoASTNode> list) throws CriticalProductionException {
        list.add(parseNativeDataType());
        List<HomoASTNode> firstInitList = new ArrayList<>();
        parseFirstForkInitInsideFunc(firstInitList);
        for (HomoASTNode node : firstInitList) {
            list.add(node);
        }
    }

    /**
     * <firstForkInitInsideFunc>:
     *     [] <id> <forkInitArray> |
     *     <id> <forkInitVar>
     * @return
     * @throws CriticalProductionException
     */
    private void parseFirstForkInitInsideFunc(List<HomoASTNode> list) throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("l_square")) {
            listLexer.match("l_square");
            listLexer.match("r_square");
            list.add(new HomoASTNode(listLexer.getLookahead()));
            listLexer.match("id");

            list.add(parseForkInitArray());
        } else if (curTypeToken.equals("id")) {
            list.add(new HomoASTNode(listLexer.getLookahead()));
            listLexer.match("id");
            list.add(parseForkInitVar());
        } else {
            throw new CriticalProductionException("expecting <" + "l_square or id"
                    + ">, but found is <"+ listLexer.getLookahead().getType() +
                    ":" + listLexer.getLookahead().getValue()
                    + "> in " + listLexer.getLookahead().getLocation());
        }
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
    private HomoASTNode parseForkInitFunc() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode();

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("l_paren");

        node.addChild(parseArgsInitListChanger());

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("r_paren");

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        List<HomoASTNode> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for (HomoASTNode statement : statementList) {
            node.addChild(statement);
        }

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        return node;
    }

    /**
     * <forkInitVar>:
     *      = <expression>
     * @return
     */
    private HomoASTNode parseForkInitVar() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode();

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("equal");

        node.addChild(parseExpression());

        return node;
    }

    /**
     * <forkInitArray>:
     *      = new <nativeDataType><arrayMember>;
     * @return
     */
    private ForkInitArray parseForkInitArray() throws CriticalProductionException {
        ForkInitArray node = new ForkInitArray();

        listLexer.match("equal");
        listLexer.match("new");

        parseNativeDataType();
        node.setIndex(parseArrayMember());

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
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
    private NodeNumber parseNumber() throws CriticalProductionException {
        NodeNumber node = new NodeNumber();

        node.setPositive(parseSign());

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
    private boolean parseSign() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("minus")) {
            listLexer.match("minus");
            return false;
        }

        return true;
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
    private Token parseArrayMember() throws CriticalProductionException {
        listLexer.match("l_square");

        Token index = parseArrayMemberFork();

        listLexer.match("r_square");

        return index;
    }

    /**
     * <arrayMemberFork>:
     *     <number> |
     *     <id>
     * @throws CriticalProductionException
     */
    private Token parseArrayMemberFork() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();

        if (curTypeToken.equals("numeric_constant")) {
            parseNumber();
        } else if (curTypeToken.equals("id")) {
            node.addChild(new HomoASTNode(listLexer.getLookahead()));
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
    private void parseStatementList(List<HomoASTNode> list) throws CriticalProductionException {
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
    private HomoASTNode parseStatement() throws CriticalProductionException {
        String curTypeToken = listLexer.getLookahead().getType();
        HomoASTNode node = new HomoASTNode();


        if (curTypeToken.equals("l_brace")) {
            node.addChild(new HomoASTNode(listLexer.getLookahead()));
            listLexer.match("l_brace");

            node.addChild(parseStatement());

            node.addChild(new HomoASTNode(listLexer.getLookahead()));
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
            List<HomoASTNode> list = new ArrayList<>();
            parseInitInsideFunc(list);
            for (HomoASTNode _node : list) {
                node.addChild(_node);
            }
        } else if (curTypeToken.equals("semicolon")) {
            node.addChild(new HomoASTNode(listLexer.getLookahead()));
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
    private HomoASTNode parseReturn() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode(listLexer.getLookahead());
        listLexer.match("return");

        node.addChild(parseExpression());

        return node;
    }

    /**
     * <expression2>:
     *     <arithmetic> <valueFork> <expressionOptionOperator>
     * @throws CriticalProductionException
     */
    private HomoASTNode parseExpression() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode();
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
    private HomoASTNode parseValueFork() throws CriticalProductionException {
        String curTokenType = listLexer.getLookahead().getType();
        HomoASTNode node = new HomoASTNode();

        if (curTokenType.equals("less") ||
                curTokenType.equals("greater") ||
                curTokenType.equals("equalequal") ||
                curTokenType.equals("exclaimequal") ||
                curTokenType.equals("equal")) {
            node.addChild(parseConditions());
            node.addChild(parseExpression());
        } else if (curTokenType.equals("semicolon")) {
            node.addChild(new HomoASTNode(listLexer.getLookahead()));
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
    private HomoASTNode parseConditional() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode();

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("if");

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("l_paren");

        node.addChild(parseExpression());

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("r_paren");

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        List<HomoASTNode> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for (HomoASTNode statement : statementList) {
            node.addChild(statement);
        }

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
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
    private HomoASTNode parseElseFork() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode();

        if (listLexer.getLookahead().getType().equals("else")) {
            node.addChild(new HomoASTNode(listLexer.getLookahead()));
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
    private HomoASTNode parseElseFork1() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode();

        if (listLexer.getLookahead().getType().equals("if")) {
            node.addChild(parseConditional());
        }

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        List<HomoASTNode> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for (HomoASTNode statement : statementList) {
            node.addChild(statement);
        }

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        return node;
    }

    /**
     * <loop>: while (<expression>) {
     *         <statementList>
     *     }
     * @throws CriticalProductionException
     */
    private HomoASTNode parseLoop() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode();

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("while");

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("l_paren");

        node.addChild(parseExpression());

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("r_paren");

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        List<HomoASTNode> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for (HomoASTNode statement : statementList) {
            node.addChild(statement);
        }

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        return node;
    }

    /**
     * <condition>: < | > | == | != | <= | >=
     */
    private HomoASTNode parseConditions() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode(listLexer.getLookahead());

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
    private HomoASTNode parseMainMethod() throws CriticalProductionException {
        HomoASTNode node = new HomoASTNode(HomoASTNodeTypes.MAINMETHOD);

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("public");

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("static");

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("void");

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.matchTypeAndCheckValue("id", "main");

//        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("l_paren");

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.matchTypeAndCheckValue("id", "String");

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("l_square");

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("r_square");

        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("id");

//        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("r_paren");

//        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("l_brace");

        List<HomoASTNode> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for (HomoASTNode statement : statementList) {
            node.addChild(statement);
        }

//        node.addChild(new HomoASTNode(listLexer.getLookahead()));
        listLexer.match("r_brace");

        return node;
    }
}
