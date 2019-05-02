package LJ.Lexer;

import LJ.Parser.ParserException.CriticalProductionException;

public class ListLexer {
    private Lexer lexer;
    private Token lookahead;

    public ListLexer(Lexer lexer) {
        this.lexer = lexer;
        consume();
    }

    public void consume() {
        lookahead = lexer.getNextToken();
    }

    public void match(String type) throws CriticalProductionException {
        if (lookahead.getType().equals(type)) {
            consume();
        } else {
            throw new CriticalProductionException("expecting " + type
                    + "; found "+ lookahead.getType() +
                    ":" + lookahead.getValue()
                    + " in " + lookahead.getLocation());
        }
    }

    public void matchTypeAndCheckValue(String type, String value) throws CriticalProductionException {
        if (lookahead.getType().equals(type) && lookahead.getValue().equals(value)) {
            consume();
        } else {
            throw new CriticalProductionException("expecting " + type
                    + "; found "+ lookahead.getType() +
                    ":" + lookahead.getValue()
                    + " in " + lookahead.getLocation());
        }
    }

    public boolean matchLite(String x) {
        if (lookahead.getType().equals(x)) {
            return true;
        }

        return false;
    }

    public void matchOneOf(String... tokens) throws CriticalProductionException {
        for (String token : tokens) {
            if (lookahead.getType().equals(token)) {
                consume();
            } else {
                throw new CriticalProductionException("expecting " + token
                        + "; found "+ lookahead.getType() +
                        ":" + lookahead.getValue()
                        + " in " + lookahead.getLocation());
            }
        }
    }

    public Token getLookahead() {
        return lookahead;
    }
}
