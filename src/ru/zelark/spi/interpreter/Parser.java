package ru.zelark.spi.interpreter;

import ru.zelark.spi.interpreter.nodes.Runnable;

public interface Parser {
    Runnable parse();
}
