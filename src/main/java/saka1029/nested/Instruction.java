package saka1029.nested;

public interface Instruction {

    void execute(Context context);

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

    static class Literal implements Instruction {
        final int literal;
        Literal(int literal) { this.literal = literal; }
        @Override public boolean equals(Object obj) { return obj instanceof Literal x && x.literal == literal; }
        @Override public void execute(Context context) { context.push(literal); }
    }
    public static Instruction literal(int literal) {
        return new Literal(literal);
    }

    static class Load implements Instruction {
        final int address;
        Load(int address) { this.address = address; }
        @Override public boolean equals(Object obj) { return obj instanceof Load x && x.address == address; }
        @Override public void execute(Context context) { context.push(context.stack[address]); }
    }
    public static Instruction load(int address) {
        return new Load(address);
    }

    static class Store implements Instruction {
        final int address;
        Store(int address) { this.address = address; }
        @Override public boolean equals(Object obj) { return obj instanceof Store x && x.address == address; }
        @Override public void execute(Context context) { context.stack[address] = context.pop(); }
    }
    public static Instruction store(int address) {
        return new Store(address);
    }
}
