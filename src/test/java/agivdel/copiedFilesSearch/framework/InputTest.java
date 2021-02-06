package agivdel.copiedFilesSearch.framework;

import agivdel.copiedFilesSearch.makers.InputHandlers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class InputTest {
    PrintStream out = System.out;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    /**
     * Адресом поиска следует устанавливать сущестующую на диске папку
     */
    @Test
    public void inputCorrectDirectoryAddress_Test() {
        InputHandler address = new InputHandlers.Directory("enter the address of the search directory:");
        String input = "src/test/resources";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        String output = Input.input(address, is, out);
        Assert.assertEquals(input, output);
    }

    /**
     * Адресом поиска нельзя устанавливать путь к файлу
     */
    @Test
    public void inputNotDirectoryAddress_Test() {
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        InputHandler address = new InputHandlers.Directory("enter the address of the search directory:");
        String input = "src/test/resources/doc1.txt";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Input.input(address, is, out);
    }

    /**
     * Адресом поиска нельзя устанавливать несуществующую папку
     */
    @Test
    public void inputNonexistentDirectoryAddress_Test() {
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        InputHandler address = new InputHandlers.Directory("enter the address of the search directory:");
        String input = "src/test/resources/doc";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Input.input(address, is, out);
    }

    /**
     * При выборе опций можно вводить только 0 или 1
     */
    @Test
    public void inputValidNumber0_Test() {
        InputHandler minSize = new InputHandlers.Option("enter 0 or 1.");
        String input = "0";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        String output = Input.input(minSize, is, out);
        Assert.assertEquals(input, output);
    }

    @Test
    public void inputValidNumber1_Test() {
        InputHandler minSize = new InputHandlers.Option("enter 0 or 1.");
        String input = "1";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        String output = Input.input(minSize, is, out);
        Assert.assertEquals(input, output);
    }

    /**
     * При выборе опций нельзя вводить что-либо кроме 0 или 1
     */
    @Test
    public void inputInvalidNumber_Test() {
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        InputHandler order = new InputHandlers.Option("enter 0 or 1.");
        String input = "2";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Input.input(order, is, out);
    }

    @Test
    public void inputLetterInsteadOfNumber_Test() {
        expectedEx.expect(java.util.NoSuchElementException.class);
        expectedEx.expectMessage("No line found");
        InputHandler order = new InputHandlers.Option("enter 0 or 1.");
        String input = "fgg";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Input.input(order, is, out);
    }
}