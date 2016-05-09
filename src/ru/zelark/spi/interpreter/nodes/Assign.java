package ru.zelark.spi.interpreter.nodes;

import ru.zelark.spi.interpreter.Token;

import java.util.Map;

public class Assign <T extends Number> implements Runnable {
    private Var variable;
    private Evaluable<T> expression;
    private final Map<String, Integer> ram;

    public Assign(Var left, Evaluable<T> right, Map<String, Integer> ram) {
        this.variable = left;
        this.expression = right;
        this.ram = ram;
    }

    @Override
    public void run() {
        ram.put(variable.name(), (Integer) expression.evaluate());
    }
}
