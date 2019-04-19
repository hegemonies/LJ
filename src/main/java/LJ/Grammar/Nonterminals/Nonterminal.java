package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;

import java.util.List;

interface Nonterminal extends Symbol {
    <T extends Symbol> List<T> getFirst();
    <T extends Symbol> List<T> getFollow();
}
