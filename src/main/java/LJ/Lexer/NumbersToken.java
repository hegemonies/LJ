package LJ.Lexer;

public class NumbersToken {
    public static boolean isNumber(String token) {
        for (String ch : token.split("")) {
            if (!Character.isDigit(ch.charAt(0))) {
                return false;
            }
        }

        return true;
    }
}
