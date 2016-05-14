package ru.zelark.spi.interpreter;

public interface SymbolTable {
    Integer get(String name);

    void put(String name, Object value);

    void dump();
}
