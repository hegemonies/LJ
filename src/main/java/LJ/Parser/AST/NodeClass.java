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

    public String wrapperVisit() { // start point for wrapper GraphViz
        StringBuilder sb = new StringBuilder();

        sb.append("digraph AST {\n" +
                "node [shape=\"circle\", style=\"filled\", margin=\"0.01\"];\n");
        sb.append(this.visit());
        sb.append("}");

        return sb.toString();
    }

    @Override
    public String visit() {
        StringBuilder sb = new StringBuilder();

        sb.append("\"class\" [label=");
        sb.append("ModAccess=").append(modAccessToken.getValue()).append("\n");
        sb.append("id=").append(idToken.getValue()).append("\n");
        sb.append("];\n");

        int index = 0;
        for (NodeInit nodeInit : initList) {
            sb.append(nodeInit.visit(index++));
        }



        return null;
    }
}
