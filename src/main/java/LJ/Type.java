package LJ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// todo: refactor: to use HashMap instead of ArrayLists
class Type {
    private List<String> types = new ArrayList<>();
    private List<String> kwords = new ArrayList<>();

    Type() {
        types.addAll(Arrays.asList(
                "int",
                "float",
                "double",
                "char",
                "long",
                "id",
                "return",
                "l_parent",
                "r_parent",
                "l_brace",
                "r_brace",
                "l_square",
                "r_square",
                "semi", // ;
                "colon", // :
                "numeric_constant",
                "plus", // +
                "minus", // -
                "star", // *
                "slash", // /
                "percent", // %
                "equal", // =
                "equalequal", // ==
                "less", // <
                "greater", // >
                "exclaim", // !
                "exclaimequal ", // !=
                "",
                "ampamp", // &&
                "pipepipe", // ||
                "amp", // &
                "pipe", // |
                "str_literal",
                "while",
                "for",
                "do",
                "if",
                "class",
                "public",
                "private",
                "protected",
                "static",
                "const",
                "eof"));
        kwords.addAll(Arrays.asList(
                "int",
                "float",
                "double",
                "char",
                "long",
                "return",
                "(",
                ")",
                "{",
                "}",
                "[",
                "]",
                ";",
                ":",
                "+",
                "-",
                "*",
                "/",
                "%",
                "=",
                "==",
                "<",
                ">",
                "!",
                "!= ",
                "",
                ".",
                "&&",
                "||",
                "&",
                "|",
                "\"",
                "\'",
                "while",
                "for",
                "do",
                "if",
                "class",
                "public",
                "private",
                "protected",
                "static",
                "const",
                "void"
        ));
    }

    boolean isToken(String token) {
        return kwords.contains(token);
    }

    private boolean isInteger(String token) {
        try {
            Integer _int = Integer.parseInt(token);
        } catch (Exception exc) {
            return false;
        }

        return true;
    }

    String getTypeOfToken(String token) {
        switch (token) {
            case "{":
                return "l_brace";
            case "}":
                return "r_brace";
            case "(":
                return "l_paren";
            case ")":
                return "r_paren";
            case "[":
                return "l_square";
            case "]":
                return "r_square";

            case "public":
                return "public";
            case "private":
                return "private";
            case "protected":
                return "protected";

            case "class":
                return "class";

            case "int":
                return "int";
            case "float":
                return "float";
            case "double":
                return "double";
            case "char":
                return "char";
            case "long":
                return "long";

            case "return":
                return "return";

            case ";":
                return "semi";
            case ":":
                return "colon";


            case "+":
                return "plus";
            case "-":
                return "minus";
            case "*":
                return "star";
            case "/":
                return "slash";
            case "%":
                return "percent";
            case "=":
                return "equal";
            case "==":
                return "equalequal";
            case "<":
                return "less";
            case ">":
                return "greater";
            case "!":
                return "exclaim";
            case "!= ":
                return "exclaimequal";

            case ".":
                return "dot";
            case "&&":
                return "ampamp";
            case "||":
                return "pipepipe";
            case "&":
                return "amp";
            case "|":
                return "pipe";
            case "\"":
                return "doublequotes";
            case "\'":
                return "quotes";

            case "while":
                return "while";
            case "for":
                return "for";
            case "do":
                return "do";
            case "if":
                return "if";

            case "static":
                return "static";
            case "const":
                return "const";
            case "void":
                return "void";

            default:
                return "id";
        }
    }
}
