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

    public void match(String x) {
        if (lookahead.getType().equals(x)) {
            consume();
        } else throw new Error("expecting " + x + "; found "+ lookahead + " in " + lookahead.getLocation());
    }

    public Token getLookahead() {
        return lookahead;
    }
}
