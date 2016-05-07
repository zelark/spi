package ru.zelark.spi.interpreter;

import ru.zelark.spi.interpreter.nodes.Ast;

public interface Parser {
    Ast parse();
}
