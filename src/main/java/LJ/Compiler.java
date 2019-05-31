package LJ;

import LJ.IdentifierTable.Table;
import LJ.Lexer.Lexer;
import LJ.Lexer.ListLexer;
import LJ.Parser.AST.NodeClass;
import LJ.Parser.Parser;

class Compiler {
    private static String src_data = null;

    static void compile(String input, String output) {
        src_data = Helper.getDataFromFIle(input);
        if (src_data == null || src_data.length() == 0) {
            System.out.println("ERROR: empty file");
            return;
        }
        printSourceCode();

        Lexer lexer = new Lexer(src_data);
        lexer.go();
        lexer.printOutput();

        Parser parser = new Parser(new ListLexer(lexer));
        NodeClass root = parser.go();
//        parser.showTree();
        parser.printTreeToFile();

        Table identifierTable = new Table();
        identifierTable.go(root);
    }

    private static void printSourceCode() {
        int count = 1;
        System.out.println("\tSource code:");
        for (String string : src_data.split("\n")) {
            System.out.println(String.format("%d. %s",
                    count++,
                    string));
        }
    }
}
