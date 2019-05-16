package LJ.Parser.AST;

import LJ.Lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class NodeClass implements Node {
    Token modAccessToken;
    Token classToken;
    Token idToken;
    List<NodeInitInsideClass> initList = new ArrayList<>();
    NodeMainMethod mainMethod;

    public void setModAccessToken(Token modAccessToken) {
        this.modAccessToken = modAccessToken;
    }

    public void setClassToken(Token classToken) {
        this.classToken = classToken;
    }

    public void setIdToken(Token idToken) {
        this.idToken = idToken;
    }

    public void setMainMethod(NodeMainMethod mainMethod) {
        this.mainMethod = mainMethod;
    }

    public void addInit(NodeInitInsideClass init) {
        initList.add(init);
    }
}
