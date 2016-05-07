package ru.zelark.spi.interpreter;

public class Token<T> {
    public enum TokenType {
        INTEGER, PLUS, MINUS, MUL, DIV, LPAREN, RPAREN, EOF
    }
    private final TokenType type;
    private final T value;

    public Token(TokenType type, T value) {
        this.type = type;
        this.value = value;
    }

    public TokenType type() {
        return type;
    }

    public T value() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, %s)", type, value.toString());
    }
}
