package ru.zelark.spi.interpreter.nodes;

public class Program implements Runnable {
    private String name;
    private Block block;

    public Program(String name, Block block) {
        this.name = name;
        this.block = block;
    }

    @Override
    public void run() {
        block.run();
    }
}
