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
        TUI.DirectoryProcessor address = new TUI.DirectoryProcessor(
                "To search for copied files, enter the address of the search directory:");
        String text = "src/test/resources";
        byte[] buffer = text.getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(buffer);
        tui.publicInput(is, address);
        Assert.assertEquals(text, address.getSelect());
    }

    /**
     * Адресом поиска нельзя устанавливать путь к файлу
     */
    @Test
    public void inputNotDirectoryAddress_Test() {
        TUI.DirectoryProcessor address = new TUI.DirectoryProcessor(
                "To search for copied files, enter the address of the search directory:");
        String text = "src/test/resources/doc1.txt";
        byte[] buffer = text.getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(buffer);
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        tui.publicInput(is, address);
    }

    /**
     * Адресом поиска нельзя устанавливать несуществующую папку
     */
    @Test
    public void inputNonexistentDirectoryAddress_Test() {
        TUI.DirectoryProcessor address = new TUI.DirectoryProcessor(
                "To search for copied files, enter the address of the search directory:");
        String text = "src/test/resources/doc";
        byte[] buffer = text.getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(buffer);
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        tui.publicInput(is, address);
    }

    /**
     * При выборе опций можно вводить только 0 или 1
     */
    @Test
    public void inputValidNumbers_Test() {
        TUI.OptionProcessor minSize = new TUI.OptionProcessor(
                "Do you need to search among files with zero size? 'yes' - 0, 'no' - 1.");
        String text_0 = "0";
        byte[] buffer = text_0.getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(buffer);
        tui.publicInput(is, minSize);
        Assert.assertEquals(text_0, minSize.getSelect());

        String text_1 = "1";
        buffer = text_1.getBytes(StandardCharsets.UTF_8);
        is = new ByteArrayInputStream(buffer);
        tui.publicInput(is, minSize);
        Assert.assertEquals(text_1, minSize.getSelect());
    }

    /**
     * При выборе опции "искать среди файлов с нулевым размером" нельзя вводить что-либо кроме 0 или 1
     */
    @Test
    public void inputInvalidNumbers_Test() {
        TUI.OptionProcessor order = new TUI.OptionProcessor(
                "To group files first by checksum (slower) or last modified time (faster) when copies searching? 'checksum' - 0, 'time' - 1.");
        String text = "2";
        byte[] buffer = text.getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(buffer);
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        tui.publicInput(is, order);
    }
}
