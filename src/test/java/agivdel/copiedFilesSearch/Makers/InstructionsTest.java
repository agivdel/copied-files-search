package agivdel.copiedFilesSearch.Makers;

import agivdel.copiedFilesSearch.*;
import org.junit.Assert;

import agivdel.copiedFilesSearch.Makers.Instructions.FormsDTO;
import agivdel.copiedFilesSearch.Makers.Instructions.FormsCalcDTO;
import agivdel.copiedFilesSearch.Makers.Instructions.DoublesDTO;
import org.junit.Test;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class InstructionsTest {

    @Test
    public void input_correct_directory_selectDirectory() {
        Instructions ins = getInstructionsAndEnterString("src/test/resources");
        FormsDTO result = ins.selectDirectory.instruct(null);
        Assert.assertEquals(8, result.files.size());
    }

    @Test
    public void delete_zero_size_files_removeZeroSizeOrNot() {
        Instructions ins = getInstructionsAndEnterString("1");
        List<Form> files = List.of(
                new TestForm("1", 10),
                new TestForm("2", 20),
                new TestForm("3", 0)
        );
        FormsDTO inputDTO = new FormsDTO(files);
        FormsDTO result = ins.removeZeroSizeOrNot.instruct(inputDTO);
        Assert.assertEquals(2, result.files.size());
    }

    @Test
    public void select_checksum_algorithm_selectChecksumAlgorithm() {
        Instructions ins = getInstructionsAndEnterString("1");

        FormsDTO precursor = new FormsDTO(new ArrayList<>());
        FormsCalcDTO result = ins.selectChecksumAlgorithm.instruct(precursor);
        Assert.assertEquals("", result.calculator.getClass().toString());
    }

    @Test
    public void no_copies_searchCopies() {
        Instructions ins = getInstructionsAndEnterString("1");
        List<Form> files = TestFormsMaker.get5FormsNoCopies();
        FormsCalcDTO precursor = new FormsCalcDTO(files, Form::size);
        DoublesDTO result = ins.searchCopies.instruct(precursor);
        Assert.assertEquals(0, result.doubles.size());
    }

    @Test
    public void some_copies_searchCopies() {
        Instructions ins = getInstructionsAndEnterString("1");
        List<Form> files = TestFormsMaker.get5FormsWith3CopiesSecondForm();
        FormsCalcDTO precursor = new FormsCalcDTO(files, Form::size);
        DoublesDTO result = ins.searchCopies.instruct(precursor);
        Assert.assertEquals(1, result.doubles.size());
    }

    @Test
    public void print_doubles_printDoubles() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(baos);
        System.setOut(newOut);// After this all System.out.println() statements will come to baos stream
        List<Form> files = Walker.allFilesFrom("src/test/resources/data");
        List<Doubles> doubles = new Searcher(Form::size).getDoublesByTimeFirst(files);
        String input = """
                displaying...
                ==================
                Last modified time:2021-02-04T20:19:30Z
                src\\test\\resources\\data\\men-3 — копия.bmp
                src\\test\\resources\\data\\men-3 — копия.jpg
                src\\test\\resources\\data\\men-3.jpeg
                __________________
                The total number of original files with copies:1\r
                """;
        new Instructions().printDoubles.instruct(new DoublesDTO(doubles));
        String output = baos.toString();
        Assert.assertEquals(input, output);
        System.setOut(System.out);//Restore stream
    }

    @Test
    public void not_repeat_repeatOrNot() {
        Instructions ins = getInstructionsAndEnterString("0");
        Boolean result = ins.repeatOrNot.instruct(null);
        Assert.assertFalse(result);
    }

    @Test
    public void repeat_repeatOrNot() {
        Instructions ins = getInstructionsAndEnterString("1");
        Boolean result = ins.repeatOrNot.instruct(null);
        Assert.assertTrue(result);
    }

    private Instructions getInstructionsAndEnterString(String input) {
        Instructions ins = new Instructions();
        ins.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        return ins;
    }
}