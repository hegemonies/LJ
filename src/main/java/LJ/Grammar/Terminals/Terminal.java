package LJ.Grammar.Terminals;

import LJ.Grammar.Symbol;

public interface Terminal extends Symbol {
    <T extends Terminal> T getTerminal();
}
