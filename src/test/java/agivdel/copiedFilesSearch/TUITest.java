package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Test;

public class TUITest {
    TUI tui = new TUI();

    /**
     * Адресом поиска следует устанавливать сущестующую на диске папку
     */
    @Test
    public void inputCorrectDirectoryAddress_Test() {
        Assert.assertTrue(tui.validationTest("dir", "src/test/resources"));
    }

    /**
     * Адресом поиска нельзя устанавливать путь к файлу
     */
    @Test
    public void inputNotDirectoryAddress_Test() {
        Assert.assertFalse(tui.validationTest("dir", "C:/doc1.doc"));
    }

    /**
     * Адресом поиска нельзя устанавливать несуществующую папку
     */
    @Test
    public void inputNonexistentDirectoryAddress_Test() {
        Assert.assertFalse(tui.validationTest("dir", "C:/Nonexistent"));
    }

    /**
     * При выборе опции "искать среди файлов с нулевым размером" нужно вводить 0 или 1
     */
    @Test
    public void inputValidNumbers_Test() {
        Assert.assertTrue(tui.validationTest("zero", String.valueOf(0)));
        Assert.assertTrue(tui.validationTest("zero", String.valueOf(1)));
    }

    /**
     * При выборе опции "искать среди файлов с нулевым размером" нельзя вводить что-либо кроме 0 или 1
     */
    @Test
    public void inputInvalidNumbers_Test() {
        Assert.assertFalse(tui.validationTest("zero", "2"));
        Assert.assertFalse(tui.validationTest("zero", "sfhf"));
    }
}
