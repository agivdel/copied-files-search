package agivdel.copiedFilesSearch.Makers;

import agivdel.copiedFilesSearch.*;

import java.nio.file.attribute.FileTime;
import java.util.List;

import static java.lang.System.in;
import static java.lang.System.out;

public class InstructionMaker {

    public Instruction<Void, Boolean> getNew() {
        return selectDirectory.andThen(removeZeroSizeOrNot)
                .andThen(selectChecksumAlgorithm)
                .andThen(searchCopies)
                .andThen(printDoubles)
                .andThen(toRepeatOrNot);
    }

    static class FormsDTO {
        List<Form> files;

        public FormsDTO(List<Form> files) {
            this.files = files;
        }
    }

    static class FormsCalcDTO {
        List<Form> files;
        ChecksumCalculator calculator;

        public FormsCalcDTO(List<Form> files, ChecksumCalculator calculator) {
            this.files = files;
            this.calculator = calculator;
        }
    }

    static class DoublesDTO {
        List<Doubles> doubles;

        public DoublesDTO(List<Doubles> doubles) {
            this.doubles = doubles;
        }
    }

    Instruction<Void, FormsDTO> selectDirectory = new Instruction<>() {
        final Walker.FileScanner fileScanner = Walker::allFilesFrom;
        public final Handler whatAddress = new Handlers.Directory(
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
        public final Handler whatMinSize = new Handlers.Option(
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
        public final Handler whatChecksumAlg = new Handlers.Option(
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
        public final Handler whatOrder = new Handlers.Option(
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
            List<Doubles> doubles = doublesDTO.doubles;
            out.println("displaying...");
            for (Doubles aDouble : doubles) {
                long timeOfFirstFile = aDouble.getDoubles().get(0).lastModified() * 1000;
                out.println("""
                    ==================
                    Last modified time: """ + FileTime.fromMillis(timeOfFirstFile));
                aDouble.getDoubles().stream().map(Form::toPath).forEach(out::println);
            }
            out.println("""
                __________________
                The total number of original files with copies: """ + doubles.size());
            return null;
        }
    };

    Instruction<Void, Boolean> toRepeatOrNot = new Instruction<>() {
        public final Handler whatNext = new Handlers.Option(
                "To search for copies of files in another directory or exit the program? 'exit' - 0, 'search' - 1.");
        @Override
        public Boolean instruct(Void voi) {
            String nextAction = Input.input(whatNext, in, out);
            return nextAction.equals("1");
        }
    };
}