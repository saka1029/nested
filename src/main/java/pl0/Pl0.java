package pl0;

import java.util.Map;
import static java.util.Map.*;

public class Pl0 {

    enum Symbol {
        nul, ident, number, plus, minus, times, slash, oddsym,
        eql, neq, lss, leq, gtr, geq, lparen, rparen, comma, semicolon,
        period, becomes, beginsym, endsym, ifsym, thensym,
        whilesym, dosym, callsym, constsym, varsym, procsym
    }

    static final Map<String, Symbol> word = Map.ofEntries(
        entry("begin", Symbol.beginsym), entry("call", Symbol.callsym),
        entry("const", Symbol.constsym), entry("do", Symbol.dosym),
        entry("end", Symbol.endsym), entry("if", Symbol.ifsym),
        entry("odd", Symbol.oddsym), entry("procedure", Symbol.procsym),
        entry("then", Symbol.thensym), entry("var", Symbol.varsym),
        entry("while", Symbol.whilesym)
    );

    char[] input;
    int index;
    int ch; // last character read
    StringBuilder sb = new StringBuilder();
    Symbol sym; // last symbol read
    String id; // last identifier read
    int num; // last number read
    int cc; // character count
    int ll; // line length

    int getch() {
        return ch = index < input.length ? input[index++] : -1;
    }

    Symbol getsym() {
        while (Character.isWhitespace(ch))
            getch();
        sb.setLength(0);
        switch (ch) {
            case -1: sym = null; break;
            case '(': sym = Symbol.lparen; getch(); break;
            case ')': sym = Symbol.rparen; getch(); break;
            case ',': sym = Symbol.comma; getch(); break;
            case '.': sym = Symbol.period; getch(); break;
            case ';': sym = Symbol.semicolon; getch(); break;
            case '+': sym = Symbol.plus; getch(); break;
            case '-': sym = Symbol.minus; getch(); break;
            case '/': sym = Symbol.slash; getch(); break;
            case ':':
                if (getch() == '=') {
                    sym = Symbol.becomes;
                    getch();
                } else
                    throw new RuntimeException("unknown token ':%c'".formatted((char)ch));
                break;
            case '=': sym = Symbol.eql; getch(); break;
            case '<':
                if (getch() == '=') {
                    sym = Symbol.leq;
                    getch();
                } else
                    sym = Symbol.lss;
                break;
            case '>':
                if (getch() == '=') {
                    sym = Symbol.geq;
                    getch();
                } else
                    sym = Symbol.gtr;
                break;
            default: 
                if (Character.isDigit(ch)) {
                    while (Character.isDigit(ch)) {
                        sb.appendCodePoint(ch);
                        getch();
                    }
                    num = Integer.parseInt(sb.toString());
                    sym = Symbol.number;
                } else if (Character.isJavaIdentifierStart(ch)) {
                    sb.appendCodePoint(ch);
                    getch();
                    while (Character.isJavaIdentifierPart(ch)) {
                        sb.appendCodePoint(ch);
                        getch();
                    }
                    id = sb.toString();
                    sym = word.get(id);
                    if (sym == null)
                        sym = Symbol.ident;
                } else
                    throw new RuntimeException("Unknown char '%c'".formatted((char)ch));
                break;
        }
        return sym;
    }
    
    public static void run(String source) {
        Pl0 pl0 = new Pl0();
        pl0.input = source.toCharArray();
        pl0.index = 0;
        pl0.getch();
        while (true) {
            Symbol sym = pl0.getsym();
            if (sym == null)
                break;
            String str = switch (sym) {
                case number -> "" + pl0.num;
                case ident -> pl0.id;
                default -> "";
            };
            System.out.println(sym + " " + str);
        }
    }
}
