package LJ.Grammar.Terminals;

public class Numbers implements Terminal {
    private IRNumbers value;

    public Numbers(IRNumbers value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        switch (value) {
            case ONE:
                return "1";
            case TWO:
                return "2";
            case THREE:
                return "3";
            case FOUR:
                return "4";
            case FIVE:
                return "5";
            case SIX:
                return "6";
            case SEVEN:
                return "7";
            case EIGHT:
                return "8";
            case NINE:
                return "9";
            case ZERO:
                return "0";
        }

        return null;
    }
}
