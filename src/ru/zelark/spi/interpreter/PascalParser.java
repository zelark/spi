package ru.zelark.spi.interpreter;

import ru.zelark.spi.interpreter.nodes.Assign;
import ru.zelark.spi.interpreter.nodes.BinOp;
import ru.zelark.spi.interpreter.nodes.Block;
import ru.zelark.spi.interpreter.nodes.Compound;
import ru.zelark.spi.interpreter.nodes.Evaluable;
import ru.zelark.spi.interpreter.nodes.NoOp;
import ru.zelark.spi.interpreter.nodes.Num;
import ru.zelark.spi.interpreter.nodes.Program;
import ru.zelark.spi.interpreter.nodes.Runnable;
import ru.zelark.spi.interpreter.nodes.Type;
import ru.zelark.spi.interpreter.nodes.UnaryOp;
import ru.zelark.spi.interpreter.nodes.Var;
import ru.zelark.spi.interpreter.nodes.VarsDeclaration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import static ru.zelark.spi.interpreter.Token.TokenType.*;

public class PascalParser implements Parser {
    private Iterator<Token> tokens;
    private Token currentToken;

    private final SymbolTable symbolTable;

    // precedence of operators
    private final EnumSet<Token.TokenType> level1 = EnumSet.of(MUL, INTEGER_DIV, REAL_DIV);
    private final EnumSet<Token.TokenType> level2 = EnumSet.of(PLUS, MINUS);

    public PascalParser(Lexer lexer, SymbolTable symbolTable) {
        this.tokens = lexer.tokenize().iterator();
        this.currentToken = this.tokens.next();
        this.symbolTable = symbolTable;
    }

    @Override
    public Runnable parse() {
        Runnable astTree = program();
        if (currentToken.type() != EOF) {
            error(currentToken.type());
        }
        return astTree;
    }

    // program : PROGRAM variable SEMI block DOT
    private Program program() {
        eat(PROGRAM);
        String progName = variable().name();
        eat(SEMI);
        Program prog = new Program(progName, block());
        eat(DOT);
        return prog;
    }

    // block : declarations compoundStatement
    private Block block() {
        return new Block(declarations(), compoundStatement());
    }

    // declarations : VAR (varsDeclaration SEMI)+
    //              | empty
    private List<VarsDeclaration> declarations() {
        List<VarsDeclaration> declarations = new ArrayList<>();
        if (currentToken.type() == VAR) {
            eat(VAR);
            while(currentToken.type() == ID) {
                declarations.add(varsDeclaration());
                eat(SEMI);
            }
        }
        return declarations;
    }

    // varsDeclaration : ID (COMMA ID)* COLON typeSpec
    private VarsDeclaration varsDeclaration() {
        List<Var> variables = new ArrayList<>();
        variables.add(new Var(currentToken, symbolTable));
        eat(ID);
        while (currentToken.type() == COMMA) {
            eat(COMMA);
            variables.add(new Var(currentToken, symbolTable));
            eat(ID);
        }
        eat(COLON);
        Type type = typeSpec();
        return new VarsDeclaration(variables, type);
    }

    // typeSpec : INTEGER | REAL
    private Type typeSpec() {
        Token token = currentToken;
        if (token.type() == INTEGER) {
            eat(INTEGER);
        }
        else {
            eat(REAL);
        }
        return new Type(token);
    }

    // compoundStatement : BEGIN statementList END
    private Runnable compoundStatement() {
        eat(BEGIN);
        List<Runnable> nodes = statementList();
        eat(END);
        return new Compound(nodes);
    }

    // statementList : statement
    //               | statement SEMI statementList
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

    // statement : compoundStatement
    //           | assignmentStatement
    //           | empty
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

    // assignmentStatement : variable ASSIGN expr
    private Runnable assignmentStatement() {
        Var left = variable();
        Token token = currentToken;
        eat(ASSIGN);
        Evaluable right = expr();
        return new Assign<BigDecimal>(left, right, symbolTable);
    }

    // variable : ID
    private Var variable() {
        Var node = new Var(currentToken, symbolTable);
        eat(ID);
        return node;
    }

    // empty :
    private Runnable empty() {
        return new NoOp();
    }

    // term ((PLUS | MINUS) term)*
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

    // term : factor ((MUL | INTEGER_DIV | REAL_DIV) factor)*
    private Evaluable term() {
        Evaluable node = factor();
        while (level1.contains(currentToken.type())) {
            Token token = currentToken;
            // TODO: it can be replaced by case.
            if (token.type() == MUL) {
                eat(MUL);
            }
            if (token.type() == INTEGER_DIV) {
                eat(INTEGER_DIV);
            }
            if (token.type() == REAL_DIV) {
                eat(REAL_DIV);
            }
            node = new BinOp(token, node, factor());
        }
        return node;
    }


    // factor : INTEGER_CONST
    //        | REAL_CONST
    //        | PLUS factor
    //        | MINUS factor
    //        | LPAREN expr RPAREN
    //        | variable
    private Evaluable factor() {
        Token token = currentToken;
        // TODO: it can be replaced by case.
        if (token.type() == INTEGER_CONST) {
            eat(INTEGER_CONST);
            return new Num<>(new BigDecimal(token.value()));
        }
        else if (token.type() == REAL_CONST) {
            eat(REAL_CONST);
            return new Num<>(new BigDecimal(token.value()));
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
