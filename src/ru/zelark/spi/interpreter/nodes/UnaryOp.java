package ru.zelark.spi.interpreter.nodes;

import static ru.zelark.spi.interpreter.Token.TokenType.*;
import ru.zelark.spi.interpreter.Token;

import java.math.BigDecimal;

public class UnaryOp implements Evaluable<BigDecimal> {
    private Token operator;
    private Evaluable<BigDecimal> right;

    public UnaryOp (Token operator, Evaluable<BigDecimal> right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public BigDecimal evaluate() {
        if (operator.type() == PLUS) {
            return right.evaluate();
        }
        else if (operator.type() == MINUS) {
            return right.evaluate().negate();
        }
        else {
            throw new Error("Invalid operation");
        }
    }
}
