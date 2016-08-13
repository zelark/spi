package ru.zelark.spi.interpreter.nodes;

import ru.zelark.spi.interpreter.Token;

public class Type {
    private String value;

    public Type(Token token) {
        this.value = token.type().toString();
    }

    public String type() {
        return value;
    }
}
