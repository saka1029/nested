package test.saka1029.nested;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.nested.Context;
import saka1029.nested.Instruction;

public class TestInstruction {

    @Test
    public void testLiteral() {
        Context context = new Context();
        context.codes.add(Instruction.literal(10));
        assertEquals(0, context.sp);
        context.run();
        assertEquals(1, context.sp);
        assertEquals(10, context.stack[0]);
        assertEquals(1, context.pc);
    }

    @Test
    public void testLoadGlobal() {
        Context context = new Context();
        context.codes.add(Instruction.loadGlobal(0));
        context.push(22);   // address = 0
        assertEquals(1, context.sp);
        assertEquals(22, context.stack[0]);
        context.run();
        assertEquals(2, context.sp);
        assertEquals(22, context.stack[0]);
        assertEquals(22, context.stack[1]);
        assertEquals(1, context.pc);
    }

    @Test
    public void testStoreGlobal() {
        Context context = new Context();
        context.codes.add(Instruction.storeGlobal(0));
        context.push(22);   // address = 0
        context.push(77);
        assertEquals(2, context.sp);
        assertEquals(22, context.stack[0]);
        assertEquals(77, context.stack[1]);
        context.run();
        assertEquals(1, context.sp);
        assertEquals(77, context.stack[0]);
        assertEquals(1, context.pc);
    }

    @Test
    public void testADD() {
        Context context = new Context();
        context.codes.add(Instruction.ADD);
        context.push(2);
        context.push(3);
        assertEquals(2, context.sp);
        assertEquals(2, context.stack[0]);
        assertEquals(3, context.stack[1]);
        context.run();
        assertEquals(1, context.sp);
        assertEquals(5, context.stack[0]);
        assertEquals(1, context.pc);
    }

    @Test
    public void testSUBTRACT() {
        Context context = new Context();
        context.codes.add(Instruction.SUBTRACT);
        context.push(2);
        context.push(3);
        assertEquals(2, context.sp);
        assertEquals(2, context.stack[0]);
        assertEquals(3, context.stack[1]);
        context.run();
        assertEquals(1, context.sp);
        assertEquals(-1, context.stack[0]);
        assertEquals(1, context.pc);
    }

    @Test
    public void testDIVIDE() {
        Context context = new Context();
        context.codes.add(Instruction.DIVIDE);
        context.push(18);
        context.push(3);
        assertEquals(2, context.sp);
        assertEquals(18, context.stack[0]);
        assertEquals(3, context.stack[1]);
        context.run();
        assertEquals(1, context.sp);
        assertEquals(6, context.stack[0]);
        assertEquals(1, context.pc);
    }

    @Test
    public void testLT() {
        Context context = new Context();
        context.codes.add(Instruction.LT);
        context.push(18);
        context.push(3);
        assertEquals(2, context.sp);
        assertEquals(18, context.stack[0]);
        assertEquals(3, context.stack[1]);
        context.run();
        assertEquals(1, context.sp);
        assertEquals(0, context.stack[0]);
        assertEquals(1, context.pc);
    }

    @Test
    public void testLT2() {
        Context context = new Context();
        context.codes.add(Instruction.LT);
        context.push(1);
        context.push(3);
        assertEquals(2, context.sp);
        assertEquals(1, context.stack[0]);
        assertEquals(3, context.stack[1]);
        context.run();
        assertEquals(1, context.sp);
        assertEquals(1, context.stack[0]);
        assertEquals(1, context.pc);
    }

}
