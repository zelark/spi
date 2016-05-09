package ru.zelark.spi.interpreter.nodes;

import static ru.zelark.spi.interpreter.Token.TokenType.*;
import ru.zelark.spi.interpreter.Token;

public class UnaryOp implements Evaluable<Integer> {
    private Token operator;
    private Evaluable<Integer> right;

    public UnaryOp (Token operator, Evaluable<Integer> right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Integer evaluate() {
        if (operator.type() == PLUS) {
            return right.evaluate();
        }
        else if (operator.type() == MINUS) {
            return -right.evaluate();
        }
        else {
            throw new Error("Invalid operation");
        }
    }
}
