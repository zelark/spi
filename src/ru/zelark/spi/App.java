package ru.zelark.spi;

import ru.zelark.spi.interpreter.Lexer;
import ru.zelark.spi.interpreter.PascalLexer;
import ru.zelark.spi.interpreter.PascalParser;
import ru.zelark.spi.interpreter.SimpleSymbolTable;
import ru.zelark.spi.interpreter.SymbolTable;
import ru.zelark.spi.interpreter.nodes.Runnable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: sip <file.pas> ");
            System.exit(1);
        }

        Path file = Paths.get(args[0]);
        String text = new String(Files.readAllBytes(file));

        SymbolTable symbolTable = new SimpleSymbolTable();

        Lexer lexer = new PascalLexer(text);
        Runnable program = new PascalParser(lexer, symbolTable).parse();
        program.run();

        symbolTable.dump();
    }
}
