package test.saka1029.nested;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

import saka1029.nested.Scanner;

public class TestScanner {

    static final Pattern IF = Pattern.compile("^if\\b");
    static final Pattern RETURN = Pattern.compile("^return\\b");

    @Test
    public void testEat() {
        Scanner scanner = new Scanner("  if (abc3漢字 == 123)  return");
        assertTrue(scanner.eat(IF)); assertEquals("if", scanner.eaten);
        assertTrue(scanner.eat("(")); assertEquals("(", scanner.eaten);
        assertTrue(scanner.eat(Scanner.ID)); assertEquals("abc3漢字", scanner.eaten);
        assertTrue(scanner.eat("==")); assertEquals("==", scanner.eaten);
        assertTrue(scanner.eat(Scanner.INT)); assertEquals("123", scanner.eaten);
        assertTrue(scanner.eat(")")); assertEquals(")", scanner.eaten);
        assertTrue(scanner.eat(RETURN)); assertEquals("return", scanner.eaten);
    }

}
