package LJ.Lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private List<Token> tokenList = new ArrayList<>();
    private Type types = new Type();
    private String[] strings;
    private int indexCurToken = 0;

    public Lexer(String src_code) {
        strings = src_code.split("\n");
        tokenList.add(new Token("program", "program", new Location(0, 0)));
    }

    public void go() {
        int row = 0;
        int col = 0;
        PredicationOfTheToken predictor = null;
        StringBuilder buffer = new StringBuilder();

        for (row = 0; row < strings.length; row++) {

            if (buffer.length() > 0) {
                String token = buffer.toString();
                row--;
                if (predictor == PredicationOfTheToken.NUM_CONST) {
                    String type = "numeric_constant";

                    if (Integer.parseInt(token.split("")[0]) == 0 &&
                            token.length() > 1) {
                        type = "unknown";
                    }

                    tokenList.add(new Token(type,
                            token,
                            new Location(row + 1, col + 1 - token.length())));
                } else if (predictor == PredicationOfTheToken.KWORD) {
                    if (types.isToken(token)) {
                        tokenList.add(new Token(types.getTypeOfToken(token),
                                token,
                                new Location(row + 1, col + 1 - token.length())));
                    }
                } else if (predictor == PredicationOfTheToken.ID) {
                    tokenList.add(new Token("id",
                            token,
                            new Location(row + 1, col + 1 - token.length())));
                } else if (predictor == PredicationOfTheToken.ID_or_KWORD) {
                    tokenList.add(new Token(types.getTypeOfToken(token),
                            token,
                            new Location(row + 1, col + 1 - token.length())));
                } else if (predictor == PredicationOfTheToken.ILLEGAL) {
                    tokenList.add(new Token("unknown",
                            token,
                            new Location(row + 1, col + 1 - token.length())));
                }

                buffer.setLength(0);
                continue;
            }

            String[] chars = strings[row].split("");
            buffer.setLength(0);

            for (col = 0; col < chars.length; col++) {
                if (chars[col].length() == 0) {
                    continue;
                }
                char cur_char = chars[col].charAt(0);

                if (buffer.length() == 0) {
                    buffer.append(cur_char);

                    if (!isLegalChar(cur_char)) {
                        predictor = PredicationOfTheToken.ILLEGAL;
                    } else if (types.isToken(Character.toString(cur_char))
                            && (col < chars.length - 1)
                            && types.isDoubleToken(cur_char + chars[col + 1])) {
                        char next_char = chars[col + 1].charAt(0);
                        buffer.append(next_char);
                        predictor = PredicationOfTheToken.KWORD;
                    } else if (types.isToken(Character.toString(cur_char))) {
                        if (cur_char == '\"') {
                            predictor = PredicationOfTheToken.STR_LITERAL;
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
                        if (Character.isDigit(cur_char)) {
                            predictor = PredicationOfTheToken.NUM_CONST;
                        } else if (Character.isLetter(cur_char)) {
                            predictor = PredicationOfTheToken.ID_or_KWORD;
                        } else if (cur_char == '_') {
                            predictor = PredicationOfTheToken.ID;
                        } else {
                            predictor = PredicationOfTheToken.KWORD;
                        }
                    }
                } else if (predictor == PredicationOfTheToken.STR_LITERAL) {
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
                    String type;
                    String token = buffer.toString();

                    if (predictor == PredicationOfTheToken.ILLEGAL) {
                        type = "unknown";
                    } else if (predictor == PredicationOfTheToken.NUM_CONST) {
                        type = "numeric_constant";
                        if (Integer.parseInt(token.split("")[0]) == 0 &&
                                token.length() > 1) {
                            type = "unknown";
                        }
                    } else if (predictor == PredicationOfTheToken.ID) {
                        type = "id";
                    } else {
                        type = types.getTypeOfToken(token);
                    }

                    buffer.setLength(0);
                    tokenList.add(new Token(type,
                            token,
                            new Location(row + 1, col + 1 - token.length())));
                    predictor = null;
                } else {
                    if (predictor == PredicationOfTheToken.NUM_CONST) {
                        if (Character.isDigit(cur_char)) {
                            buffer.append(cur_char);
                        } else {
                            String token = buffer.toString();
                            String type = "numeric_constant";

                            if (Integer.parseInt(token.split("")[0]) == 0 &&
                                    token.length() > 1) {
                                type = "unknown";
                            }

                            tokenList.add(new Token(type,
                                    token,
                                    new Location(row + 1, col + 1 - token.length())));
                            predictor = null;
                            col--;

                            buffer.setLength(0);
                        }
                    } else if (predictor == PredicationOfTheToken.KWORD) {
                        if (types.isToken(buffer.toString())) {
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token(types.getTypeOfToken(token),
                                    token,
                                    new Location(row + 1, col + 1 - token.length())));
                            predictor = null;
                        } else if (Character.isLetter(cur_char)) {
                            buffer.append(cur_char);
                        }
                    } else if (predictor == PredicationOfTheToken.ID) {
                        if (Character.isLetterOrDigit(cur_char) || cur_char == '_') {
                            buffer.append(cur_char);
                        } else {
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token("id",
                                    token,
                                    new Location(row + 1, col + 1 - token.length())));
                            predictor = null;
                        }
                    } else if (predictor == PredicationOfTheToken.ID_or_KWORD) {
                        if (Character.isDigit(cur_char) || cur_char == '_') {
                            col--;
                            predictor = PredicationOfTheToken.ID;
                        } else if (Character.isLetter(cur_char)) {
                            buffer.append(cur_char);
                        } else if (!isLegalChar(cur_char)) {
                            predictor = PredicationOfTheToken.ILLEGAL;
                            buffer.append(cur_char);
                        } else {
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token(types.getTypeOfToken(token),
                                    token,
                                    new Location(row + 1, col + 1 - token.length())));
                            predictor = null;
                            col--;
                        }
                    } else if (predictor == PredicationOfTheToken.ILLEGAL) {
                        if (Character.isLetter(cur_char) || Character.isDigit(cur_char)) {
                            buffer.append(cur_char);
                        } else {
                            String token = buffer.toString();
                            buffer.setLength(0);
                            tokenList.add(new Token("unknown",
                                    token,
                                    new Location(row + 1, col + 1 - token.length())));
                            predictor = null;
                            col--;
                        }
                    }
                }
            }
        }

        if (buffer.length() > 0) {
            row--;
            if (predictor == PredicationOfTheToken.NUM_CONST) {
                String token = buffer.toString();
                String type = "numeric_constant";

                if (Integer.parseInt(token.split("")[0]) == 0 &&
                        token.length() > 1) {
                    type = "unknown";
                }

                tokenList.add(new Token(type,
                        token,
                        new Location(row + 1, col + 1 - token.length())));
            } else if (predictor == PredicationOfTheToken.KWORD) {
                if (types.isToken(buffer.toString())) {
                    String token = buffer.toString();
                    tokenList.add(new Token(types.getTypeOfToken(token),
                            token,
                            new Location(row + 1, col + 1 - token.length())));
                }
            } else if (predictor == PredicationOfTheToken.ID) {
                String token = buffer.toString();
                buffer.setLength(0);
                tokenList.add(new Token("id",
                        token,
                        new Location(row + 1, col + 1 - token.length())));
            } else if (predictor == PredicationOfTheToken.ID_or_KWORD) {
                String token = buffer.toString();
                buffer.setLength(0);
                tokenList.add(new Token(types.getTypeOfToken(token),
                        token,
                        new Location(row + 1, col + 1 - token.length())));
            } else if (predictor == PredicationOfTheToken.ILLEGAL) {
                String token = buffer.toString();
                buffer.setLength(0);
                tokenList.add(new Token("unknown",
                        token,
                        new Location(row + 1, col + 1 - token.length())));
            }
        }

        tokenList.add(new Token("eof", "", new Location(row + 1, col + 1)));
    }

    public void printOutput() {
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
                || ch == '\"'
                || ch == '\''
                || ch == ';'
//                || ch == ':'
                || ch == ' '
                || ch == '<'
                || ch == '>';

    }

    Token getNextToken() {
        if (indexCurToken != tokenList.size()) {
            return tokenList.get(indexCurToken++);
        }

        return null;
    }

    int getIndexCurToken() {
        return indexCurToken;
    }

    void setIndexCurToken(int index) {
        indexCurToken = index;
    }

    public void resetTokenCounter() {
        indexCurToken = 0;
    }
}