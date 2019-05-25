package LJ.Parser.AST.Value;

import java.util.ArrayList;
import java.util.List;

public class FuncCall extends GenericValue { // todo refactor
    // value is id
    private List<GenericValue> argsCall = new ArrayList<>();

    public void setArgsCall(List<GenericValue> argsCall) {
        this.argsCall = argsCall;
    }
}
