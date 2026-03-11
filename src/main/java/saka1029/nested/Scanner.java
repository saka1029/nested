package saka1029.nested;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
    public String input;
    public String eaten;

    public Scanner(String input) {
        this.input = input.trim();
    }

    public static final Pattern SPACES = Pattern.compile("^\\s+");
    public static final Pattern INT = Pattern.compile("^\\w+");
    public static final Pattern ID = Pattern.compile("^\\p{IsLetter}[_\\p{IsLetter}\\p{IsDigit}]*");

    boolean eaten(String eaten) {
        this.eaten = eaten;
        input = input.substring(eaten.length()).trim();
        return true;
    }

    public boolean eat(String... expects) {
        for (String expect : expects)
            if (input.startsWith(expect))
                return eaten(expect);
        return false;
    }

    public boolean eat(Pattern expect) {
        Matcher matcher = expect.matcher(input);
        if (matcher.find())
            return eaten(matcher.group());
        return false;
    }

}
