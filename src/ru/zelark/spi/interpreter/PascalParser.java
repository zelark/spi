package ru.zelark.spi.interpreter;

import static ru.zelark.spi.interpreter.Token.TokenType.*;
import ru.zelark.spi.interpreter.nodes.Assign;
import ru.zelark.spi.interpreter.nodes.Evaluable;
import ru.zelark.spi.interpreter.nodes.Num;
import ru.zelark.spi.interpreter.nodes.Runnable;
import ru.zelark.spi.interpreter.nodes.UnaryOp;
import ru.zelark.spi.interpreter.nodes.BinOp;
import ru.zelark.spi.interpreter.nodes.NoOp;
import ru.zelark.spi.interpreter.nodes.Compound;
import ru.zelark.spi.interpreter.nodes.Var;
import java.util.List;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;

public class PascalParser implements Parser {
    private Iterator<Token> tokens;
    private Token currentToken;

    private final Map<String, Integer> ram;

    // precedence of operators
    private final EnumSet<Token.TokenType> level1 = EnumSet.of(MUL, DIV);
    private final EnumSet<Token.TokenType> level2 = EnumSet.of(PLUS, MINUS);

    public PascalParser(Lexer lexer, Map<String, Integer> ram) {
        this.tokens = lexer.tokenize().iterator();
        this.currentToken = this.tokens.next();
        this.ram = ram;
    }

    @Override
    public Runnable parse() {
        Runnable astTree = program();
        if (currentToken.type() != EOF) {
            error(currentToken.type());
        }
        return astTree;
    }

    /**
         program: compound_statement DOT

         compound_statement: BEGIN statement_list END

         statement_list: statement | statement SEMI statement_list

         statement: compound_statement | assignment_statement | empty

         assignment_statement: variable ASSIGN expr

         empty:

         expr: term ((PLUS | MINUS) term)*

         term: factor ((MUL | DIV) factor)*

         factor: PLUS factor | MINUS factor | INTEGER | LPAREN expr RPAREN | variable

         variable: ID
     */

    private Runnable program() {
        Runnable node = compoundStatement();
        eat(DOT);
        return node;
    }

    private Runnable compoundStatement() {
        eat(BEGIN);
        List<Runnable> nodes = statementList();
        eat(END);
        return new Compound(nodes);
    }

    private List<Runnable> statementList() {
        Runnable node = statement();
        List<Runnable> statements = new ArrayList<>();
        statements.add(node);
        while (currentToken.type() == SEMI) {
            eat(SEMI);
            statements.add(statement());
        }
        return statements;
    }

    private Runnable statement() {
        if (currentToken.type() == BEGIN) {
            return compoundStatement();
        }
        else if (currentToken.type() == ID) {
            return assignmentStatement();
        }
        else {
            return empty();
        }
    }

    private Runnable assignmentStatement() {
        Var left = variable();
        Token token = currentToken;
        eat(ASSIGN);
        Evaluable right = expr();
        return new Assign<Integer>(left, right, ram);
    }

    private Var variable() {
        Var node = new Var(currentToken, ram);
        eat(ID);
        return node;
    }

    private Runnable empty() {
        return new NoOp();
    }

    private Evaluable expr() {
        Evaluable node = term();
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

    private Evaluable term() {
        Evaluable node = factor();
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

    private Evaluable factor() {
        Token token = currentToken;
        if (token.type() == INTEGER) {
            eat(INTEGER);
            return new Num<Integer>(token);
        }
        else if (token.type() == PLUS) {
            eat(PLUS);
            return new UnaryOp(token, factor());
        }
        else if (token.type() == MINUS) {
            eat(MINUS);
            return new UnaryOp(token, factor());
        }
        else if (token.type() == LPAREN) {
            eat(LPAREN);
            Evaluable node = expr();
            eat(RPAREN);
            return node;
        }
        else {
            return variable();
        }
    }

    private void eat(Token.TokenType type) {
        if (currentToken.type() == type) {
            currentToken = tokens.next();
        }
        else {
            error(type);
        }
    }

    private void error(Token.TokenType type) {
        String message = String.format("Invalid syntax: expected token type is %s but actual one is %s.",
                type.toString(), currentToken.type().toString());
        throw new Error(message);
    }
}
