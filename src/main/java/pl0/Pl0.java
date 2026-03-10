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

    int ch; // last character read
    Symbol sym; // last symbol read
    String id; // last identifier read
    int num; // last number read
    int cc; // character count
    int ll; // line length

    public static void run(String source) {

    }
}
