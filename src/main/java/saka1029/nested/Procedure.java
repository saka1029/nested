package saka1029.nested;

import java.util.LinkedHashMap;
import java.util.Map;

public class Procedure extends Reference {

    public Map<String, Variable> variables = new LinkedHashMap<>();

    public Procedure(int address) {
        super(address);
    }

}
