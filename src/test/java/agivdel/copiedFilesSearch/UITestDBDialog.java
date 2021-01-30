package agivdel.copiedFilesSearch;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import static agivdel.copiedFilesSearch.MyFile.notEmpty;
import static org.junit.Assert.assertEquals;

public class UITestDBDialog {

    UI ui = new UI();
    PrintStream outTestPs;
    Scanner inTestScanner;

    @Before
    public void setUp() throws IOException {
        PipedOutputStream outUi = new PipedOutputStream();
        PipedInputStream inTest = new PipedInputStream(outUi, 1024 * 16);

        PipedOutputStream outTest = new PipedOutputStream();
        PipedInputStream inUi = new PipedInputStream(outTest, 1024 * 16);

        ui.in = inUi;
        ui.out = new PrintStream(outUi);

        outTestPs = new PrintStream(outTest);
        inTestScanner = new Scanner(inTest);
    }


    @Test(timeout = 20000)
    public void testRequestResponse() throws InterruptedException {
        ui.fileScanner = name -> List.of(
                notEmpty("file 1", 1),
                notEmpty("file 2", 1),
                notEmpty("file 22", 2)
        );

        Thread thread = new Thread(ui::run);
        thread.start();

        expectLine("To search for copied files, enter the address of the search directory:");

        outToConsole(".");

        expectLine("counting files...");
        expectLine("Do you need to search among files with zero size? 'yes' - 0, 'no' - 1.");

        outToConsole("1");

        expectLine("deleting files with zero size...");
        expectLine("To group files first by checksum (slower) or last modified time (faster) when copies searching? 'checksum' - 0, 'time' - 1.");

        outToConsole("0");

        expectLine("looking for duplicates...");
        expectLine("displaying...");
        expectLine("==================\"");
        expectLine("Last modified time: 1970-01-02T03:55:00Z");
        expectLine("file 1");
        expectLine("file 2");
        expectLine("__________________");
        expectLine("The total number of original files with copies: 1");
        expectLine("To search for copies of files in another directory or exit the program? 'exit' - 0, 'search' - 1.");

        outToConsole("0");

        thread.join();
    }

    private void outToConsole(String x) {
        outTestPs.println(x);
    }

    private void expectLine(String expected) {
        assertEquals(expected, inTestScanner.nextLine());
    }
}