package LJ.Grammar.Nonterminals;

import LJ.Grammar.Symbol;
import LJ.Grammar.Terminals.DataType;
import LJ.Grammar.Terminals.TypeDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NTDataType implements Nonterminal {
    private static final List<List<Symbol>> rules = new ArrayList<>();

    static {
        rules.add(Arrays.asList(new DataType(TypeDataType.INT)));
        // later you can add more
    }

    @Override
    public List<Symbol> getFirst() {
        return null;
    }

    @Override
    public List<Symbol> getFollow() {
        return null;
    }
}
