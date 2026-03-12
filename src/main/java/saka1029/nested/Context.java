package saka1029.nested;

import java.util.ArrayList;
import java.util.List;

public class Context {

    public int[] stack = new int[500];
    public int sp = 0;
    public int pc = 0;
    public List<Instruction> codes = new ArrayList<>();

    public void run() {
        codes.get(pc++).execute(this);
    }

    public void push(int v) {
        stack[sp++] = v;
    }

}
