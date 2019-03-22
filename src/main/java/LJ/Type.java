package LJ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Type {
    List<String> types = new ArrayList<>();

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
                "+",
                "-",
                "*",
                "/",
                "equal",
                "==",
                "<",
                ">",
                "!",
                "!=",
                ">=",
                "<=",
                "",
                "str_literal",
                "class",
                "public",
                "private",
                "protected",
                "static",
                "const",
                "eof"));
    }

    public int getSize() {
        return types.size();
    }


}
