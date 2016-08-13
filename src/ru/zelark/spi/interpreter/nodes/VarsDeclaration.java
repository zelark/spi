package ru.zelark.spi.interpreter.nodes;

import java.util.List;

public class VarsDeclaration implements Runnable {
    private List<Var> variables;
    private Type type;

    public VarsDeclaration(List<Var> variables, Type type) {
        this.variables = variables;
        this.type = type;
    }

    @Override
    public void run() {
        // do nothing
    }
}
