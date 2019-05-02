package LJ;

import LJ.Lexer.Lexer;
import LJ.Lexer.ListLexer;
import LJ.Parser.Parser;

class Compiler {
    private static String src_data = null;

    static void compile(String input, String output) {
        src_data = Helper.getDataFromFIle(input);
        printSourceCode();

        assert src_data != null;
        Lexer lexer = new Lexer(src_data);
        lexer.go();
        lexer.printOutput();

        Parser parser = new Parser(new ListLexer(lexer));
        parser.go();
    }

    private static void printSourceCode() {
        int count = 1;
        System.out.println("\tSource code:");
        for (String string : src_data.split("\n")) {
            System.out.printf(count++ + ". " + string + "\n");
        }
    }
}
