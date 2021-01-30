package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class UITestDB {

    @Test
    public void testSimpleIO() {
        UI ui = new UI();
        ui.fileScanner = name -> List.of(
                MyFile.notEmpty("file 1", 1),
                MyFile.notEmpty("file 2", 1),
                MyFile.notEmpty("file 22", 2)
        );

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
}