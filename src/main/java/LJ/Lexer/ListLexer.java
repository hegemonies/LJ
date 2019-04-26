package LJ.Lexer;

public class ListLexer {
    private Lexer lexer;
    private Token lookahead;

    public ListLexer(Lexer lexer) {
        this.lexer = lexer;
        consume();
    }

    private void consume() {
        lookahead = lexer.getNextToken();
    }

    public Token getLookahead() {
        return lookahead;
    }
}
