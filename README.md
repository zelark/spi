Simple Pascal Interpreter
==========================
This is a simple interpreter, which I have been writing in Java. It's based on a series ["Let's build a simple interpreter"] (https://ruslanspivak.com/lsbasi-part1/). The series is great. I highly recommend it to you.

For now, the interpreter supports following stuff:
- PROGRAM keyword;
- Block for declaration variables, which starts with VAR keyword;
- BEGIN ... END blocks;
- Integer variables like 'foo', 'bar' 'w42' and so on;
- Assignment operator ':=';
- Integer and real variables;
- binary operations: '+', '-', '*', 'div' and '/';
- unary operations: '+', '-';
- parenthesized expressions with arbitrary depth nesting.

### Grammar
```
    program : PROGRAM variable SEMI block DOT

    block : declarations compoundStatement

    declarations : VAR (varsDeclaration SEMI)+
                 | empty

    varsDeclaration : ID (COMMA ID)* COLON typeSpec

    typeSpec : INTEGER | REAL

    compoundStatement : BEGIN statementList END

    statementList : statement
                  | statement SEMI statementList

    statement : compoundStatement
              | assignmentStatement
              | empty

    assignmentStatement : variable ASSIGN expr

    variable : ID

    empty :

    term ((PLUS | MINUS) term)*

    term : factor ((MUL | INTEGER_DIV | REAL_DIV) factor)*

    factor : INTEGER_CONST
           | REAL_CONST
           | PLUS factor
           | MINUS factor
           | LPAREN expr RPAREN
           | variable
```

### How to use
```
> java -jar sip.jar test.pas
```