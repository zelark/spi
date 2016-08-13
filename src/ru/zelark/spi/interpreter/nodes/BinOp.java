package ru.zelark.spi.interpreter.nodes;

import static ru.zelark.spi.interpreter.Token.TokenType.*;
import ru.zelark.spi.interpreter.Token;

import java.math.BigDecimal;

public class BinOp implements Evaluable<BigDecimal> {
    private Token operator;
    private Evaluable<BigDecimal> left;
    private Evaluable<BigDecimal> right;

    public BinOp(Token operator, Evaluable<BigDecimal> left, Evaluable<BigDecimal> right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public BigDecimal evaluate() {
        if (operator.type() == PLUS) {
            return left.evaluate().add(right.evaluate());
        }
        else if (operator.type() == MINUS) {
            return left.evaluate().subtract(right.evaluate());
        }
        else if (operator.type() == MUL) {
            return left.evaluate().multiply(right.evaluate());
        }
        else if (operator.type() == INTEGER_DIV) {
            return left.evaluate().divide(right.evaluate(), BigDecimal.ROUND_DOWN);
        }
        else if (operator.type() == REAL_DIV) {
            return left.evaluate().divide(right.evaluate(), 20, BigDecimal.ROUND_DOWN);
        }
        else {
            throw new Error("Invalid operation");
        }
    }
}
