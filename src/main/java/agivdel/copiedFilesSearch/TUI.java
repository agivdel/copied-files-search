package agivdel.copiedFilesSearch;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class TUI {
    Searcher searcher = new Searcher();
    Walker walker = new Walker();

    public void run() throws IOException {
        PrintStream out = System.out;
        List<File> files;
        List<Doubles> doubles;
        boolean isRepeat;
        do {
            isRepeat = false;
            String selectedDirectory = input(new DirectoryProcessor("To search for copied files, enter the address of the search directory, to exit press z:"));
            files = walker.iterationFilesFrom(selectedDirectory);
            String minSize = input(new OptionProcessor("Do you need to search among files with zero size? 'yes' - 0, 'no' - 1."));
            if (minSize.equals("1")) {
                out.println("deleting files with zero size...");
                files = walker.removeZeroSize(files);
            }
            String order = input(new OptionProcessor("To group files first by checksum (slower) or last modified time (faster) when copies searching? 'checksum' - 0, 'time' - 1."));
            if (order.equals("1")) {
                doubles = searcher.getDoublesByTimeFirst(files);
            } else {
                doubles = searcher.getDoublesByChecksumFirst(files);
            }
            printAllDoubles(doubles);
            String repeat = input(new OptionProcessor("To search for copies of files in another directory or exit the program? 'exit' - 0, 'search' - 1."));
            if (repeat.equals("1")) {
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

    private String input(Processor processor) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(processor.getMessage());
        do {
            processor.read(scanner.nextLine());
        } while (!processor.isValid());
        return processor.getSelect();
    }

    private void printAllDoubles(List<Doubles> doublesList) throws IOException {
        for (Doubles doubles : doublesList) {
            System.out.println("==================");
            System.out.println("Last modified time: " + Files.getLastModifiedTime(doubles.getDoubles().get(0).toPath()));
            doubles.getDoubles().forEach(System.out::println);
        }
        System.out.println("__________________");
        System.out.println("The total number of original files with copies: " + doublesList.size());
    }
}
