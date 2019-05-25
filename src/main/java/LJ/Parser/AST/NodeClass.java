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

    public String wrapperVisit(int startIndex) { // start point for wrapper GraphViz
        StringBuilder sb = new StringBuilder();

        sb.append("digraph AST {\n" +
                "node [shape=\"circle\", style=\"filled\", margin=\"0.01\"];\n");
        String rootNode = String.format("\"%s%d\"", "class", startIndex++);
        startIndex = this.visit(rootNode, startIndex, sb);
        sb.append("}");

        return sb.toString();
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        sb.append(String.format("%s [label=\"ModAccess=%s\nid=%s\"];\n",
                rootNode,
                modAccessToken.getValue(),
                idToken.getValue()));

        for (NodeInit nodeInit : initList) {
            index = nodeInit.visit(rootNode, index++, sb);
        }

        index = mainMethod.visit(rootNode, index, sb);

        return index;
    }
}
