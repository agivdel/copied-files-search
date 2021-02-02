package agivdel.copiedFilesSearch;

import java.io.*;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class UI {
    Walker.FileScanner fileScanner = Walker::allFilesFrom;
    Walker.ZeroRemover zeroRemover = Walker::removeZeroSizeForm;

    public static final Processor whatAddress = new DirectoryProcessor(
            "To search for copied files, enter the address of the search directory:");
    public static final Processor whatMinSize = new OptionProcessor(
            "Do you need to search among files with zero size? 'yes' - 0, 'no' - 1.");
    public static final Processor whatChecksumAlg = new OptionProcessor(
            "What algorithm should be used to calculate the checksum of files? 'CRC32' - 0, 'Adler32' - 1.");
    public static final Processor whatOrder = new OptionProcessor(
            "To group files first by checksum (slower) or last modified time (faster) when copies searching? 'checksum' - 0, 'time' - 1.");
    public static final Processor whatNext = new OptionProcessor(
            "To search for copies of files in another directory or exit the program? 'exit' - 0, 'search' - 1.");

    public void run() {
        boolean isRepeat;
        do {
            isRepeat = false;

            String address = input(whatAddress, in, out);
            out.println("counting files...");
//            List<Forms> files = walker.allFilesFrom(address);
            List<Forms> files = fileScanner.scan(address);

            String minSize = input(whatMinSize, in, out);
            if (minSize.equals("1")) {
                out.println("deleting files with zero size...");
//                files = walker.removeZeroSizeForm(files);
                files = zeroRemover.remove(files);
            }

            String checksumAlg = input(whatChecksumAlg, in, out);
            ChecksumCalculator calculator = null;
            if (checksumAlg.equals("1")) {
                calculator = Checker::getAdler32;
            } else {
                calculator = Checker::getCRC32;
            }

            String order = input(whatOrder, in, out);
            out.println("looking for duplicates...");
            List<Doubles> doubles;
            Searcher searcher = new Searcher(calculator);
            if (order.equals("1")) {
                doubles = searcher.getDoublesByTimeFirst(files);
            } else {
                doubles = searcher.getDoublesByChecksumFirst(files);
            }

            printAllDoubles(doubles, out);

            String nextAction = input(whatNext, in, out);
            if (nextAction.equals("1")) {
                isRepeat = true;
            }
        } while (isRepeat);
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
            long timeOfFirstFile = doubles.getDoubles().get(0).lastModified() * 1000;
            out.println("""
                    ==================
                    Last modified time: """ + FileTime.fromMillis(timeOfFirstFile));
            doubles.getDoubles().stream().map(Forms::toPath).forEach(out::println);
        }
        out.println("""
                __________________
                The total number of original files with copies: """ + doublesList.size());
    }
}
