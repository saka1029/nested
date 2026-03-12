package saka1029.nested;

public interface Instruction {

    void execute(Context context);

    public static final Instruction ADD = c -> c.push(c.pop() + c.pop());
    public static final Instruction SUBTRACT = c -> c.push(-c.pop() + c.pop());
    public static final Instruction MULTIPLY = c -> c.push(c.pop() * c.pop());
    public static final Instruction DIVIDE = c -> { int r=c.pop(); c.push(c.pop() / r); };
    public static final Instruction EQ = c -> c.push(c.pop() == c.pop() ? 1:0);
    public static final Instruction NE = c -> c.push(c.pop() != c.pop() ? 1:0);
    public static final Instruction LT = c -> c.push(c.pop() > c.pop() ? 1:0);
    public static final Instruction LE = c -> c.push(c.pop() >= c.pop() ? 1:0);
    public static final Instruction GT = c -> c.push(c.pop() < c.pop() ? 1:0);
    public static final Instruction GE = c -> c.push(c.pop() <= c.pop() ? 1:0);

    public static Instruction literal(int literal) {
        return c -> c.push(literal);
    }
    public static Instruction load(int address) {
        return c -> c.push(c.stack[address]);
    }
    public static Instruction store(int address) {
        return c -> c.stack[address] = c.pop();
    }
}
