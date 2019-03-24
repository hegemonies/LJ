package LJ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Type {
    List<String> types = new ArrayList<>();
    List<String> kwords = new ArrayList<>();

    public Type() {
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
                "semi", // ;
                "colon", // :
                "numeric_constant",
                "plus", // +
                "minus", // -
                "star", // *
                "slash", // /
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
                ";",
                ":",
                "+",
                "-",
                "*",
                "/",
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
                "const"));
    }

    public boolean isToken(String token) {
        return kwords.contains(token);
    }

    public String getTypeOfToken(String token) {
        switch (token) {
            case "{":
                return "l_brace";
            case "}":
                return "r_brace";
            case "(":
                return "l_paren";
            case ")":
                return "r_paren";

            case "public":
                return "public";
            case "private":
                return "private";
            case "protected":
                return "protected";

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

            default:
                return "id";
        }
    }
}
