package LJ.Grammar.Terminals;

public class TModeAccess implements Terminal {
    private TypeTModAccess type;

    public TModeAccess(TypeTModAccess type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        switch (type) {
            case PUBLIC:
                return "public";
            case PRIVATE:
                return "private";
            case PROTECTED:
                return "protected";
            case FINAL:
                return "final";
            case STATIC:
                return "static";
        }

        return null;
    }
}
