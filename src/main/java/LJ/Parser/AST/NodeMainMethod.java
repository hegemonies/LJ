package LJ.Parser.AST;

import LJ.Lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class NodeMainMethod implements Node {
    private Token publicToken;
    private Token staticToken;
    private Token voidToken;
    private Token idMainToken;
    private Token gettingDataTypeToken; // String
    private Token idArgsToken;
    private List<Node> statementList = new ArrayList<>();

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

    public void addStatement(Node statement) {
        statementList.add(statement);
    }
}
