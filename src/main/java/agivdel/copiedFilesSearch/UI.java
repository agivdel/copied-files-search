package agivdel.copiedFilesSearch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.*;

public class UI {
    Searcher searcher = new Searcher();
    Walker walker = new Walker();

    public static final Processor whatAddress = new DirectoryProcessor(
            "To search for copied files, enter the address of the search directory:");
    public static final Processor whatMinSize = new OptionProcessor(
            "Do you need to search among files with zero size? 'yes' - 0, 'no' - 1.");
    public static final Processor whatOrder = new OptionProcessor(
            "To group files first by checksum (slower) or last modified time (faster) when copies searching? 'checksum' - 0, 'time' - 1.");
    public static final Processor whatNext = new OptionProcessor(
            "To search for copies of files in another directory or exit the program? 'exit' - 0, 'search' - 1.");

    public void run() {
        boolean isRepeat;
        do {
            isRepeat = false;

            String selectedDirectory = input(whatAddress, in, out);
            List<Forms> files = walker.iterationFilesFrom(selectedDirectory);

            String zeroSize = input(whatMinSize, in, out);
            if (zeroSize.equals("1")) {
                files = walker.removeZeroSizeForm(files);
            }

            String grouper = input(whatOrder, in, out);
            out.println("looking for duplicates...");
            List<Doubles> doubles;
            if (grouper.equals("1")) {
                doubles = searcher.getDoublesByTimeFirst(files);
            } else {
                doubles = searcher.getDoublesByChecksumFirst(files);
            }
            printAllDoubles(doubles, out);

            String repeat = input(whatNext, in, out);
            if (repeat.equals("1")) {
                isRepeat = true;
            }
        } while (isRepeat);
        System.exit(0);
    }

    static class DirectoryProcessor implements Processor {
        private final String message;

        public DirectoryProcessor(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean isValid(String select) {
            return Files.isDirectory(Paths.get(select).normalize());
        }
    }

    static class OptionProcessor implements Processor {
        private final String message;

        public OptionProcessor(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean isValid(String select) {
            return select.equals("0") || select.equals("1");
        }
    }

    public static String input(Processor processor, InputStream is, PrintStream out) {
        Scanner scanner = new Scanner(is);
        out.println(processor.getMessage());
        String select;
        do {
            select = scanner.nextLine();
        } while (!processor.isValid(select));
        return select;
    }

    public static void printAllDoubles(List<Doubles> doublesList, PrintStream out) {
        out.println("displaying...");
        for (Doubles doubles : doublesList) {
            out.println("==================");
            Forms file = doubles.getDoubles().get(0);
            out.println("Last modified time: " + FileTime.fromMillis(file.lastModified() * 1000));
            doubles.getDoubles().stream().map(Forms::toPath).forEach(out::println);
        }
        out.println("""
                __________________
                The total number of original files with copies: """ + doublesList.size());
    }
}
