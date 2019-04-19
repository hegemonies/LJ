package LJ.Grammar.Terminals;

public class Protected implements Terminal {
    @Override
    public <T extends Terminal> T getTerminal() {
        return null;
    }
}
