package test.saka1029.nested;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

public class TestRegex {

    @Test
    public void testB() {
        Pattern word = Pattern.compile("^if\\b");
        assertTrue(word.matcher("if ").find());
        assertTrue(word.matcher("if").find());
        assertTrue(word.matcher("if-").find());
        assertFalse(word.matcher("iff").find());
        assertFalse(word.matcher("if3").find());
        assertFalse(word.matcher("if_").find());
    }

}
