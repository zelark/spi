package ru.zelark.spi.interpreter;

import static ru.zelark.spi.interpreter.Token.TokenType.*;
import java.util.ArrayList;
import java.util.List;

public class CalcLexer implements Lexer{
    private final char[] text;
    private List<Token> tokens = new ArrayList<Token>();
    private int pos = 0;

    private final char NONE = '\0';

    public CalcLexer(String text) {
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

        if (Character.isWhitespace(character)) {
            skipWhitespaces();
            character = currentChar();
        }

        if (Character.isDigit(character)) {
            return new Token<Integer>(INTEGER, integer());
        }

        if (character == '(') {
            nextChar();
            return new Token<String>(LPAREN, "(");
        }

        if (character == ')') {
            nextChar();
            return new Token<String>(RPAREN, ")");
        }

        if (character == '+') {
            nextChar();
            return new Token<String>(PLUS, "+");
        }

        if (character == '-') {
            nextChar();
            return new Token<String>(MINUS, "-");
        }

        if (character == '*') {
            nextChar();
            return new Token<String>(MUL, "*");
        }

        if (character == '/') {
            nextChar();
            return new Token<String>(DIV, "/");
        }

        if (character == NONE) {
            return new Token<String>(EOF, "None");
        }
        else {
            throw new Error(String.format("Invalid character '%s' at position %d, ", currentChar(), pos));
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

    private void skipWhitespaces() {
        Character character = currentChar();
        while (character != NONE && Character.isSpaceChar(character)) {
            character = this.nextChar();
        }
    }

    private Integer integer() {
        Character character = currentChar();
        String number = "";
        while (character != NONE && Character.isDigit(character)) {
            number = number + character.toString();
            character = this.nextChar();
        }
        return Integer.parseInt(number);
    }
}
