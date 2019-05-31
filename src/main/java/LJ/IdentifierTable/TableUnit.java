package LJ.IdentifierTable;

import java.util.Map;

class TableUnit implements GenericUnit {
    private Map<String, GenericUnit> table;

    public TableUnit(Map<String, GenericUnit> table) {
        this.table = table;
    }

    public Map<String, GenericUnit> getTable() {
        return table;
    }
}
