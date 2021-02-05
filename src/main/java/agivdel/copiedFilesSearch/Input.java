package agivdel.copiedFilesSearch;

import agivdel.copiedFilesSearch.Makers.Handler;

import java.io.*;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Scanner;

public class Input {

    public static String input(Handler handler, InputStream is, PrintStream out) {
        Scanner scanner = new Scanner(is);
        out.println(handler.getMessage());
        String select;
        do {
            select = scanner.nextLine();
        } while (!handler.isValid(select));
        return select;
    }

    //пока используется только в тестах
    public static void printAllDoubles(List<Doubles> doublesList, PrintStream out) {
        out.println("displaying...");
        for (Doubles doubles : doublesList) {
            long timeOfFirstFile = doubles.getDoubles().get(0).lastModified() * 1000;
            out.println("""
                    ==================
                    Last modified time: """ + FileTime.fromMillis(timeOfFirstFile));
            doubles.getDoubles().stream().map(Form::toPath).forEach(out::println);
        }
        out.println("""
                __________________
                The total number of original files with copies: """ + doublesList.size());
    }
}
