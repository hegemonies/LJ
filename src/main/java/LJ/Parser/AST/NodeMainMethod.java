package LJ.Parser.AST;

import LJ.Lexer.Token;
import LJ.Parser.AST.Statement.NodeStatement;

import java.util.ArrayList;
import java.util.List;

public class NodeMainMethod implements Node {
    private Token publicToken;
    private Token staticToken;
    private Token voidToken;
    private Token idMainToken;
    private Token gettingDataTypeToken; // String
    private Token idArgsToken;
    private List<NodeStatement> statementList = new ArrayList<>();

    public void setPublicToken(Token publicToken) {
        this.publicToken = publicToken;
    }

    public void setStaticToken(Token staticToken) {
        this.staticToken = staticToken;
    }

    public void setVoidToken(Token voidToken) {
        this.voidToken = voidToken;
    }

    public void setIdMainToken(Token idMainToken) {
        this.idMainToken = idMainToken;
    }

    public void setGettingDataTypeToken(Token gettingDataTypeToken) {
        this.gettingDataTypeToken = gettingDataTypeToken;
    }

    public void setIdArgsToken(Token idArgsToken) {
        this.idArgsToken = idArgsToken;
    }

    public void addStatement(NodeStatement statement) {
        statementList.add(statement);
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"MainMethod%d\"", index++);

        for (NodeStatement statement : statementList) {
            index = statement.visit(thisNode, index, sb);
        }

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
