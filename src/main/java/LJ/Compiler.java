package LJ;

import LJ.Lexer.Lexer;

class Compiler {
    private static String src_data = null;

    static void compile(String input, String output) {
        src_data = Helper.getDataFromFIle(input);
        printSourceCode();

        assert src_data != null;
        Lexer lexer = new Lexer(src_data);
        lexer.go();
        lexer.printOutput();

//        parse.go();
    }

    private static void printSourceCode() {
        int count = 0;
        System.out.println("\tSource code:");
        for (String string : src_data.split("\n")) {
            System.out.printf(count++ + ". " + string + "\n");
        }
    }
}
