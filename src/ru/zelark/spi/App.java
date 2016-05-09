package ru.zelark.spi;

import ru.zelark.spi.interpreter.PascalParser;
import ru.zelark.spi.interpreter.PascalLexer;
import ru.zelark.spi.interpreter.Lexer;
import ru.zelark.spi.interpreter.Token;
import ru.zelark.spi.interpreter.nodes.Runnable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class App {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Integer> ram = new HashMap<>();
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
                Runnable program = new PascalParser(lexer, ram).parse();
                program.run();
                printRam(ram);
            }
        }
    }

    public static void printRam(Map ram) {
        Iterator it = ram.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}
