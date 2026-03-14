package test.saka1029.nested;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import saka1029.nested.Context;
import saka1029.nested.Instruction;
import saka1029.nested.Nested;
import saka1029.nested.Nested.Token;

public class TestNested {

    @Test
    public void testToken() {
        Nested scanner = new Nested("  if (abc3漢字 != 123)  return");
        assertEquals(Token.IF, scanner.token());
        assertEquals(Token.LP, scanner.token());
        assertEquals(Token.ID, scanner.token()); assertEquals("abc3漢字", scanner.tokenString);
        assertEquals(Token.NE, scanner.token());
        assertEquals(Token.INT, scanner.token()); assertEquals("123", scanner.tokenString);
        assertEquals(Token.RP, scanner.token());
        assertEquals(Token.RETURN, scanner.token());
        assertEquals(Token.EOF, scanner.token());
    }

    @Test
    public void testNE() {
        Nested scanner = new Nested("  != !A");
        assertEquals(Token.NE, scanner.token());
        try {
            scanner.token();
        } catch (RuntimeException e) {
            assertEquals("Unknown '!A'", e.getMessage());
        }
    }

    @Test
    public void testAND() {
        Nested scanner = new Nested("  && & ");
        assertEquals(Token.AND, scanner.token());
        try {
            scanner.token();
        } catch (RuntimeException e) {
            assertEquals("Unknown '& '", e.getMessage());
        }
    }

    @Test
    public void testOR() {
        Nested scanner = new Nested("  || |x ");
        assertEquals(Token.OR, scanner.token());
        try {
            scanner.token();
        } catch (RuntimeException e) {
            assertEquals("Unknown '|x'", e.getMessage());
        }
    }

    @Test
    public void testParse() {
        Context context = Nested.parse(
            """
            program
                var x = 3, y = 5;
                y = 5 * x;
                x = x + 3;
            end
            """);
        assertEquals(11, context.codes.size());
        List<Instruction> expectedCodes = List.of(
            Instruction.literal(3),
            Instruction.literal(5),
            Instruction.literal(5),
            Instruction.loadGlobal(0),
            Instruction.MULTIPLY,
            Instruction.storeGlobal(1),
            Instruction.loadGlobal(0),
            Instruction.literal(3),
            Instruction.ADD,
            Instruction.storeGlobal(0),
            Instruction.NOP);
        assertEquals(expectedCodes, context.codes);
    }

    @Test
    public void testIfThenElse() {
        Context context = Nested.parse(
            """
            program
                var x = 2;
                if 0 then
                    x = 0;
                else
                    x = 5;
                end
            end
            """);
        assertEquals(9, context.codes.size());
        List<Instruction> expectedCodes = List.of(
            Instruction.literal(2),
            Instruction.literal(0),
            Instruction.branchFalse(6),
            Instruction.literal(0),
            Instruction.storeGlobal(0),
            Instruction.branch(8),
            Instruction.literal(5),
            Instruction.storeGlobal(0),
            Instruction.NOP);
        assertEquals(expectedCodes, context.codes);
        context.run();
        assertEquals(5, context.get("x"));
    }

    @Test
    public void testIfThen() {
        Context context = Nested.parse(
            """
            program
                var x = 2;
                if 1 then
                    x = 0;
                end
            end
            """);
        assertEquals(6, context.codes.size());
        List<Instruction> expectedCodes = List.of(
            Instruction.literal(2),
            Instruction.literal(1),
            Instruction.branchFalse(5),
            Instruction.literal(0),
            Instruction.storeGlobal(0),
            Instruction.NOP);
        assertEquals(expectedCodes, context.codes);
        context.run();
        assertEquals(0, context.get("x"));
    }

    @Test
    public void testWhileDo() {
        Context context = Nested.parse(
            """
            program
                var x = 2;
                while x do
                    x = x - 1;
                end
            end
            """);
        List<Instruction> expectedCodes = List.of(
            Instruction.literal(2),
            Instruction.loadGlobal(0),
            Instruction.branchFalse(8),
            Instruction.loadGlobal(0),
            Instruction.literal(1),
            Instruction.SUBTRACT,
            Instruction.storeGlobal(0),
            Instruction.branch(1),
            Instruction.NOP);
        assertEquals(expectedCodes.size(), context.codes.size());
        assertEquals(expectedCodes, context.codes);
        context.run();
        assertEquals(0, context.get("x"));
    }

    @Test
    public void testDisplay() {
        Context context = Nested.parse(
            """
            program
                var x = 2;
                display x;
                x = x - 1;
                display x;
            end
            """);
        List<Instruction> expectedCodes = List.of(
            Instruction.literal(2),
            Instruction.loadGlobal(0),
            Instruction.DISPLAY,
            Instruction.loadGlobal(0),
            Instruction.literal(1),
            Instruction.SUBTRACT,
            Instruction.storeGlobal(0),
            Instruction.loadGlobal(0),
            Instruction.DISPLAY,
            Instruction.NOP);
        assertEquals(expectedCodes.size(), context.codes.size());
        assertEquals(expectedCodes, context.codes);
        context.run();
        assertEquals(1, context.get("x"));
    }

    @Test
    public void testRun() {
        String source = """
            program
                var x = 3, y = 5;
                y = 5 * x;
                x = x + 3;
            end
            """;
        Context context = Nested.parse(source);
        context.run();
        assertEquals(2, context.sp);
        // var x
        assertEquals(6, context.stack[0]);
        assertEquals(0, (int)context.references.get("x").address);
        assertEquals(6, context.get("x"));
        // var y
        assertEquals(15, context.stack[1]);
        assertEquals(1, (int)context.references.get("y").address);
        assertEquals(15, context.get("y"));
    }

    @Test
    public void testProc() {
        String source = """
            program
                var x = 3;
                proc f(a, b) var s = 3; x = 5; end
                x = 4;
            end
            """;
        Context context = Nested.parse(source);
        System.out.println(context.references);
        List<Instruction> expectedCodes = List.of(
            Instruction.literal(3),
            Instruction.branch(5),
            Instruction.literal(3),
            Instruction.literal(5),
            Instruction.storeGlobal(0),
            Instruction.literal(4),
            Instruction.storeGlobal(0),
            Instruction.NOP);
        assertEquals(expectedCodes.size(), context.codes.size());
        for (int i = 0; i < expectedCodes.size(); ++i) {
            System.out.println("compare " + i);
            assertEquals(expectedCodes.get(i), context.codes.get(i));
        }
        assertEquals(expectedCodes, context.codes);
    }
}
