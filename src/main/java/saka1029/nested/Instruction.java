package saka1029.nested;

public interface Instruction {

    void execute(Context context);

    /*
     *            sp
     *            v
     * stack: a b
     */
    public static final Instruction ADD = c -> c.stack[c.sp - 2] += c.stack[--c.sp];
    public static final Instruction SUBTRACT = c -> c.stack[c.sp - 2] -= c.stack[--c.sp];
}
