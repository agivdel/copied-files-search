package agivdel.copiedFilesSearch;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Scanner;

public class UI {
    Searcher searcher = new Searcher();
    Walker.FileScanner fileScanner = Walker::allFilesFrom;
    InputStream in = System.in;
    PrintStream out = System.out;

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

            Scanner scanner = new Scanner(in);

            String address = input(whatAddress, scanner, out);
            out.println("counting files...");
            List<Forms> files = fileScanner.scan(address);

            String minSize = input(whatMinSize, scanner, out);
            if (minSize.equals("1")) {
                out.println("deleting files with zero size...");
                files = Walker.removeZeroSizeForm(files);
            }

            String order = input(whatOrder, scanner, out);
            out.println("looking for duplicates...");
            List<Doubles> doubles;
            if (order.equals("1")) {
                doubles = searcher.getDoublesByTimeFirst(files);
            } else {
                doubles = searcher.getDoublesByChecksumFirst(files);
            }

            printAllDoubles(doubles, out);

            String nextAction = input(whatNext, scanner, out);
            if (nextAction.equals("1")) {
                isRepeat = true;
            }
        } while (isRepeat);
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

    public static String input(Processor processor, Scanner scanner, PrintStream out) {
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
            long timeOfFirstFile = doubles.getDoubles().get(0).lastModified() * 1000;
            out.println("==================\"");
            out.println("Last modified time: " + FileTime.fromMillis(timeOfFirstFile));
            doubles.getDoubles().stream().map(Forms::toPath).forEach(out::println);
        }
        out.println("__________________");
        out.println("The total number of original files with copies: " + doublesList.size());
    }
}
