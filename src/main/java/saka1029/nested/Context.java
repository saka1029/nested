package saka1029.nested;

import java.util.ArrayList;
import java.util.List;

public class Context {

    public final int[] stack = new int[500];
    public int sp = 0;
    public int pc = 0;
    public final List<Instruction> codes;

    public Context(List<Instruction> codes) {
        this.codes = codes;
    }

    public Context() {
        this(new ArrayList<>());
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

    public void push(int v) {
        stack[sp++] = v;
    }

    public int pop() {
        return stack[--sp];
    }

}
