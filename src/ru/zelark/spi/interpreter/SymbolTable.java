package ru.zelark.spi.interpreter;

import java.math.BigDecimal;

public interface SymbolTable {
    BigDecimal get(String name);

    void put(String name, Object value);

    void dump();
}
