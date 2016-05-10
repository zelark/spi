Sipmple Pascal Interpreter
==========================
This is a simple interpreter, which I have been writing in Java. It's based on a series ["Let's build a simple interpreter"] (https://ruslanspivak.com/lsbasi-part1/). The series is great. I highly recommend it to you.

For now, the interpreter can handle with only integers and supports following stuff:
- BEGIN ... END blocks;
- Integer variables like 'foo', 'bar' 'w' and so on;
- Assignment operator ':=';
- binary operations: '+', '-', '*', 'div';
- unary operations: '+', '-';
- parenthesized expressions with arbitrary depth nesting.

The complete grammar is:
```
    program : compoundStatement DOT

    compoundStatement : BEGIN statementList END

    statementList : statement
                  | statement SEMI statementList

    statement : compoundStatement
              | assignmentStatement
              | empty

    assignmentStatement : variable ASSIGN expr

    empty :

    expr : term ((PLUS | MINUS) term)*

    term : factor ((MUL | DIV) factor)*

    factor : PLUS factor
           | MINUS factor
           | INTEGER
           | LPAREN expr RPAREN
           | variable

    variable : ID
```