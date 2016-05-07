package ru.zelark.spi;

import ru.zelark.spi.interpreter.CalcLexer;
import ru.zelark.spi.interpreter.CalcParser;
import ru.zelark.spi.interpreter.nodes.Ast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("spi> ");
            String line = input.readLine();
            if (line == null || line.equals("exit")) {
                System.out.println();
                break;
            }
            else if (!line.equals("")) {
                Ast ast = new CalcParser(new CalcLexer(line)).parse();
                System.out.println(ast.evaluate());
            }
        }
    }
}
