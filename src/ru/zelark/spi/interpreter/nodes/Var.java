package ru.zelark.spi.interpreter.nodes;

import ru.zelark.spi.interpreter.Token;

import java.util.Map;

public class Var implements Evaluable<Integer>{
    private final Token token;
    private final String name;
    private final Map<String, Integer> ram;

    public Var(Token token, Map<String, Integer> ram) {
        this.token = token;
        this.name = token.value().toLowerCase();
        this.ram = ram;
    }

    public String name() {
        return name;
    }

    @Override
    public Integer evaluate() {
        if (ram.containsKey(name)) {
            return ram.get(name);
        }
        throw new Error(String.format("Variable '%s' not found.", token.value()));
    }
}
