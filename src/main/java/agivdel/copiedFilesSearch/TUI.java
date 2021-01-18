package agivdel.copiedFilesSearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class TUI {

    public String input(String control, String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        while (true) {
            String select = scanner.nextLine();
            if (select.equalsIgnoreCase("z")) System.exit(0);
            if (control.equals("dir") && validateDir(select)) return select;
            if (control.equals("zero") && validateNum(select)) return select;
        }
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

    public void printAllDoubles(List<Doubles> doublesList) throws IOException {
        for (Doubles doubles : doublesList) {
            System.out.println("==================");
            File first = doubles.getDoubles().get(0);
            System.out.println("Last modified time: " + Files.getLastModifiedTime(first.toPath()));
            System.out.println("Original file:\n" + first.getName());
            System.out.println("File copies: ");
            doubles.getDoubles().stream()
                    .map(File::getName)
                    .skip(1)
                    .forEach(System.out::println);
        }
        System.out.println("__________________");
        System.out.println("The total number of original files with copies: " + doublesList.size());
    }
}
