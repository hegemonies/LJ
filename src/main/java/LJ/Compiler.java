package LJ;

class Compiler {
    private static String src_data = null;

    static void compile(String input, String output) {
        src_data = Helper.getDataFromFIle(input);

        assert src_data != null;
        Lexer lexer = new Lexer(src_data);
        lexer.go();
        lexer.printOutput();

//        parse.go();
    }

    private void printSourceCode() {
        System.out.println("\tSource code:");
        System.out.println(src_data);
    }
}
