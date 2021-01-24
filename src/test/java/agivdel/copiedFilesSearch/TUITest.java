package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
        tui.publicInput(is, address);
        Assert.assertEquals(input, address.getSelect());
    }

    /**
     * Адресом поиска нельзя устанавливать путь к файлу
     */
    @Test
    public void inputNotDirectoryAddress_Test() {
        TUI.DirectoryProcessor address = new TUI.DirectoryProcessor("Enter the address of the search directory:");
        String input = "src/test/resources/doc1.txt";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        tui.publicInput(is, address);
    }

    /**
     * Адресом поиска нельзя устанавливать несуществующую папку
     */
    @Test
    public void inputNonexistentDirectoryAddress_Test() {
        TUI.DirectoryProcessor address = new TUI.DirectoryProcessor("Enter the address of the search directory:");
        String input = "src/test/resources/doc";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        tui.publicInput(is, address);
    }

    /**
     * При выборе опций можно вводить только 0 или 1
     */
    @Test
    public void inputValidNumber0_Test() {
        TUI.OptionProcessor minSize = new TUI.OptionProcessor("Enter 0 or 1.");
        String input = "0";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        tui.publicInput(is, minSize);
        Assert.assertEquals(input, minSize.getSelect());
    }

    @Test
    public void inputValidNumber1_Test() {
        TUI.OptionProcessor minSize = new TUI.OptionProcessor("Enter 0 or 1.");
        String input = "1";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        tui.publicInput(is, minSize);
        Assert.assertEquals(input, minSize.getSelect());
    }

    /**
     * При выборе опции "искать среди файлов с нулевым размером" нельзя вводить что-либо кроме 0 или 1
     */
    @Test
    public void inputInvalidNumber_Test() {
        TUI.OptionProcessor order = new TUI.OptionProcessor("Enter 0 or 1.");
        String input = "2";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        tui.publicInput(is, order);
    }

    @Test
    public void inputLetterInsteadOfNumber_Test() {
        TUI.OptionProcessor order = new TUI.OptionProcessor("Enter 0 or 1.");
        String input = "fgg";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        tui.publicInput(is, order);
    }
}
