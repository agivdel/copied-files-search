package agivdel.copiedFilesSearch;

import java.nio.file.attribute.FileTime;
import java.util.List;

import static java.lang.System.in;
import static java.lang.System.out;

public class InstructionsMaker {

    public Instructions<Void, Boolean> getNew() {
        return selectDirectory.linkWith(removeZeroSizeOrNot)
                .linkWith(selectChecksumAlgorithm)
                .linkWith(searchCopies)
                .linkWith(printDoubles)
                .linkWith(toRepeatOrNot);
    }

    static class FormsDTO {
        List<Forms> files;

        public FormsDTO(List<Forms> files) {
            this.files = files;
        }
    }

    static class FormsCalcDTO {
        List<Forms> files;
        ChecksumCalculator calculator;

        public FormsCalcDTO(List<Forms> files, ChecksumCalculator calculator) {
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

    Instructions<Void, FormsDTO> selectDirectory = new Instructions<>() {
        final Walker.FileScanner fileScanner = Walker::allFilesFrom;
        public final Processor whatAddress = new DirectoryProcessor(
                "To search for copied files, enter the address of the search directory:");
        @Override
        public FormsDTO instruct(Void voi) {
            String address = UI.input(whatAddress, in, out);
            out.println("counting files...");
            return new FormsDTO(fileScanner.scan(address));
        }
    };

    Instructions<FormsDTO, FormsDTO> removeZeroSizeOrNot = new Instructions<>() {
        final Walker.ZeroRemover zeroRemover = Walker::removeZeroSizeForm;
        public final Processor whatMinSize = new OptionProcessor(
                "Do you need to search among files with zero size? 'yes' - 0, 'no' - 1.");
        @Override
        public FormsDTO instruct(FormsDTO formsDTO) {
            String minSize = UI.input(whatMinSize, in, out);
            final List<Forms> files = formsDTO.files;
            if (minSize.equals("1")) {
                out.println("deleting files with zero size...");
                return new FormsDTO(zeroRemover.remove(files));
            }
            return formsDTO;
        }
    };

    Instructions<FormsDTO, FormsCalcDTO> selectChecksumAlgorithm = new Instructions<>() {
        public final Processor whatChecksumAlg = new OptionProcessor(
                "What algorithm should be used to calculate the checksum of files? 'CRC32' - 0, 'Adler32' - 1.");
        @Override
        public FormsCalcDTO instruct(FormsDTO formsDTO) {
            String checksumAlg = UI.input(whatChecksumAlg, in, out);
            ChecksumCalculator calculator;
            if (checksumAlg.equals("1")) {
                calculator = Checker::getAdler32;
            } else {
                calculator = Checker::getCRC32;
            }
            return new FormsCalcDTO(formsDTO.files, calculator);
        }
    };

    Instructions<FormsCalcDTO, DoublesDTO> searchCopies = new Instructions<>() {
        public final Processor whatOrder = new OptionProcessor(
                "To group files first by checksum (slower) or last modified time (faster) when copies searching? 'checksum' - 0, 'time' - 1.");
        @Override
        public DoublesDTO instruct(FormsCalcDTO formsCalcDTO) {
            String order = UI.input(whatOrder, in, out);
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

    Instructions<DoublesDTO, Void> printDoubles = new Instructions<>() {
        @Override
        public Void instruct(DoublesDTO doublesDTO) {
            List<Doubles> doubles = doublesDTO.doubles;
            out.println("displaying...");
            for (Doubles aDouble : doubles) {
                long timeOfFirstFile = aDouble.getDoubles().get(0).lastModified() * 1000;
                out.println("""
                    ==================
                    Last modified time: """ + FileTime.fromMillis(timeOfFirstFile));
                aDouble.getDoubles().stream().map(Forms::toPath).forEach(out::println);
            }
            out.println("""
                __________________
                The total number of original files with copies: """ + doubles.size());
            return null;
        }
    };

    Instructions<Void, Boolean> toRepeatOrNot = new Instructions<>() {
        public final Processor whatNext = new OptionProcessor(
                "To search for copies of files in another directory or exit the program? 'exit' - 0, 'search' - 1.");
        @Override
        public Boolean instruct(Void voi) {
            String nextAction = UI.input(whatNext, in, out);
            return nextAction.equals("1");
        }
    };
}