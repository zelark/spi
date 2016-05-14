package ru.zelark.spi.interpreter.nodes;

import ru.zelark.spi.interpreter.SymbolTable;
import ru.zelark.spi.interpreter.Token;

public class Var implements Evaluable<Integer>{
    private final String name;
    private final SymbolTable symbolTable;

    public Var(Token token, SymbolTable symbolTable) {
        this.name = token.value();
        this.symbolTable = symbolTable;
    }

    public String name() {
        return name;
    }

    @Override
    public Integer evaluate() {
        return symbolTable.get(name);
    }
}
