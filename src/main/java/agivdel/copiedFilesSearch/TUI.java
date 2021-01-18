package agivdel.copiedFilesSearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class TUI {
    Searcher searcher = new Searcher();

    public void run() throws IOException {
        while (true) {
            String selectedDirectory = input("dir", "To search for copied files, enter the address of the search directory, to exit press z:");
            List<File> fileList = iterationFilesFrom(selectedDirectory);
            System.out.println("The number of files in this directory: " + fileList.size());
            String minSize = input("zero", "Do you need to search among files with zero size? 'Yes' - 0, 'No' - 1.");
            if (minSize.equals("1")) {
                fileList = removeZeroSize(fileList);
            }
            System.out.println("I'm working, don't bother me, please...");
            List<Doubles> doublesList = searcher.getDoublesList(fileList);
            printAllDoubles(doublesList);
        }
    }

    private String input(String control, String message) {
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

    private List<File> iterationFilesFrom(String selectedDirectory) {
        try (Stream<Path> pathStream = Files.walk(Paths.get(selectedDirectory))) {
            return pathStream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(toList());
        } catch (IOException e) {
            //TODO дописать обработку исключения
            throw new RuntimeException(e);
        }
    }

    private List<File> removeZeroSize(List<File> fileList) {
        return fileList.stream()
                .filter(f -> {
                    try {
                        return Files.size(f.toPath()) != 0;
                    } catch (IOException e) {
                        System.err.println("IO error");
                    }
                    return false;
                })
                .collect(toList());
    }

    private void printAllDoubles(List<Doubles> doublesList) throws IOException {
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
