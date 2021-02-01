package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UITest {
    PrintStream out = System.out;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    /**
     * Адресом поиска следует устанавливать сущестующую на диске папку
     */
    @Test
    public void inputCorrectDirectoryAddress_Test() {
        Processor address = new DirectoryProcessor("enter the address of the search directory:");
        String input = "src/test/resources";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        String output = UI.input(address, is, out);
        Assert.assertEquals(input, output);
    }

    /**
     * Адресом поиска нельзя устанавливать путь к файлу
     */
    @Test
    public void inputNotDirectoryAddress_Test() {
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        Processor address = new DirectoryProcessor("enter the address of the search directory:");
        String input = "src/test/resources/doc1.txt";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        UI.input(address, is, out);
    }

    /**
     * Адресом поиска нельзя устанавливать несуществующую папку
     */
    @Test
    public void inputNonexistentDirectoryAddress_Test() {
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        Processor address = new DirectoryProcessor("enter the address of the search directory:");
        String input = "src/test/resources/doc";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        UI.input(address, is, out);
    }

    /**
     * При выборе опций можно вводить только 0 или 1
     */
    @Test
    public void inputValidNumber0_Test() {
        Processor minSize = new OptionProcessor("enter 0 or 1.");
        String input = "0";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        String output = UI.input(minSize, is, out);
        Assert.assertEquals(input, output);
    }

    @Test
    public void inputValidNumber1_Test() {
        Processor minSize = new OptionProcessor("enter 0 or 1.");
        String input = "1";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        String output = UI.input(minSize, is, out);
        Assert.assertEquals(input, output);
    }

    /**
     * При выборе опций нельзя вводить что-либо кроме 0 или 1
     */
    @Test
    public void inputInvalidNumber_Test() {
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        Processor order = new OptionProcessor("enter 0 or 1.");
        String input = "2";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        UI.input(order, is, out);
    }

    @Test
    public void inputLetterInsteadOfNumber_Test() {
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        Processor order = new OptionProcessor("enter 0 or 1.");
        String input = "fgg";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        UI.input(order, is, out);
    }

    @Test
    public void correctPrintDoubles_Test() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(baos);
        System.setOut(newOut);// After this all System.out.println() statements will come to baos stream
        List<Forms> files = Walker.allFilesFrom("src/test/resources/data");
        List<Doubles> doubles = new Searcher(Forms::size).getDoublesByTimeFirst(files);
        String input = """
                displaying...
                ==================
                Last modified time:2021-02-01T17:30:18Z
                src\\test\\resources\\data\\men-3 — копия.bmp
                src\\test\\resources\\data\\men-3 — копия.jpg
                src\\test\\resources\\data\\men-3.jpeg
                __________________
                The total number of original files with copies:1\r
                """;
        UI.printAllDoubles(doubles, newOut);
        String output = baos.toString();
        Assert.assertEquals(input, output);
        System.setOut(out);//Restore stream
    }
}
