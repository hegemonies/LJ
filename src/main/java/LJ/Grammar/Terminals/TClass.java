package LJ.Grammar.Terminals;

public class TClass implements Terminal {
    @Override
    public <T extends Terminal> T getTerminal() {
        return null;
    }
}
