package agivdel.copiedFilesSearch;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.groupingBy;

/**
 * Группировка файлов по времени последнего редактирования, затем по контрольной сумме CRC32.
 */

public class Searcher {
    private final ChecksumCalculator calculator;

    public Searcher(ChecksumCalculator calculator) {
        this.calculator = calculator;
    }

    public List<Doubles> getDoublesByTimeFirst(List<Form> files) {
        return splitByTime(new Doubles(files))
                .flatMap(this::splitByChecksum)
                .collect(toList());
    }

    public List<Doubles> getDoublesByChecksumFirst(List<Form> files) {
        return splitByChecksum(new Doubles(files))
                .flatMap(this::splitByTime)
                .collect(toList());
    }

    private Stream<Doubles> splitByTime(Doubles doubles) {
        return doubles.getDoubles()
                .stream()
                .collect(groupingBy(Form::lastModified))
                .values()
                .stream()
                .filter(l -> l.size() > 1)
                .map(Doubles::new);
    }

    //каждый doubles - своя контрольная сумма; равна для файлов (не копий) одного (в том числе нулевого) размера
    private Stream<Doubles> splitByChecksum(Doubles doubles) {
        return doubles.getDoubles()
                .stream()
                .collect(groupingBy(calculator::calculateChecksum))
                .values()
                .stream()
                .filter(l -> l.size() > 1)
                .map(Doubles::new);
    }
}