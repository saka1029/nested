package saka1029.nested;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Context {

    public final int[] stack = new int[500];
    public int sp = 0;
    public int pc = 0;
    public final List<Instruction> codes;
    public final Map<String, Reference> references;

    public Context(List<Instruction> codes, Map<String, Reference> references) {
        this.codes = codes;
        this.references = references;
    }

    public Context() {
        this(new ArrayList<>(), new LinkedHashMap<>());
    }

    public void run() {
        while (true) {
            if (pc < 0 || pc >= codes.size())
                break;
            Instruction instruction = codes.get(pc++);
            if (instruction == null)
                break;
            instruction.execute(this);
        }
    }

    public int get(String variable) {
        Reference ref = references.get(variable);
        if (ref == null)
            throw new RuntimeException("Variable '%s' not defined".formatted(variable));
        return stack[ref.address];
    }

    public void nop() {
    }

    public void push(int v) {
        stack[sp++] = v;
    }

    public int pop() {
        return stack[--sp];
    }

}
