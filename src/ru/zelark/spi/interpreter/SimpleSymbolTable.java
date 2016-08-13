package ru.zelark.spi.interpreter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class SimpleSymbolTable implements SymbolTable {
    private Map<String, Object> symbolTable = new HashMap<>();

    @Override
    public BigDecimal get(String name) {
        String key = name.toLowerCase();
        if (symbolTable.containsKey(key)) {
            return (BigDecimal) symbolTable.get(key);
        }
        throw new Error(String.format("Variable '%s' not found.", name));
    }

    @Override
    public void put(String name, Object value) {
        symbolTable.put(name.toLowerCase(), value);
    }

    public void dump() {
        symbolTable.forEach((k, v) -> System.out.println(k + " -> " + v));
    }
}
