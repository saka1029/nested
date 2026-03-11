package test.saka1029.nested;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.nested.Nested;
import saka1029.nested.Nested.Token;

public class TestNested {

    @Test
    public void testToken() {
        Nested scanner = new Nested("  if (abc3漢字 == 123)  return");
        assertEquals(Token.IF, scanner.token());
        assertEquals(Token.LP, scanner.token());
        assertEquals(Token.ID, scanner.token()); assertEquals("abc3漢字", scanner.string);
        assertEquals(Token.EQ, scanner.token());
        assertEquals(Token.INT, scanner.token()); assertEquals("123", scanner.string);
        assertEquals(Token.RP, scanner.token());
        assertEquals(Token.RETURN, scanner.token());
        assertEquals(Token.EOF, scanner.token());
    }
}
