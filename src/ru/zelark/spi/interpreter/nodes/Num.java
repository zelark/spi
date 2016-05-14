package ru.zelark.spi.interpreter.nodes;

import ru.zelark.spi.interpreter.Token;

public class Num<T extends Number> implements Evaluable<T> {
    private T value;

    public Num(T value) {
        this.value = value;
    }

    @Override
    public T evaluate() {
        return value;
    }
}
