package saka1029.nested;

public interface Instruction {

    void execute(Context context);

    public static final Instruction ADD = c -> c.stack[c.sp - 2] += c.stack[--c.sp];
    public static final Instruction SUBTRACT = c -> c.stack[c.sp - 2] -= c.stack[--c.sp];
    public static final Instruction MULTIPLY = c -> c.stack[c.sp - 2] *= c.stack[--c.sp];
    public static final Instruction DIVIDE = c -> c.stack[c.sp - 2] /= c.stack[--c.sp];
    public static Instruction literal(int literal) {
        return c -> c.push(literal);
    }
}
