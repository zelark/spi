package ru.zelark.spi.interpreter;

import static ru.zelark.spi.interpreter.Token.TokenType.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PascalLexer implements Lexer {
    private static final char NONE = '\0';
    private static final Map<String, Token> reservedKeywords = new HashMap<>();

    private final char[] text;
    private int pos = 0;

    private List<Token> tokens = new ArrayList<>();

    static
    {
        reservedKeywords.put("program", new Token(PROGRAM, PROGRAM.toString()));
        reservedKeywords.put("var", new Token(VAR, VAR.toString()));
        reservedKeywords.put("begin", new Token(BEGIN, BEGIN.toString()));
        reservedKeywords.put("end", new Token(END, END.toString()));
        reservedKeywords.put("integer", new Token(INTEGER, INTEGER.toString()));
        reservedKeywords.put("real", new Token(INTEGER, REAL.toString()));
        reservedKeywords.put("div", new Token(INTEGER_DIV, INTEGER_DIV.toString()));
    }

    public PascalLexer(String text) {
        this.text = text.toCharArray();
    }

    @Override
    public List<Token> tokenize() {
        if (!tokens.isEmpty()) {
            return tokens;
        }
        Token token = nextToken();
        while (token.type() != Token.TokenType.EOF) {
            tokens.add(token);
            token = nextToken();
        }
        tokens.add(token);
        return tokens;
    }

    private Token nextToken() {
        Character character = currentChar();

        while (Character.isWhitespace(character) || character == '{') {
            if (Character.isWhitespace(character)) {
                skipWhitespaces();
                character = currentChar();
            }

            if (character == '{') {
                skipComment();
                character = currentChar();
            }
        }

        if (Character.isDigit(character)) {
            return number();
        }

        if (Character.isAlphabetic(character)) {
            return id();
        }

        if (character == ':' && peekedChar() == '=') {
            nextChar();
            nextChar();
            return new Token(ASSIGN, ":=");
        }

        if (character == ':') {
            nextChar();
            return new Token(COLON, ":");
        }

        if (character == ';') {
            nextChar();
            return new Token(SEMI, ";");
        }

        if (character == ',') {
            nextChar();
            return new Token(COMMA, ",");
        }

        if (character == '.') {
            nextChar();
            return new Token(DOT, ".");
        }

        if (character == '(') {
            nextChar();
            return new Token(LPAREN, "(");
        }

        if (character == ')') {
            nextChar();
            return new Token(RPAREN, ")");
        }

        if (character == '+') {
            nextChar();
            return new Token(PLUS, "+");
        }

        if (character == '-') {
            nextChar();
            return new Token(MINUS, "-");
        }

        if (character == '*') {
            nextChar();
            return new Token(MUL, "*");
        }

        if (character == '/') {
            nextChar();
            return new Token(REAL_DIV, "/");
        }

        if (character == NONE) {
            return new Token(EOF, "None");
        }
        else {
            throw new Error(String.format("Illegal character '%s' at position %d, ", currentChar(), pos));
        }
    }

    private Character currentChar() {
        if (pos > text.length - 1) {
            return NONE;
        }
        else {
            return text[pos];
        }
    }

    private Character nextChar() {
        pos = pos + 1;
        return currentChar();
    }

    private Character peekedChar() {
        if (pos + 1 > text.length - 1) {
            return NONE;
        }
        else {
            return text[pos + 1];
        }
    }

    private void skipWhitespaces() {
        Character character = currentChar();
        while (character != NONE && Character.isWhitespace(character)) {
            character = this.nextChar();
        }
    }

    private void skipComment() {
        Character character = currentChar();
        while (character != '}') {
            character = this.nextChar();
        }
        nextChar();
    }

    private Token number() {
        Character character = currentChar();
        Token token;
        String number = "";

        while (character != NONE && Character.isDigit(character)) {
            number = number + character.toString();
            character = this.nextChar();
        }

        if (character == '.') {
            number = number + character;
            character = this.nextChar();

            while (character != NONE && Character.isDigit(character)) {
                number = number + character.toString();
                character = this.nextChar();
            }

            token = new Token(REAL_CONST, number);
        }
        else {
            token = new Token(INTEGER_CONST, number);
        }

        if (Character.isAlphabetic(character)) {
            throw new Error(String.format("Illegal character '%s' at position %d, ", currentChar(), pos));
        }

        return token;
    }

    private Token id() {
        Character character = currentChar();
        String name = "";
        while (character != NONE && Character.isLetterOrDigit(character)) {
            name = name + character.toString();
            character = this.nextChar();
        }
        return reservedKeywords.getOrDefault(name.toLowerCase(), new Token(ID, name));
    }
}
