package agivdel.copiedFilesSearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class TUI {
    Searcher searcher = new Searcher();
    Walker walker = new Walker();

    public void run() throws IOException {
        while (true) {
            String selectedDirectory = input("dir",
                    "To search for copied files, enter the address of the search directory, to exit press z:");
            System.out.println("counting files...");
            List<File> files = walker.iterationFilesFrom(selectedDirectory);
            System.out.println("The number of files in this directory: " + files.size());
            String minSize = input("one_zero",
                    "Do you need to search among files with zero size? 'Yes' - 0, 'No' - 1.");
            if (minSize.equals("1")) {
                System.out.println("deleting files with zero size...");
                files = walker.removeZeroSize(files);
            }
            String group = input("one_zero",
                    "To group files by checksum or last modified time when searching for copies? 'checksum' - 0, 'time' - 1.");
            List<Doubles> doubles;
            System.out.println("looking for duplicates...");
            if (group.equals("1")) {
                doubles = searcher.getDoublesByTimeThenChecksum(files);
            } else {
                doubles = searcher.getDoublesByChecksumThenTime(files);
            }
            System.out.println("displaying...");
            printAllDoubles(doubles);
        }
    }

    private String input(String control, String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        while (true) {
            String select = scanner.nextLine();
            if (jointValidation(control, select)) return select;
        }
    }

    //Don`t use! Only for test!
    public boolean validationTest(String control, String select) {
        return jointValidation(control, select);
    }

    private boolean jointValidation(String control, String select) {
        if (select.equalsIgnoreCase("z")) System.exit(0);
        if (control.equals("dir") && validateDir(select)) return true;
        return control.equals("one_zero") && validateNum(select);
    }

    private boolean validateDir(String select) {
        Path path = Paths.get(select).normalize();
        if (!Files.isDirectory(path)) {
            System.out.println("Address does not exist or is not a directory. Enter search directory:");
            return false;
        }
        return true;
    }

    private boolean validateNum(String select) {
        return select.equals("0") || select.equals("1");
    }

    private void printAllDoubles(List<Doubles> doublesList) throws IOException {
        for (Doubles doubles : doublesList) {
            System.out.println("==================");
            File first = doubles.getDoubles().get(0);
            System.out.println("Last modified time: " + Files.getLastModifiedTime(first.toPath()));
            System.out.println("Original file: ");
            System.out.println(first);
            System.out.println("File copies: ");
            for (int i = 1; i < doubles.getDoubles().size(); i++) {
                System.out.println(doubles.getDoubles().get(i));
            }
        }
        System.out.println("__________________");
        System.out.println("The total number of original files with copies: " + doublesList.size());
    }
}
