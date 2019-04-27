package LJ.Grammar.Terminals;

import java.util.regex.Pattern;

public class ID implements Terminal {
    private String value;

    public ID() {}

    public ID(String value) {
        this.value = value;

//    public void setValue(String value) throws Exception {
//        if (Pattern.matches("\\b[A-Za-z_]\\w+", value)) {
//            this.value = value;
//        } else {
//            throw new Exception("Error id");
//        }
    }

    @Override
    public String getValue() {
        return null;
    }
}
