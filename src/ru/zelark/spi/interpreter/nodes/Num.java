package ru.zelark.spi.interpreter.nodes;

import ru.zelark.spi.interpreter.Token;

public class Num<T extends Number> implements Ast<T> {
    private Token<T> token;
    private Integer value;

    public Num(Token<T> token) {
        this.token = token;
    }

    @Override
    public T evaluate() {
        return token.value();
    }
}