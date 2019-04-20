package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;

import java.util.List;

interface Nonterminal extends Symbol {
    List<Symbol> getFirst();
    List<Symbol> getFollow();
}
