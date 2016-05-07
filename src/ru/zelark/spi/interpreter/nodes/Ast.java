package ru.zelark.spi.interpreter.nodes;

public interface Ast<T> {
    public T evaluate();
}
