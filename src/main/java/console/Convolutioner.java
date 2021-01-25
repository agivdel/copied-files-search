package console;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.parseInt;

public class Convolutioner {
    public static void main(String[] args) {
        new Convolutioner().handleCommands(new Scanner(System.in), System.out);
    }

    public void handleCommands(final Scanner scanner, final PrintStream out) {
        if (scanner == null)
            throw new NullPointerException();
        if (out == null)
            throw new NullPointerException();
        out.println("enter 0 to find doubles");
        out.println("enter 1 to say hello (no args)");
        while (true) {
            int commandNumber = MIN_VALUE;
            try {
                commandNumber = parseInt(scanner.nextLine());
            } catch (Exception e) {
                out.println("can't read command number");
            }

            if (commandNumber >= 0 && commandNumber < 2) {
                final Map<String, Path> argumentValues_Path = new HashMap<>();
                final Map<String, Boolean> argumentValues_Boolean = new HashMap<>();
                if (commandNumber == 0) {
                    while (true) {
                        try {
                            out.println("enter path to folder");
                            final String pathString = scanner.nextLine();
                            Path path = null;
                            try {
                                path = Paths.get(pathString);
                            } catch (final Exception e) {
                                new Exception("can't resolve path: " + e.getMessage());
                            }
                            final boolean directory = Files.isDirectory(path);
                            if (directory) {
                                argumentValues_Path.put("path", path);
                            } else {
                                new Exception("not a directory");
                            }

                            out.println("enter skip empty files (yes/no)");
                            String scan = scanner.nextLine();
                            switch (scan.toLowerCase()) {
                                case "yes" -> argumentValues_Boolean.put("skipZero", true);
                                case "no" -> argumentValues_Boolean.put("skipZero", false);
                                default -> new Exception("please answer 'yes' or 'no'");
                            }

                            break;
                        } catch (Exception e) {
                            out.println("wrong argument value: " + e.getMessage());
                        }
                    }
                    final Path path = argumentValues_Path.get("path");
                    final Boolean skipZero = argumentValues_Boolean.get("skipZero");
                    System.out.printf("search in %s, skip zero: %s\n", path, skipZero);

                } else if (commandNumber == 1) {
                    out.println("Hello!");
                }
                return;
            } else {
                out.println("wrong command number");
            }
        }
    }
}
