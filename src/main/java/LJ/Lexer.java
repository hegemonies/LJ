package LJ;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class Lexer {
    private List<Token> tokenList = new ArrayList<>();
    private Type types = new Type();
    private String[] strings;
    private int index_cur_token = 0;

    Lexer(String src_code) {
        strings = src_code.split("\n");
    }

    void go() {
        int row = 0;
        int col = 0;

        for (row = 0; row < strings.length; row++) {
            String[] chars = strings[row].split("");
            StringBuilder buffer = new StringBuilder();
            PredicationOfTheToken predicate = null;

            for (col = 0; col < chars.length; col++) {
                char cur_char = chars[col].charAt(0);

                if (buffer.length() == 0) {
                    buffer.append(cur_char);

                    if (!isLegalChar(cur_char)) {
                        predicate = PredicationOfTheToken.ILLEGAL;
                    } else if (types.isToken(Character.toString(cur_char))
                            && (col < chars.length - 1)
                            && chars[col].equals(chars[col + 1])) {
                        char next_char = chars[col + 1].charAt(0);
                        buffer.append(next_char);
                        predicate = PredicationOfTheToken.KWORD;
                    } else if (types.isToken(Character.toString(cur_char))) {
                        if (cur_char == '\"') {
                            predicate = PredicationOfTheToken.STR_LITERAL;
                        } else {
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token(types.getTypeOfToken(token),
                                    token,
                                    new Location(row + 1, col + 1)));
                        }
                    } else if (cur_char == ' ') {
                        buffer.setLength(0);
                    } else {
                        if (Character.isDigit(cur_char)){
                            predicate = PredicationOfTheToken.NUM_CONST;
                        } else if (Character.isLetter(cur_char)) {
                            predicate = PredicationOfTheToken.ID_or_KWORD;
                        } else if (cur_char == '_') {
                            predicate = PredicationOfTheToken.ID;
                        } else {
                            predicate = PredicationOfTheToken.KWORD;
                        }
                    }
                } else if (predicate == PredicationOfTheToken.STR_LITERAL) {
                    buffer.append(cur_char);

                    if (col != chars.length - 1) {
                        if (cur_char == '\"') {
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token("str_literal",
                                    token,
                                    new Location(row + 1, col + 1)));
                        }
                    } else {
                        String type = "unknown";

                        if (cur_char == '\"') {
                            type = "str_literal";
                        }

                        String token = buffer.toString();
                        buffer.setLength(0);
                        tokenList.add(new Token(type,
                                token,
                                new Location(row + 1, col + 1)));
                    }
                } else if (cur_char == ' ') {
                    if (predicate == PredicationOfTheToken.ILLEGAL) {
                        String token = buffer.toString();
                        buffer.setLength(0);
                        tokenList.add(new Token("unknown",
                                token,
                                new Location(row + 1, col + 1 - token.length())));
                        predicate = null;
                    } else if (predicate == PredicationOfTheToken.NUM_CONST) {
                        String token = buffer.toString();
                        buffer.setLength(0);
                        tokenList.add(new Token("numeric_constant",
                                token,
                                new Location(row + 1, col + 1 - token.length())));
                        predicate = null;
                    } else if (predicate == PredicationOfTheToken.ID) {
                        String token = buffer.toString();
                        buffer.setLength(0);
                        tokenList.add(new Token("id",
                                token,
                                new Location(row + 1, col + 1 - token.length())));
                        predicate = null;
                    } else {
                        String token = buffer.toString();
                        buffer.setLength(0);
                        tokenList.add(new Token(types.getTypeOfToken(token),
                                token,
                                new Location(row + 1, col + 1 - token.length())));
                        predicate = null;
                    }
                } else {
                    if (predicate == PredicationOfTheToken.NUM_CONST) {
                        if (Character.isDigit(cur_char)) {
                            buffer.append(cur_char);
                        } else {
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token("numeric_constant",
                                    token,
                                    new Location(row + 1, col + 1 - token.length())));
                            predicate = null;
                            col--;
                        }
                    } else if (predicate == PredicationOfTheToken.KWORD) {
                        if (types.isToken(buffer.toString())) {
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token(types.getTypeOfToken(token),
                                    token,
                                    new Location(row + 1, col + 1 - token.length())));
                            predicate = null;
                        } else if (Character.isLetter(cur_char)) {
                            buffer.append(cur_char);
                        }
                    } else if (predicate == PredicationOfTheToken.ID) {
                        if (Character.isLetterOrDigit(cur_char) || cur_char == '_') {
                            buffer.append(cur_char);
                        } else {
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token("id",
                                    token,
                                    new Location(row + 1, col + 1 - token.length())));
                            predicate = null;
                        }
                    } else if (predicate == PredicationOfTheToken.ID_or_KWORD) {
                        if (Character.isDigit(cur_char) || cur_char == '_') {
                            col--;
                            predicate = PredicationOfTheToken.ID;
                        } else if (Character.isLetter(cur_char)) {
                            buffer.append(cur_char);
                        } else if (!isLegalChar(cur_char)) {
                            predicate = PredicationOfTheToken.ILLEGAL;
                            buffer.append(cur_char);
                        } else {
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token(types.getTypeOfToken(token),
                                    token,
                                    new Location(row + 1, col + 1 - token.length())));
                            predicate = null;
                            col--;
                        }
                    } else if (predicate == PredicationOfTheToken.ILLEGAL) {
                        if (Character.isLetter(cur_char) || Character.isDigit(cur_char)) {
                            buffer.append(cur_char);
                        } else {
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token("unknown",
                                    token,
                                    new Location(row + 1, col + 1 - token.length())));
                            predicate = null;
                            col--;
                        }
                    }
                }
            }
        }

        tokenList.add(new Token("eof", "", new Location(row + 1, col + 1)));
    }

    void printOutput() {
        System.out.println("\tLexer output:");
        tokenList.forEach((token) -> System.out.println(token.toString()));
    }

    private boolean isLegalChar(Character ch) {
        return Character.isLetter(ch)
                || Character.isDigit(ch)
                || ch == '_'
                || ch == '!'
                || ch == '*'
                || ch == '%'
                || ch == '&'
                || ch == '('
                || ch == ')'
                || ch == '['
                || ch == ']'
                || ch == '{'
                || ch == '}'
                || ch == '-'
                || ch == '='
                || ch == '+'
                || ch == '/'
                || ch == '\\'
                || ch == '|'
                || ch == '.'
                || ch == ','
                || ch == '~'
                || ch == '\"'
                || ch == '\''
                || ch == ';'
                || ch == ':'
                || ch == ' ';

    }

    public Token getNextToken() {
        if (index_cur_token != tokenList.size()) {
            return tokenList.get(index_cur_token++);
        }

        return null;
    }

    public void resetTokenCounter() {
        index_cur_token = 0;
    }
}