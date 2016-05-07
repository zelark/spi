package ru.zelark.spi.interpreter;

import java.util.List;

public interface Lexer {
    public List<Token> tokenize();
}
