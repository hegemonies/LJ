package LJ.Parser.AST;

import LJ.Lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class NodeMainMethod implements Node { // todo may be getters not need
    Token publicToken;
    Token staticToken;
    Token voidToken;
    Token idMainToken;
    Token gettingDataTypeToken; // String
    Token idArgsToken;
    List<Node> statementList = new ArrayList<>();

    public Token getPublicToken() {
        return publicToken;
    }

    public void setPublicToken(Token publicToken) {
        this.publicToken = publicToken;
    }

    public Token getStaticToken() {
        return staticToken;
    }

    public void setStaticToken(Token staticToken) {
        this.staticToken = staticToken;
    }

    public Token getVoidToken() {
        return voidToken;
    }

    public void setVoidToken(Token voidToken) {
        this.voidToken = voidToken;
    }

    public Token getIdMainToken() {
        return idMainToken;
    }

    public void setIdMainToken(Token idMainToken) {
        this.idMainToken = idMainToken;
    }

    public Token getGettingDataTypeToken() {
        return gettingDataTypeToken;
    }

    public void setGettingDataTypeToken(Token gettingDataTypeToken) {
        this.gettingDataTypeToken = gettingDataTypeToken;
    }

    public Token getIdArgsToken() {
        return idArgsToken;
    }

    public void setIdArgsToken(Token idArgsToken) {
        this.idArgsToken = idArgsToken;
    }

    public void addStatement(Node statement) {
        statementList.add(statement);
    }
}
