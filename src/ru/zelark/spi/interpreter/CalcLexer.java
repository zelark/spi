package ru.zelark.spi.interpreter;

import static ru.zelark.spi.interpreter.Token.TokenType.*;
import java.util.ArrayList;
import java.util.List;

public class CalcLexer implements Lexer{
    private char[] text;
    private int pos = 0;

    private final char NONE = '\0';

    public CalcLexer(String text) {
        this.text = text.toCharArray();
    }

    @Override
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<Token>();
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

        while (character != NONE && Character.isSpaceChar(character)) {
            character = nextChar();
        }

        String number = null;
        while (character != NONE && Character.isDigit(character)) {
            if (number == null) {
                number = character.toString();
            }
            else {
                number = number + character.toString();
            }
            character = nextChar();
        }
        if (number != null) {
            return new Token<Integer>(INTEGER, Integer.parseInt(number));
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
}
