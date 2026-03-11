package saka1029.nested;

import java.util.Map;
import static java.util.Map.*;

public class Nested {

    public enum Token {
        LP, RP, COMMA,
        PLUS, MINUS, STAR, SLASH,
        ASSIGN, EQ, NE, GT, GE, LT, LE,
        SEMI,
        VAR, END, PROCEDURE, FUNCTION, IF, THEN, ELSE, WHILE, RETURN,
        INT, ID, EOF;
    }
    static final Map<String, Token> RESERVED = Map.ofEntries(
        entry("var", Token.VAR), entry("end", Token.END),
        entry("procedure", Token.PROCEDURE), entry("function", Token.FUNCTION),
        entry("if", Token.IF), entry("then", Token.THEN), entry("else", Token.ELSE),
        entry("while", Token.WHILE),
        entry("return", Token.RETURN)
    );

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

    Token token(Token token) {
        ch();
        return token;
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

    public Token token() {
        spaces();
        int start = index - 1;
        switch (ch) {
            case -1: return Token.EOF;
            case '(': return token(Token.LP);
            case ')': return token(Token.RP);
            case ';': return token(Token.SEMI);
            case ',': return token(Token.COMMA);
            case '+': return token(Token.PLUS);
            case '-': return token(Token.MINUS);
            case '*': return token(Token.STAR);
            case '/': return token(Token.SLASH);
            case '=': return ch() == '=' ? token(Token.EQ) : Token.ASSIGN;
            case '<': return ch() == '=' ? token(Token.LE) : Token.LT;
            case '>': return ch() == '=' ? token(Token.GE) : Token.GT;
            default:
                if (isIdFirst(ch)) {
                    ch();
                    while (isIdRest(ch))
                        ch();
                    string = new String(input, start, index - start - 1);
                    Token token = RESERVED.get(string);
                    return token == null ? Token.ID : token;
                } else if (isDigit(ch)) {
                    ch();
                    while (isDigit(ch))
                        ch();
                    string = new String(input, start, index - start - 1);
                    return Token.INT;
                } else
                    throw new RuntimeException("Unknown char '%c'".formatted((char)ch));
        }
    }



}
