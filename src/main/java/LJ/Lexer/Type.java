package LJ.Lexer;

import java.util.*;

public class Type {
    private Map<String, String> types = new HashMap<>();

    public Type() {
        types.put("{", "l_brace");
        types.put("}", "r_brace");
        types.put("(", "l_paren");
        types.put(")", "r_paren");
        types.put("[", "l_square");
        types.put("]", "r_square");

        types.put("public", "public");
        types.put("private", "private");
//        types.put("protected", "protected");

        types.put("class", "class");

        types.put("int", "int");
        // types.put("float", "float");
        // types.put("double", "double");
         types.put("char", "char");
        // types.put("long", "long");
        // types.put("boolean", "boolean");

        types.put("return", "return");

        types.put(";", "semi");
        types.put(":", "colon");

        types.put("+", "plus");
        types.put("-", "minus");
        types.put("*", "star");
        types.put("/", "slash");
        types.put("%", "percent");

        types.put("=", "equal");
        types.put("==", "equalequal");
        types.put("<", "less");
        types.put(">", "greater");
        types.put("!", "exclaim");
        types.put("!=", "exclaimequal");

        types.put(",", "comma");
        types.put(".", "dot");
        types.put("&&", "ampamp");
        types.put("||", "pipepipe");
        types.put("&", "amp");
        types.put("|", "pipe");
        types.put("\"", "doublequotes");
        types.put("\'", "quotes");

        types.put("while", "while");
        // types.put("for", "for");
        // types.put("do", "do");
        types.put("if", "if");

         types.put("static", "static");
        // types.put("const", "const");
        types.put("void", "void");
        types.put("id", "id");

        types.put("new", "new");
    }

    boolean isToken(String token) {
        return types.containsKey(token);
    }

    private boolean isInteger(String token) {
        try {
            Integer _int = Integer.parseInt(token);
        } catch (Exception exc) {
            return false;
        }

        return true;
    }

    public String getTypeOfToken(String token) {
        String type = types.get(token);

        if (type == null) {
            type = "id";
        }

        return type;
    }
}
