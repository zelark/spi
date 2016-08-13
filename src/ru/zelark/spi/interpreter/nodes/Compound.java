package ru.zelark.spi.interpreter.nodes;

import java.util.List;

public class Compound implements Runnable{
    private final List<Runnable> children;

    public Compound(List<Runnable> children) {
        this.children = children;
    }

    @Override
    public void run() {
        children.forEach(Runnable::run);
    }
}
