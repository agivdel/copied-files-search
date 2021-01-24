package agivdel.copiedFilesSearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.*;

public class TUI {
    Searcher searcher = new Searcher();
    Walker walker = new Walker();
    String whatAddress = "To search for copied files, enter the address of the search directory:";
    String whatMinSize = "Do you need to search among files with zero size? 'yes' - 0, 'no' - 1.";
    String whatOrder = "To group files first by checksum (slower) or last modified time (faster) when copies searching? 'checksum' - 0, 'time' - 1.";
    String whatNext = "To search for copies of files in another directory or exit the program? 'exit' - 0, 'search' - 1.";

    public void run() throws IOException {
        boolean isRepeat;
        do {
            isRepeat = false;

            DirectoryProcessor address = new DirectoryProcessor(whatAddress);
            input(address);
            List<File> files = walker.iterationFilesFrom(address.getSelect());

            OptionProcessor minSize = new OptionProcessor(whatMinSize);
            input(minSize);
            if (minSize.getSelect().equals("1")) {
                files = walker.removeZeroSize(files);
            }

            OptionProcessor order = new OptionProcessor(whatOrder);
            input(order);
            out.println("looking for duplicates...");
            List<Doubles> doubles;
            if (order.getSelect().equals("1")) {
                doubles = searcher.getDoublesByTimeFirst(files);
            } else {
                doubles = searcher.getDoublesByChecksumFirst(files);
            }
            printAllDoubles(doubles);

            OptionProcessor next = new OptionProcessor(whatNext);
            input(next);
            if (next.getSelect().equals("1")) {
                isRepeat = true;
            }
        } while (isRepeat);
        System.exit(0);
    }

    interface Processor {
        boolean isValid();
        String getMessage();
        String getSelect();
        default void read(String s) {}
    }

    static class DirectoryProcessor implements Processor {
        private final String message;
        private String select;

        public DirectoryProcessor(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getSelect() {
            return select;
        }

        @Override
        public void read(String s) {
            this.select = s;
        }

        @Override
        public boolean isValid() {
            return Files.isDirectory(Paths.get(select).normalize());
        }
    }

    static class OptionProcessor implements Processor {
        private final String message;
        private String select;

        public OptionProcessor(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getSelect() {
            return select;
        }

        @Override
        public void read(String s) {
            this.select = s;
        }

        @Override
        public boolean isValid() {
            return select.equals("0") || select.equals("1");
        }
    }

    private void input(Processor processor) {
        Scanner scanner = new Scanner(System.in);
        out.println(processor.getMessage());
        do {
            String s = scanner.nextLine();
            processor.read(s);
        } while (!processor.isValid());
    }

    private void printAllDoubles(List<Doubles> doublesList) throws IOException {
        out.println("displaying...");
        for (Doubles doubles : doublesList) {
            out.println("==================");
            out.println("Last modified time: " + Files.getLastModifiedTime(doubles.getDoubles().get(0).toPath()));
            doubles.getDoubles().forEach(System.out::println);
        }
        out.println("__________________");
        out.println("The total number of original files with copies: " + doublesList.size());
    }
}
