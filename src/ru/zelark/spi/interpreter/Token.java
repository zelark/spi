package ru.zelark.spi.interpreter;

public class Token {
    public enum TokenType {
        PROGRAM,
        VAR,
        BEGIN,
        END,
        DOT,
        COMMA,
        COLON,
        SEMI,
        ID,
        ASSIGN,
        INTEGER_CONST,
        REAL_CONST,
        INTEGER,
        REAL,
        PLUS,
        MINUS,
        MUL,
        INTEGER_DIV,
        REAL_DIV,
        LPAREN,
        RPAREN,
        EOF
    }
    private final TokenType type;
    private final String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType type() {
        return type;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, \"%s\")", type, value);
    }
}
