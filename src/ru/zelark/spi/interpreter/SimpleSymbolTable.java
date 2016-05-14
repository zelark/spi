package ru.zelark.spi.interpreter;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class SimpleSymbolTable implements SymbolTable {
    private Map symbolTable = new HashMap();

    @Override
    public Integer get(String name) {
        String key = name.toLowerCase();
        if (symbolTable.containsKey(key)) {
            return (Integer) symbolTable.get(key);
        }
        throw new Error(String.format("Variable '%s' not found.", name));
    }

    @Override
    public void put(String name, Object value) {
        symbolTable.put(name.toLowerCase(), value);
    }

    public void dump() {
        Iterator it = symbolTable.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " -> " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}
