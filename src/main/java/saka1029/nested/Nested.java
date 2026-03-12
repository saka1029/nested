package saka1029.nested;

import java.util.Map;

import static java.util.Map.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Nested {

    public enum Token {
        LP("("), RP(")"), COMMA(","), SEMICOLON(";"),
        PLUS("+"), MINUS("-"), STAR("*"), SLASH("/"),
        ASSIGN("="), EQ("=="), NOT("!"), NE("!="),
        GT(">"), GE(">="), LT("<"), LE("<="),
        AND("&"), OR("|"),
        PROGRAM("program"), VAR("var"), END("end"),
        PROCEDURE("procedure"), FUNCTION("function"),
        IF("if"), THEN("then"), ELSE("else"),
        WHILE("while"), DO("do"), RETURN("return"),
        INT("INTEGER"), ID("IDENTifier"), EOF("EOF");

        final String name;
        Token(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    static final Map<String, Token> RESERVED = Map.ofEntries(
        entry("program", Token.PROGRAM),
        entry("var", Token.VAR), entry("end", Token.END),
        entry("procedure", Token.PROCEDURE), entry("function", Token.FUNCTION),
        entry("if", Token.IF), entry("then", Token.THEN), entry("else", Token.ELSE),
        entry("while", Token.WHILE), entry("do", Token.DO),
        entry("return", Token.RETURN)
    );

    final int[] input;
    int index = 0, ch;

    // token()で読み込んだ後のTokenとToken文字列
    // tokenStringはtokenがIDまたはINTのときのみ値が保証される。
    public Token token;
    public String tokenString;

    // eatまたはmustにマッチしたあとのTokenとToken文字列
    // eatenStringはeatenがIDまたはINTのときのみ値が保証される。
    public Token eaten;
    public String eatenString;

    List<Instruction> codes = new ArrayList<>();
    Map<String, Integer> variables = new LinkedHashMap<>();

    public Nested(String input) {
        this.input = input.codePoints().toArray();
        ch();
    }

    int ch() {
        if (index < input.length) 
            return ch = input[index++];
        index = input.length + 1;
        return ch = -1;
    }

    void spaces() {
        while (Character.isWhitespace(ch))
            ch();
    }

    static boolean isDigit(int ch) {
        return Character.isDigit(ch);
    }

    static boolean isIdFirst(int ch) {
        return Character.isLetter(ch);
    }

    static boolean isIdRest(int ch) {
        return isIdFirst(ch) || Character.isDigit(ch) || ch == '_';
    }

    Token token(Token token) {
        ch();
        return this.token = token;
    }

    Token token(int second, Token token) {
        int first = ch;
        if (ch() == second)
            return token(token);
        throw new RuntimeException("Unknown '%c%c'".formatted(first, ch));
    }

    Token token(Token single, int second, Token token) {
        if (ch() == second)
            return token(token);
        return this.token = single;
    }

    Token id() {
        int start = index - 1;
        do {
            ch();
        } while (isIdRest(ch));
        tokenString = new String(input, start, index - start - 1);
        Token found = RESERVED.get(tokenString);
        return token = found == null ? Token.ID : found;
    }

    Token number() {
        int start = index - 1;
        do {
            ch();
        } while (isDigit(ch));
        tokenString = new String(input, start, index - start - 1);
        return token = Token.INT;
    }

    public Token token() {
        spaces();
        switch (ch) {
            case -1: return Token.EOF;
            case '(': return token(Token.LP);
            case ')': return token(Token.RP);
            case ';': return token(Token.SEMICOLON);
            case ',': return token(Token.COMMA);
            case '+': return token(Token.PLUS);
            case '-': return token(Token.MINUS);
            case '*': return token(Token.STAR);
            case '/': return token(Token.SLASH);
            case '=': return token(Token.ASSIGN, '=', Token.EQ);
            case '<': return token(Token.LT, '=', Token.LE);
            case '>': return token(Token.GT, '=', Token.GE);
            case '!': return token(Token.NOT, '=', Token.NE);
            case '&': return token(Token.AND);
            case '|': return token(Token.OR);
            default:
                if (isIdFirst(ch))
                    return id();
                else if (isDigit(ch))
                    return number();
                else
                    throw new RuntimeException("Unknown char '%c'".formatted((char)ch));
        }
    }

    boolean eat(Token... expects) {
        for (Token expected : expects)
            if (token == expected) {
                eaten = token;
                eatenString = tokenString;
                token();
                return true;
            }
        return false;
    }

    void must(Token expected) {
        if (token != expected)
            throw error("'%s' expected", expected);
        eaten = token;
        eatenString = tokenString;
        token();
    }

    RuntimeException error(String format, Object... args) {
        return new RuntimeException(format.formatted(args));
    }

    void factor() {
        if (eat(Token.LP)) {
            expression();
            must(Token.RP);
        } else if (eat(Token.ID)) {
            String name = eatenString;
            Integer addr = variables.get(name);
            if (addr == null)
                throw error("Variable '%s' not defined", name);
            codes.add(Instruction.load(addr));
        } else if (eat(Token.INT)) {
            int value = Integer.parseInt(eatenString);
            codes.add(Instruction.literal(value));
        } else
            throw error("Unknown token '%s'", token);
    }

    void term() {
        factor();
        while (true) {
            if (eat(Token.STAR)) {
                factor();
                codes.add(Instruction.MULTIPLY);
            } else if (eat(Token.SLASH)) {
                factor();
                codes.add(Instruction.DIVIDE);
            } else
                break;
        }
    }

    void expression() {
        boolean minus = false;
        if (eat(Token.PLUS))
            /* do nothing */;
        else if(eat(Token.MINUS))
            minus = true;
        term();
        if (minus)
            codes.add(Instruction.NEGATE);
        while (true) {
            if (eat(Token.PLUS)) {
                term();
                codes.add(Instruction.ADD);
            } else if (eat(Token.MINUS)) {
                term();
                codes.add(Instruction.SUBTRACT);
            } else
                break;
        }
    }

    void var() {
        // Token.VARがeatされた状態
        must(Token.ID);
        String name = eatenString;
        if (eat(Token.ASSIGN))
            expression();
        else
            codes.add(Instruction.literal(0));
        if (variables.containsKey(name))
            throw error("Variale '%s' duplicated", name);
        variables.put(name, variables.size());
    }

    void vars() {
        var();
        while (eat(Token.COMMA))
            var();
        must(Token.SEMICOLON);
    }

    void routines() {
        // procedureまたはfunctionがeatされている
        must(Token.ID);
        must(Token.LP);
        if (eat(Token.ID)) {
            while (eat(Token.COMMA)) {
                must(Token.ID);
            }
        }
        must(Token.RP);
        if (eat(Token.VAR))
            vars();
        statements();
        must(Token.END);
    }

    void statement() {
        if (eat(Token.ID)) {
            String name = eatenString;
            if (eat(Token.ASSIGN)) {
                expression();
                Integer addr = variables.get(name);
                if (addr == null)
                    throw error("Variable '%s' not defined", name);
                codes.add(Instruction.store(addr));
            } else if (eat(Token.LP)) {
                if (!eat(Token.RP)) {
                    expression();
                    while (eat(Token.COMMA))
                        expression();
                }
                must(Token.RP);
            }
        } else if (eat(Token.IF)) {
            expression();
            must(Token.THEN);
            statements();
            if (eat(Token.ELSE))
                statements();
            must(Token.END);
        } else if (eat(Token.WHILE)){
            expression();
            must(Token.DO);
            statements();
            must(Token.END);
        }
    }

    void statements() {
        statement();
        while (eat(Token.SEMICOLON))
            statement();
        eat(Token.SEMICOLON);
    }

    void program() {
        token();
        must(Token.PROGRAM);
        if (eat(Token.VAR))
            vars();
        // if (eat(Token.PROCEDURE, Token.FUNCTION))
        //     routines();
        statements();
        must(Token.END);
    }

    public static List<Instruction> parse(String input) {
        Nested nested = new Nested(input);
        nested.program();
        return nested.codes;
    }

}
