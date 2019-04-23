package LJ.Grammar.Terminals;

public class DataType implements Terminal {
    private TypeDataType type;

    public DataType(TypeDataType type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        switch (type) {
            case INT:
                return "int";
        }

        return null;
    }
}
