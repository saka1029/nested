package saka1029.nested;

import java.util.LinkedHashMap;
import java.util.Map;

public class Routine extends Reference {

    public Map<String, Local> variables = new LinkedHashMap<>();

    public Routine(int address) {
        super(address);
    }

    @Override
    public String toString() {
        return "Routine(addr=%d, vars=%s)".formatted(address, variables);
    }
}
