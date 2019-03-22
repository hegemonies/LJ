package LJ;

import java.util.Objects;

class Compiler {
    static void compile(String input, String output) {
        Lexer lexer = new Lexer(Objects.requireNonNull(Helper.getDataFromFIle(input)));
        lexer.go();
//        parse.go();
    }
}
