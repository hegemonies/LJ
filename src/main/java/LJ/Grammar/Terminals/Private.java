package LJ.Grammar.Terminals;

public class Private implements Terminal {
    @Override
    public <T extends Terminal> T getTerminal() {
        return null;
    }
}
