package LJ.Grammar.Terminals;

public class Loop implements Terminal {
    private TypeLoop type;

    public Loop(TypeLoop type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        switch (type) {
            case WHILE:
                return "while";
//            case FOR: todo later
//                return "for";
//            case DO-WHILE:
//                return "do-while";
        }

        return null;
    }
}
