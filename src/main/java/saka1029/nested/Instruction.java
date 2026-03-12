package saka1029.nested;

public interface Instruction {

    void execute(Context context);

    public static final Instruction NOP = c -> c.nop();
    public static final Instruction ADD = c -> c.push(c.pop() + c.pop());
    public static final Instruction SUBTRACT = c -> c.push(-c.pop() + c.pop());
    public static final Instruction MULTIPLY = c -> c.push(c.pop() * c.pop());
    public static final Instruction DIVIDE = c -> { int r=c.pop(); c.push(c.pop() / r); };
    public static final Instruction NEGATE = c -> c.push(-c.pop());
    public static final Instruction EQ = c -> c.push(c.pop() == c.pop() ? 1:0);
    public static final Instruction NE = c -> c.push(c.pop() != c.pop() ? 1:0);
    public static final Instruction LT = c -> c.push(c.pop() > c.pop() ? 1:0);
    public static final Instruction LE = c -> c.push(c.pop() >= c.pop() ? 1:0);
    public static final Instruction GT = c -> c.push(c.pop() < c.pop() ? 1:0);
    public static final Instruction GE = c -> c.push(c.pop() <= c.pop() ? 1:0);

    static abstract class InstAbs implements Instruction {
        final int value;
        InstAbs(int value) { this.value = value; }
        @Override
        public boolean equals(Object obj) {
            return obj != null
                && obj.getClass() == getClass()
                && obj instanceof InstAbs ia
                && ia.value == value;
        }
    }

    static class Literal extends InstAbs {
        Literal(int value) { super(value); }
        @Override public void execute(Context context) { context.push(value); }
    }
    public static Instruction literal(int literal) { return new Literal(literal); }

    static class Load extends InstAbs {
        Load(int value) { super(value); }
        @Override public void execute(Context context) { context.push(context.stack[value]); }
    }
    public static Instruction load(int address) { return new Load(address); }

    static class Store extends InstAbs {
        Store(int value) { super(value); }
        @Override public void execute(Context context) { context.stack[value] = context.pop(); }
    }
    public static Instruction store(int address) { return new Store(address); }

    static class Branch extends InstAbs {
        Branch(int value) { super(value); }
        @Override public void execute(Context context) { context.pc = value; }
    }
    public static Instruction branch(int address) { return new Branch(address); }

    static class BranchFalse extends InstAbs {
        BranchFalse(int value) { super(value); }
        @Override public void execute(Context context) { if (context.pop() == 0) context.pc = value; }
    }
    public static Instruction branchFalse(int address) { return new BranchFalse(address); }
}
