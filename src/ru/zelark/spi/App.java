package ru.zelark.spi;

import ru.zelark.spi.interpreter.Token;
import ru.zelark.spi.interpreter.Lexer;
import ru.zelark.spi.interpreter.PascalLexer;
import ru.zelark.spi.interpreter.PascalParser;
import ru.zelark.spi.interpreter.SymbolTable;
import ru.zelark.spi.interpreter.SimpleSymbolTable;
import ru.zelark.spi.interpreter.nodes.Runnable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        SymbolTable symbolTable = new SimpleSymbolTable();
        while (true) {
            System.out.print("spi> ");
            String line = input.readLine();
            if (line == null || line.equals("exit")) {
                System.out.println();
                break;
            }
            else if (!line.equals("")) {
                Lexer lexer = new PascalLexer(line);
                for (Token token : lexer.tokenize()) {
                    System.out.println(token);
                }
                Runnable program = new PascalParser(lexer, symbolTable).parse();
                program.run();
                symbolTable.dump();
            }
        }
    }
}
