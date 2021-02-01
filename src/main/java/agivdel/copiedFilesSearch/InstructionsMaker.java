package agivdel.copiedFilesSearch;

import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;
import static java.lang.System.out;

public class InstructionsMaker {

    public List<Instructions> getInstructions() {
        List<Instructions> instructionsList = new ArrayList<>();

        Instructions instruction0 = new Instructions() {
            public final Processor whatAddress = new DirectoryProcessor(
                    "To search for copied files, enter the address of the search directory:");
            @Override
            public UI instruct(UI ui) {
                String address = UI.input(whatAddress, in, out);
                out.println("counting files...");
                ui.files = ui.fileScanner.scan(address);
                return ui;
            }
        };
        instructionsList.add(instruction0);

        Instructions instruction1 = new Instructions() {
            public final Processor whatMinSize = new OptionProcessor(
                    "Do you need to search among files with zero size? 'yes' - 0, 'no' - 1.");
            @Override
            public UI instruct(UI ui) {
                String minSize = UI.input(whatMinSize, in, out);
                if (minSize.equals("1")) {
                    out.println("deleting files with zero size...");
                    ui.files = ui.zeroRemover.remove(ui.files);
                }
                return ui;
            }
        };
        instructionsList.add(instruction1);

        Instructions instruction2 = new Instructions() {
            public final Processor whatChecksumAlg = new OptionProcessor(
                    "What algorithm should be used to calculate the checksum of files? 'CRC32' - 0, 'Adler32' - 1.");
            @Override
            public UI instruct(UI ui) {
                String checksumAlg = UI.input(whatChecksumAlg, in, out);
                if (checksumAlg.equals("1")) {//start of instruct();
                    ui.calculator = Checker::getAdler32;
                } else {
                    ui.calculator = Checker::getCRC32;
                }
                return ui;
            }
        };
        instructionsList.add(instruction2);

        Instructions instruction3 = new Instructions() {
            public final Processor whatOrder = new OptionProcessor(
                    "To group files first by checksum (slower) or last modified time (faster) when copies searching? 'checksum' - 0, 'time' - 1.");
            @Override
            public UI instruct(UI ui) {
                String order = UI.input(whatOrder, in, out);
                out.println("looking for duplicates...");
                Searcher searcher = new Searcher(ui.calculator);
                if (order.equals("1")) {
                    ui.doubles = searcher.getDoublesByTimeFirst(ui.files);
                } else {
                    ui.doubles = searcher.getDoublesByChecksumFirst(ui.files);
                }
                return ui;
            }
        };
        instructionsList.add(instruction3);

        Instructions instruction4 = new Instructions() {
            @Override
            public UI instruct(UI ui) {
                out.println("displaying...");
                for (Doubles doubles : ui.doubles) {
                    long timeOfFirstFile = doubles.getDoubles().get(0).lastModified() * 1000;
                    out.println("""
                    ==================
                    Last modified time: """ + FileTime.fromMillis(timeOfFirstFile));
                    doubles.getDoubles().stream().map(Forms::toPath).forEach(out::println);
                }
                out.println("""
                __________________
                The total number of original files with copies: """ + ui.doubles.size());
                return ui;
            }
        };
        instructionsList.add(instruction4);

        Instructions instruction5 = new Instructions() {
            public final Processor whatNext = new OptionProcessor(
                    "To search for copies of files in another directory or exit the program? 'exit' - 0, 'search' - 1.");
            @Override
            public UI instruct(UI ui) {
                String nextAction = UI.input(whatNext, in, out);
                if (nextAction.equals("1")) {
                    ui.isRepeat = true;
                }
                return ui;
            }
        };
        instructionsList.add(instruction5);

        return instructionsList;
    }
}