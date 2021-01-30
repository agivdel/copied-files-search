package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class UITestDB {

    @Test
    public void testSimpleIO() {
        UI ui = new UI();
        ui.fileScanner = new Walker.FileScanner() {
            @Override
            public List<Forms> scan(String name) {
                return List.of(
                        notEmpty("file 1", 1),
                        notEmpty("file 2", 1),
                        notEmpty("file 22", 2)
                );
            }
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        printStream.println(".");
        printStream.println("1");
        printStream.println("0");
        printStream.println("0");

        ui.in = new ByteArrayInputStream(baos.toByteArray());
        ByteArrayOutputStream uiOut = new ByteArrayOutputStream();
        ui.out = new PrintStream(uiOut);

        ui.run();

        String[] expectedOut = """
                To search for copied files, enter the address of the search directory:
                counting files...
                Do you need to search among files with zero size? 'yes' - 0, 'no' - 1.
                deleting files with zero size...
                To group files first by checksum (slower) or last modified time (faster) when copies searching? 'checksum' - 0, 'time' - 1.
                looking for duplicates...
                displaying...
                =================="
                Last modified time: 1970-01-02T03:55:00Z
                file 1
                file 2
                __________________
                The total number of original files with copies: 1
                To search for copies of files in another directory or exit the program? 'exit' - 0, 'search' - 1.
                                """.split("\n");
        String[] split = uiOut.toString().split(System.lineSeparator());
        Assert.assertArrayEquals(expectedOut, split);

    }

    @Test
    public void testRequestResponse() throws IOException, InterruptedException {
        UI ui = new UI();
        ui.fileScanner = new Walker.FileScanner() {
            @Override
            public List<Forms> scan(String name) {
                return List.of(
                        notEmpty("file 1", 1),
                        notEmpty("file 2", 1),
                        notEmpty("file 22", 2)
                );
            }
        };

        PipedOutputStream outUi = new PipedOutputStream();
        PipedInputStream inTest = new PipedInputStream(outUi, 1024 * 16);

        PipedOutputStream outTest = new PipedOutputStream();
        PipedInputStream inUi = new PipedInputStream(outTest, 1024 * 16);

        ui.in = inUi;
        ui.out = new PrintStream(outUi);

        PrintStream outTestPs = new PrintStream(outTest);
        Scanner inTestScanner = new Scanner(inTest);

        Thread thread = new Thread(ui::run);
        thread.start();

        assertEquals("To search for copied files, enter the address of the search directory:", inTestScanner.nextLine());

        outTestPs.println(".");

        assertEquals("counting files...", inTestScanner.nextLine());
        assertEquals("Do you need to search among files with zero size? 'yes' - 0, 'no' - 1.", inTestScanner.nextLine());

        outTestPs.println("1");

        assertEquals("deleting files with zero size...", inTestScanner.nextLine());
        assertEquals("To group files first by checksum (slower) or last modified time (faster) when copies searching? 'checksum' - 0, 'time' - 1.", inTestScanner.nextLine());

        outTestPs.println("0");

        assertEquals("looking for duplicates...", inTestScanner.nextLine());
        assertEquals("displaying...", inTestScanner.nextLine());
        assertEquals("==================\"", inTestScanner.nextLine());
        assertEquals("Last modified time: 1970-01-02T03:55:00Z", inTestScanner.nextLine());
        assertEquals("file 1", inTestScanner.nextLine());
        assertEquals("file 2", inTestScanner.nextLine());
        assertEquals("__________________", inTestScanner.nextLine());
        assertEquals("The total number of original files with copies: 1", inTestScanner.nextLine());
        assertEquals("To search for copies of files in another directory or exit the program? 'exit' - 0, 'search' - 1.", inTestScanner.nextLine());

        outTestPs.println("0");
    }

    static MyFile notEmpty(String name, long crc32) {
        return new MyFile(name, 1, crc32, 100500);
    }

    static class MyFile implements Forms {

        final long size;
        final long crc32;
        final long time;
        final String name;

        public MyFile(String name, long size, long crc32, long time) {
            this.name = name;
            this.size = size;
            this.crc32 = crc32;
            this.time = time;
        }

        @Override
        public Path toPath() {
            return Paths.get(name);
        }

        @Override
        public long size() {
            return size;
        }

        @Override
        public long lastModified() {
            return time;
        }

        @Override
        public long getCRC32() {
            return crc32;
        }
    }
}