package agivdel.copiedFilesSearch.framework;

import agivdel.copiedFilesSearch.makers.InputHandler;

import java.io.*;
import java.util.Scanner;

public class Input {
    public static String input(InputHandler inputHandler, InputStream is, PrintStream out) {
        Scanner scanner = new Scanner(is);
        out.println(inputHandler.getMessage());
        String select;
        do {
            select = scanner.nextLine();
        } while (!inputHandler.isValid(select));
        return select;
    }
}