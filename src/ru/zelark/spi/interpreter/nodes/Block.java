package ru.zelark.spi.interpreter.nodes;

import java.util.List;

public class Block implements Runnable {
    private List<VarsDeclaration> declarations;
    private Runnable compoundStatment;

    public Block(List<VarsDeclaration> declarations, Runnable compoundStatment) {
        this.declarations = declarations;
        this.compoundStatment = compoundStatment;
    }

    @Override
    public void run() {
        declarations.forEach(Runnable::run);
        compoundStatment.run();
    }
}
