package agivdel.copiedFilesSearch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.*;

public class UI {
    Searcher searcher = new Searcher();
    Walker walker = new Walker();

    public static final String whatAddress = "To search for copied files, enter the address of the search directory:";
    public static final String whatMinSize = "Do you need to search among files with zero size? 'yes' - 0, 'no' - 1.";
    public static final String whatOrder = "To group files first by checksum (slower) or last modified time (faster) when copies searching? 'checksum' - 0, 'time' - 1.";
    public static final String whatNext = "To search for copies of files in another directory or exit the program? 'exit' - 0, 'search' - 1.";

    public static final Processor address = new DirectoryProcessor(whatAddress);
    public static final OptionProcessor minSize = new OptionProcessor(whatMinSize);
    public static final OptionProcessor order = new OptionProcessor(whatOrder);
    public static final OptionProcessor next = new OptionProcessor(whatNext);

    public void run() throws IOException {
        boolean isRepeat;
        do {
            isRepeat = false;

            String selectedDirectory = input(address, in, out);
            List<File> files = walker.iterationFilesFrom(selectedDirectory);

            String zeroSize = input(minSize, in, out);
            if (zeroSize.equals("1")) {
                files = walker.removeZeroSize(files);
            }

            String grouper = input(order, in, out);
            out.println("looking for duplicates...");
            List<Doubles> doubles;
            if (grouper.equals("1")) {
                doubles = searcher.getDoublesByTimeFirst(files);
            } else {
                doubles = searcher.getDoublesByChecksumFirst(files);
            }
            printAllDoubles(doubles, out);

            String repeat = input(next, in, out);
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

    public static void printAllDoubles(List<Doubles> doublesList, PrintStream out) throws IOException {
        out.println("displaying...");
        for (Doubles doubles : doublesList) {
            out.println("==================");
            out.println("Last modified time: " + Files.getLastModifiedTime(doubles.getDoubles().get(0).toPath()));
            doubles.getDoubles().forEach(out::println);
        }
        out.println("__________________");
        out.println("The total number of original files with copies: " + doublesList.size());
    }
}
