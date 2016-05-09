package ru.zelark.spi.interpreter.nodes;

import static ru.zelark.spi.interpreter.Token.TokenType.*;
import ru.zelark.spi.interpreter.Token;

public class BinOp implements Evaluable<Integer> {
    private Token operator;
    private Evaluable<Integer> left;
    private Evaluable<Integer> right;

    public BinOp (Token operator, Evaluable<Integer> left, Evaluable<Integer> right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public Integer evaluate() {
        if (operator.type() == PLUS) {
            return left.evaluate() + right.evaluate();
        }
        else if (operator.type() == MINUS) {
            return left.evaluate() - right.evaluate();
        }
        else if (operator.type() == MUL) {
            return left.evaluate() * right.evaluate();
        }
        else if (operator.type() == DIV) {
            return left.evaluate() / right.evaluate();
        }
        else {
            throw new Error("Invalid operation");
        }
    }
}
