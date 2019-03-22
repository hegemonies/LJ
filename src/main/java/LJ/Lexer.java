package LJ;

import java.util.ArrayList;
import java.util.List;

class Lexer {
    private List<Token> tokenList = new ArrayList<>();
    private String[] strings;

    Lexer(String data) {
        System.out.println("data: " + data);
        strings = data.split("\n");
    }

    void go() {
        for (int row = 0; row < strings.length; row++) {
            String[] tokens = strings[row].split(" ");
            for (int col = 0; col < tokens.length; col++) {
                String token = tokens[col];
                if (isStaticToken(token)) {
                    tokenList.add(new Token(token, token, new Location(row, col)));
                }
            }
        }
    }

    private boolean isStaticToken(String token) {
        switch (token) {
            case "public":
                return true;
            case "private":
                return true;
            case "protected":
                return true;
        }

        return true;
    }

//    private Type getType(String token) {
//        return Type.eof;
//    }
}
