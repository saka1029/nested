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
        DISPLAY("display"),
        INT("INTEGER"), ID("IDENTifier"), EOF("EOF");

        final String name;
        Token(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static final Map<String, Token> RESERVED = Map.ofEntries(
        entry("program", Token.PROGRAM),
        entry("var", Token.VAR), entry("end", Token.END),
        entry("procedure", Token.PROCEDURE), entry("function", Token.FUNCTION),
        entry("if", Token.IF), entry("then", Token.THEN), entry("else", Token.ELSE),
        entry("while", Token.WHILE), entry("do", Token.DO),
        entry("display", Token.DISPLAY),
        entry("return", Token.RETURN)
    );

    static final int DUMMY = Integer.MIN_VALUE;

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

    void assignStatement() {
        String name = eatenString;
        must(Token.ASSIGN);
        expression();
        Integer addr = variables.get(name);
        if (addr == null)
            throw error("Variable '%s' not defined", name);
        codes.add(Instruction.store(addr));
        must(Token.SEMICOLON);
    }

    /**
     * if expression then  statements else    statements   end
     *               BF X;            B Y; X:            Y:
     * if expression then  statements                      end
     *               BF X;                               X:
     */
    void ifStatement() {
        expression();
        must(Token.THEN);
        int thenPos = codes.size();
        codes.add(Instruction.branchFalse(DUMMY));
        statements();
        boolean elseExists = false;
        if (eat(Token.ELSE)) {
            elseExists = true;
            int elsePos = codes.size();
            codes.add(Instruction.branch(DUMMY));
            codes.set(thenPos, Instruction.branchFalse(codes.size()));
            statements();
            codes.set(elsePos, Instruction.branch(codes.size()));
        }
        must(Token.END);
        if (!elseExists)
            codes.set(thenPos, Instruction.branchFalse(codes.size()));
    }

    /**
     * while expression  do    statements  end
     * X:                BF Y              B X  Y:
     */
    void whileStatement() {
        int whilePos = codes.size();
        expression();
        must(Token.DO);
        int doPos = codes.size();
        codes.add(Instruction.branchFalse(DUMMY));
        statements();
        must(Token.END);
        codes.add(Instruction.branch(whilePos));
        codes.set(doPos, Instruction.branchFalse(codes.size()));
    }

    void displayStatement() {
        expression();
        codes.add(Instruction.DISPLAY);
        must(Token.SEMICOLON);
    }

    void statements() {
        while (true)
            if (eat(Token.ID))
                assignStatement();
            else if(eat(Token.IF))
                ifStatement();
            else if(eat(Token.WHILE))
                whileStatement();
            else if(eat(Token.DISPLAY))
                displayStatement();
            else
                break;
    }

    void program() {
        must(Token.PROGRAM);
        if (eat(Token.VAR))
            vars();
        statements();
        must(Token.END);
        codes.add(Instruction.NOP);
    }

    public static Context parse(String input) {
        Nested nested = new Nested(input);
        nested.token();
        nested.program();
        return new Context(nested.codes, nested.variables);
    }

}
