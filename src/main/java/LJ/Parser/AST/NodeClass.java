package LJ.Parser.AST;

import LJ.Lexer.Token;
import LJ.Parser.AST.Inits.NodeInit;

import java.util.ArrayList;
import java.util.List;

public class NodeClass implements Node {
    Token modAccessToken;
    Token idToken;
    List<NodeInit> initList = new ArrayList<>();
    NodeMainMethod mainMethod;

    public void setModAccessToken(Token modAccessToken) {
        this.modAccessToken = modAccessToken;
    }

    public void setIdToken(Token idToken) {
        this.idToken = idToken;
    }

    public void setMainMethod(NodeMainMethod mainMethod) {
        this.mainMethod = mainMethod;
    }

    public void addInit(NodeInit init) {
        initList.add(init);
    }
}