package test.saka1029.nested;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.nested.Context;
import saka1029.nested.Instruction;

public class TestInstruction {

    @Test
    public void testADD() {
        Context context = new Context();
        context.push(2);
        context.push(3);
        assertEquals(2, context.sp);
        assertEquals(2, context.stack[0]);
        assertEquals(3, context.stack[1]);
        Instruction.ADD.execute(context);
        assertEquals(1, context.sp);
        assertEquals(5, context.stack[0]);
    }

    @Test
    public void testSUBTRACT() {
        Context context = new Context();
        context.push(2);
        context.push(3);
        assertEquals(2, context.sp);
        assertEquals(2, context.stack[0]);
        assertEquals(3, context.stack[1]);
        Instruction.SUBTRACT.execute(context);
        assertEquals(1, context.sp);
        assertEquals(-1, context.stack[0]);
    }

}
