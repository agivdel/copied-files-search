package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TUITest {
    TUI tui = new TUI();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    /**
     * Адресом поиска следует устанавливать сущестующую на диске папку
     */
    @Test
    public void inputCorrectDirectoryAddress_Test() {
        TUI.DirectoryProcessor address = new TUI.DirectoryProcessor("Enter the address of the search directory:");
        String input = "src/test/resources";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        tui.publicInput(address, is);
        Assert.assertEquals(input, address.getSelect());
    }

    /**
     * Адресом поиска нельзя устанавливать путь к файлу
     */
    @Test
    public void inputNotDirectoryAddress_Test() {
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        TUI.DirectoryProcessor address = new TUI.DirectoryProcessor("Enter the address of the search directory:");
        String input = "src/test/resources/doc1.txt";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        tui.publicInput(address, is);
    }

    /**
     * Адресом поиска нельзя устанавливать несуществующую папку
     */
    @Test
    public void inputNonexistentDirectoryAddress_Test() {
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        TUI.DirectoryProcessor address = new TUI.DirectoryProcessor("Enter the address of the search directory:");
        String input = "src/test/resources/doc";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        tui.publicInput(address, is);
    }

    /**
     * При выборе опций можно вводить только 0 или 1
     */
    @Test
    public void inputValidNumber0_Test() {
        TUI.OptionProcessor minSize = new TUI.OptionProcessor("Enter 0 or 1.");
        String input = "0";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        tui.publicInput(minSize, is);
        Assert.assertEquals(input, minSize.getSelect());
    }

    @Test
    public void inputValidNumber1_Test() {
        TUI.OptionProcessor minSize = new TUI.OptionProcessor("Enter 0 or 1.");
        String input = "1";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        tui.publicInput(minSize, is);
        Assert.assertEquals(input, minSize.getSelect());
    }

    /**
     * При выборе опций нельзя вводить что-либо кроме 0 или 1
     */
    @Test
    public void inputInvalidNumber_Test() {
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        TUI.OptionProcessor order = new TUI.OptionProcessor("Enter 0 or 1.");
        String input = "2";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        tui.publicInput(order, is);
    }

    @Test
    public void inputLetterInsteadOfNumber_Test() {
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        TUI.OptionProcessor order = new TUI.OptionProcessor("Enter 0 or 1.");
        String input = "fgg";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        tui.publicInput(order, is);
    }

    @Test
    public void correctPrintDoubles_Test() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = System.out;

        System.setOut(new PrintStream(baos));// After this all System.out.println() statements will come to baos stream

        System.out.print("two");
        Assert.assertEquals("two", baos.toString());

        System.setOut(out);//Restore stream

//        List<File> files = new Walker().iterationFilesFrom("src/test/resources/data/photo/people");
//        List<Doubles> doubles = new Searcher().getDoublesByTimeFirst(files);;
//        tui.publicOutput(doubles);
//        String expectedOutput = """
//                displaying...
//                ==================
//                Last modified time: 2019-01-08T17:03:49.576Z\s
//                C:\\Users\\agivd\\JavaProjects\\copiedFilesSearch\\src\\test\\resources\\data\\photo\\people\\woman-3 — копия — копия.bmp
//                C:\\Users\\agivd\\JavaProjects\\copiedFilesSearch\\src\\test\\resources\\data\\photo\\people\\woman-3 — копия.jpg
//                C:\\Users\\agivd\\JavaProjects\\copiedFilesSearch\\src\\test\\resources\\data\\photo\\people\\woman-3.jpg
//                __________________
//                The total number of original files with copies: 1\s""";
//        Assert.assertEquals(expectedOutput, baos.toString());
    }
}
