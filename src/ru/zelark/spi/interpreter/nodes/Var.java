package ru.zelark.spi.interpreter.nodes;

import ru.zelark.spi.interpreter.Token;

import java.util.Map;

public class Var implements Evaluable<Integer>{
    private Token<String> token;
    private final Map<String, Integer> ram;

    public Var(Token<String> token, Map<String, Integer> ram) {
        this.token = token;
        this.ram = ram;
    }

    public String name() {
        return token.value();
    }

    @Override
    public Integer evaluate() {
        if (ram.containsKey(token.value())) {
            return ram.get(token.value());
        }
        throw new Error(String.format("Variable %s not found.", token.value()));
    }
}
