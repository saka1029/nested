package saka1029.nested;

import java.util.Map;

import static java.util.Map.*;

import java.util.ArrayList;
import java.util.List;

public class Nested {

    public enum Token {
        LP("("), RP(")"), COMMA(","), SEMICOLON(";"),
        PLUS("+"), MINUS("-"), STAR("*"), SLASH("/"),
        ASSIGN("="), EQ("=="), NE("!="),
        GT(">"), GE(">="), LT("<"), LE("<="),
        AND("&&"), OR("||"),
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

    public static class Instruction {

    }

    final int[] input;
    int index = 0, ch;
    public Token token;
    public String string;

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
        string = new String(input, start, index - start - 1);
        Token found = RESERVED.get(string);
        return token = found == null ? Token.ID : found;
    }

    Token number() {
        int start = index - 1;
        do {
            ch();
        } while (isDigit(ch));
        string = new String(input, start, index - start - 1);
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
            case '!': return token('=', Token.NE);
            case '&': return token('&', Token.AND);
            case '|': return token('|', Token.OR);
            default:
                if (isIdFirst(ch))
                    return id();
                else if (isDigit(ch))
                    return number();
                else
                    throw new RuntimeException("Unknown char '%c'".formatted((char)ch));
        }
    }

    public Token eaten;
    public String eatenString;

    boolean eat(Token... expects) {
        for (Token expected : expects)
            if (token == expected) {
                eaten = token;
                eatenString = string;
                token();
                return true;
            }
        return false;
    }

    void expect(Token expected) {
        if (token != expected)
            throw error("'%s' expected", expected);
        token();
    }

    List<Instruction> codes = new ArrayList<>();

    RuntimeException error(String format, Object... args) {
        return new RuntimeException(format.formatted(args));
    }

    void factor() {
        if (eat(Token.LP)) {
            expression();
            expect(Token.RP);
        } else if (eat(Token.ID)) {
            ;
        } else if (eat(Token.INT)) {
            ;
        } else
            throw error("Unknown token '%s'", token);
    }

    void term() {
        factor();
        while (true) {
            if (eat(Token.STAR))
                factor();
            else if (eat(Token.SLASH))
                factor();
            else
                break;
        }
    }

    void expression() {
        term();
        while (true) {
            if (eat(Token.PLUS))
                term();
            else if (eat(Token.MINUS))
                term();
            else
                break;
        }
    }

    void var() {
        expect(Token.ID);
        if (eat(Token.ASSIGN))
            expression();
    }

    void vars() {
        var();
        while (eat(Token.COMMA))
            var();
        expect(Token.SEMICOLON);
    }

    void routines() {
        System.out.printf("%s %s%n", eatenString, string);
        expect(Token.ID);
        expect(Token.LP);
        if (eat(Token.ID))
            while (eat(Token.COMMA))
                expect(Token.ID);
        expect(Token.RP);
        if (eat(Token.VAR))
            vars();
        statements();
        expect(Token.END);
    }

    void statement() {
        if (eat(Token.ID)) {
            if (eat(Token.ASSIGN)) {
                expression();
            } else if (eat(Token.LP)) {
                if (!eat(Token.RP)) {
                    expression();
                    while (eat(Token.COMMA))
                        expression();
                }
                expect(Token.RP);
            }
        } else if (eat(Token.IF)) {
            expression();
            expect(Token.THEN);
            statements();
            if (eat(Token.ELSE))
                statements();
            expect(Token.END);
        } else if (eat(Token.WHILE)){
            expression();
            expect(Token.DO);
            statements();
            expect(Token.END);
        } else
            throw error("Unknown token '%s'", token);
    }

    void statements() {
        statement();
        while (eat(Token.SEMICOLON))
            statement();
        if (eat(Token.SEMICOLON))
            ;
    }

    void program() {
        token();
        expect(Token.PROGRAM);
        if (eat(Token.VAR))
            vars();
        if (eat(Token.PROCEDURE, Token.FUNCTION))
            routines();
        statements();
    }

    public static List<Instruction> parse(String input) {
        Nested nested = new Nested(input);
        nested.program();
        return nested.codes;
    }

}
