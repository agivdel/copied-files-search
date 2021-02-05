package agivdel.copiedFilesSearch.makers;

import agivdel.copiedFilesSearch.framework.*;

import java.io.InputStream;
import java.nio.file.attribute.FileTime;
import java.util.List;

import static java.lang.System.out;

/**
 * Для работы программы необходимо создать:
 * анонимные классы инструкции, реализующих интерфейс Instruction,
 * статические классы DTO,
 * классы обработчиков введенных с консоли данных, реализующих интерфейс Handler,
 * а также указать последовательность выполнения инструкций в методе getNew().
 */

public class Instructions {
    InputStream in = System.in;

    //only for test
    void setIn(InputStream in) {
        this.in = in;
    }

    public Instruction<Void, Boolean> getNew() {
        return selectDirectory.andThen(removeZeroSizeOrNot)
                .andThen(selectChecksumAlgorithm)
                .andThen(searchCopies)
                .andThen(printDoubles)
                .andThen(repeatOrNot);
    }

    public static class FormsDTO {
        public List<Form> files;

        public FormsDTO(List<Form> files) {
            this.files = files;
        }
    }

    public static class FormsCalcDTO {
        public List<Form> files;
        public ChecksumCalculator calculator;

        public FormsCalcDTO(List<Form> files, ChecksumCalculator calculator) {
            this.files = files;
            this.calculator = calculator;
        }
    }

    public static class DoublesDTO {
        public List<Doubles> doubles;

        public DoublesDTO(List<Doubles> doubles) {
            this.doubles = doubles;
        }
    }

    Instruction<Void, FormsDTO> selectDirectory = new Instruction<>() {
        final Walker.FileScanner fileScanner = Walker::allFilesFrom;
        public final InputHandler whatAddress = new InputHandlers.Directory(
                "To search for copied files, enter the address of the search directory:");
        @Override
        public FormsDTO instruct(Void voi) {
            String address = Input.input(whatAddress, in, out);
            out.println("counting files...");
            return new FormsDTO(fileScanner.scan(address));
        }
    };

    Instruction<FormsDTO, FormsDTO> removeZeroSizeOrNot = new Instruction<>() {
        final Walker.ZeroRemover zeroRemover = Walker::removeZeroSizeForm;
        public final InputHandler whatMinSize = new InputHandlers.Option(
                "Do you need to search among files with zero size? 'yes' - 0, 'no' - 1.");
        @Override
        public FormsDTO instruct(FormsDTO formsDTO) {
            String minSize = Input.input(whatMinSize, in, out);
            final List<Form> files = formsDTO.files;
            if (minSize.equals("1")) {
                out.println("deleting files with zero size...");
                return new FormsDTO(zeroRemover.remove(files));
            }
            return formsDTO;
        }
    };

    Instruction<FormsDTO, FormsCalcDTO> selectChecksumAlgorithm = new Instruction<>() {
        public final InputHandler whatChecksumAlg = new InputHandlers.Option(
                "What algorithm should be used to calculate the checksum of files? 'CRC32' - 0, 'Adler32' - 1.");
        @Override
        public FormsCalcDTO instruct(FormsDTO formsDTO) {
            String checksumAlg = Input.input(whatChecksumAlg, in, out);
            ChecksumCalculator calculator;
            if (checksumAlg.equals("1")) {
                calculator = Checker::getAdler32;
            } else {
                calculator = Checker::getCRC32;
            }
            return new FormsCalcDTO(formsDTO.files, calculator);
        }
    };

    Instruction<FormsCalcDTO, DoublesDTO> searchCopies = new Instruction<>() {
        public final InputHandler whatOrder = new InputHandlers.Option(
                "To group files first by checksum (slower) or last modified time (faster) when copies searching? 'checksum' - 0, 'time' - 1.");
        @Override
        public DoublesDTO instruct(FormsCalcDTO formsCalcDTO) {
            String order = Input.input(whatOrder, in, out);
            out.println("looking for duplicates...");
            Searcher searcher = new Searcher(formsCalcDTO.calculator);
            List<Doubles> doubles;
            if (order.equals("1")) {
                doubles = searcher.getDoublesByTimeFirst(formsCalcDTO.files);
            } else {
                doubles = searcher.getDoublesByChecksumFirst(formsCalcDTO.files);
            }
            return new DoublesDTO(doubles);
        }
    };

    Instruction<DoublesDTO, Void> printDoubles = new Instruction<>() {
        @Override
        public Void instruct(DoublesDTO doublesDTO) {
            out.println("displaying...");
            for (Doubles doubles : doublesDTO.doubles) {
                long timeOfFirstFile = doubles.getDoubles().get(0).lastModified() * 1000;
                out.println("""
                    ==================
                    Last modified time: """ + FileTime.fromMillis(timeOfFirstFile));
                doubles.getDoubles().stream().map(Form::toPath).forEach(out::println);
            }
            out.println("""
                __________________
                The total number of original files with copies: """ + doublesDTO.doubles.size());
            return null;
        }
    };

    Instruction<Void, Boolean> repeatOrNot = new Instruction<>() {
        public final InputHandler whatNext = new InputHandlers.Option(
                "To search for copies of files in another directory or exit the program? 'exit' - 0, 'search' - 1.");
        @Override
        public Boolean instruct(Void voi) {
            String nextAction = Input.input(whatNext, in, out);
            return nextAction.equals("1");
        }
    };
}