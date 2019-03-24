package LJ;

import java.util.ArrayList;
import java.util.List;

class Lexer {
    private List<Token> tokenList = new ArrayList<>();
    private Type types = new Type();
    private String[] strings;

    Lexer(String data) {
        System.out.println("src code:\n" + data);
        strings = data.split("\n");
    }

    void go() {
        int row = 0;
        int col = 0;

        for (row = 0; row < strings.length; row++) {
            String[] chars = strings[row].split("");
            StringBuilder buffer = new StringBuilder();
            int token_length = 0;
            PredicationOfTheToken predicate = null;

            for (col = 0; col < chars.length; col++) {
                char cur_char = chars[col].charAt(0);

                if (token_length == 0) {
                    buffer.append(cur_char);
                    token_length++;

                    if (types.isToken(Character.toString(cur_char))) {
                        token_length = 0;
                        String token = buffer.toString();
                        buffer.setLength(0);
                        tokenList.add(new Token(types.getTypeOfToken(token), token, new Location(row + 1, col + 1 - token.length())));
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
                } else if (cur_char == ' ') {
                    token_length = 0;
                    String token = buffer.toString();
                    buffer.setLength(0);
                    tokenList.add(new Token(types.getTypeOfToken(token), token, new Location(row + 1, col + 1 - token.length())));
                } else {
                    if (predicate == PredicationOfTheToken.NUM_CONST) {
                        if (Character.isDigit(cur_char)) {
                            buffer.append(cur_char);
                            token_length++;
                        } else {
                            token_length = 0;
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token("numeric_constant", token, new Location(row + 1, col + 1 - token.length())));
                        }
                    } else if (predicate == PredicationOfTheToken.KWORD) {
                        if (types.isToken(buffer.toString())) {
                            token_length = 0;
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token(types.getTypeOfToken(token), token, new Location(row + 1, col + 1 - token.length())));
                        }
                    } else if (predicate == PredicationOfTheToken.ID) {
                        if (Character.isLetterOrDigit(cur_char) || cur_char == '_') {
                            buffer.append(cur_char);
                            token_length++;
                        } else {
                            token_length = 0;
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token("id", token, new Location(row + 1, col + 1 - token.length())));
                        }
                    } else if (predicate == PredicationOfTheToken.ID_or_KWORD) {
                        if (Character.isDigit(cur_char) || cur_char == '_') {
                            col--;
                            predicate = PredicationOfTheToken.ID;
                        } else if (Character.isLetter(cur_char)) {
                            buffer.append(cur_char);
                            token_length++;
                        }
                    }
                }
            }
        }

        tokenList.add(new Token("eof", "", new Location(row, col)));
    }

    enum PredicationOfTheToken {
        NUM_CONST,
        ID,
        KWORD,
        ID_or_KWORD
    }

    void showOutput() {
        System.out.println("\tlexer output:");
        tokenList.forEach((token) -> System.out.println(token));
    }
}