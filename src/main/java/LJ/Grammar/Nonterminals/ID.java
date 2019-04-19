package LJ.Grammar.Nonterminals;

public class ID implements Nonterminal {

    @Override
    public <T extends Nonterminal> T getFirst() {
        return null;
    }

    @Override
    public <T extends Nonterminal> T getFollow() {
        return null;
    }
}
