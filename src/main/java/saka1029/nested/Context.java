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
    public final Map<String, Integer> variables;

    public Context(List<Instruction> codes, Map<String, Integer> variables) {
        this.codes = codes;
        this.variables = variables;
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
        return stack[variables.get(variable)];
    }

    public void push(int v) {
        stack[sp++] = v;
    }

    public int pop() {
        return stack[--sp];
    }

}
