package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

        File in = File.createTempFile("testRequestResponse-in", null);

        File out = File.createTempFile("testRequestResponse-out", null);

        System.out.println(in);
        System.out.println(out);

        ui.in = new FileInputStream(in);
        ui.out = new PrintStream(new FileOutputStream(out));

        Thread thread = new Thread(ui::run);
        thread.start();


        PrintStream writeIn = new PrintStream(new FileOutputStream(in));

        writeIn.println(".");
        writeIn.println("1");
        writeIn.println("0");
        writeIn.println("0");

        thread.join();

        Scanner readOut = new Scanner(new FileInputStream(out));
        List<String> lines = new ArrayList<>();
        while (readOut.hasNextLine()) {
            lines.add(readOut.nextLine());
        }

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
        String[] split = lines.toArray(lines.toArray(new String[0]));
        Assert.assertArrayEquals(expectedOut, split);

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