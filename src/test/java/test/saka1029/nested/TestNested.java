package test.saka1029.nested;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import saka1029.nested.Nested;
import saka1029.nested.Nested.Instruction;
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
        List<Instruction> code = Nested.parse(
            """
            program
                var x = 3, y = 5;
                procedure f(a, b, c)
                    x = -2 + a
                end
                y = 5 * x
            end
            """);
        assertEquals(0, code.size());

    }
}
