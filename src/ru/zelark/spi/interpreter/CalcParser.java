package ru.zelark.spi.interpreter;

import static ru.zelark.spi.interpreter.Token.TokenType.*;
import ru.zelark.spi.interpreter.nodes.Ast;
import ru.zelark.spi.interpreter.nodes.BinOp;
import ru.zelark.spi.interpreter.nodes.Num;
import ru.zelark.spi.interpreter.nodes.UnaryOp;
import java.util.EnumSet;
import java.util.Iterator;

public class CalcParser implements Parser {
    private Iterator<Token> tokens;
    private Token currentToken;

    // precedence of operators
    private final EnumSet<Token.TokenType> level1 = EnumSet.of(MUL, DIV);
    private final EnumSet<Token.TokenType> level2 = EnumSet.of(PLUS, MINUS);

    public CalcParser(Lexer lexer) {
        this.tokens = lexer.tokenize().iterator();
        this.currentToken = this.tokens.next();
    }

    @Override
    public Ast parse() {
        Ast astTree = expr();
        if (currentToken.type() != EOF) {
            error();
        }
        return astTree;
    }

    /**
     * expr   : term ((PLUS | MINUS) term)*
     * term   : factor ((MUL | DIV) factor)*
     * factor : (PLUS | MUNUS) factor | INTEGER | LPAREN expr RPAREN
     */
    private Ast expr() {
        Ast node = term();
        while (level2.contains(currentToken.type())) {
            Token token = currentToken;
            if (token.type() == PLUS) {
                eat(PLUS);
            }
            if (token.type() == MINUS) {
                eat(MINUS);
            }
            node = new BinOp(token, node, term());
        }
        return node;
    }

    private Ast term() {
        Ast node = factor();
        while (level1.contains(currentToken.type())) {
            Token token = currentToken;
            if (token.type() == MUL) {
                eat(MUL);
            }
            if (token.type() == DIV) {
                eat(DIV);
            }
            node = new BinOp(token, node, factor());
        }
        return node;
    }

    private Ast factor() {
        Token token = currentToken;
        if (token.type() == INTEGER) {
            eat(INTEGER);
            return new Num(token);
        }
        else if (token.type() == PLUS) {
            eat(PLUS);
            return new UnaryOp(token, factor());
        }
        else if (token.type() == MINUS) {
            eat(MINUS);
            return new UnaryOp(token, factor());
        }
        else {
            eat(LPAREN);
            Ast node = expr();
            eat(RPAREN);
            return node;
        }
    }

    private void eat(Token.TokenType type) {
        if (currentToken.type() == type) {
            currentToken = tokens.next();
        }
        else {
            error();
        }
    }

    private void error() {
        throw new Error("Invalid syntax");
    }
}
