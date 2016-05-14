package ru.zelark.spi.interpreter.nodes;

import ru.zelark.spi.interpreter.SymbolTable;

public class Assign <T extends Number> implements Runnable {
    private Var variable;
    private Evaluable<T> expression;
    private final SymbolTable symbolTable;

    public Assign(Var left, Evaluable<T> right, SymbolTable symbolTable) {
        this.variable = left;
        this.expression = right;
        this.symbolTable = symbolTable;
    }

    @Override
    public void run() {
        symbolTable.put(variable.name(), expression.evaluate());
    }
}
