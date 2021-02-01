package agivdel.copiedFilesSearch;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.groupingBy;

/**
 * Группировка файлов по времени последнего редактирования, затем по контрольной сумме CRC32.
 */

public class Searcher {
    public List<Doubles> getDoublesByTimeFirst(List<Forms> files) {
        return splitByTime(new Doubles(files))
                .flatMap(this::splitByChecksum)
                .collect(toList());
    }

    public List<Doubles> getDoublesByChecksumFirst(List<Forms> files) {
        return splitByChecksum(new Doubles(files))
                .flatMap(this::splitByTime)
                .collect(toList());
    }

    private Stream<Doubles> splitByTime(Doubles doubles) {
        return doubles.getDoubles()
                .stream()
                .collect(groupingBy(Forms::lastModified))
                .values()
                .stream()
                .filter(l -> l.size() > 1)
                .map(Doubles::new);
    }

    //каждый doubles - своя контрольная сумма (по CRC32); равна для файлов (не копий) одного (в том числе нулевого) размера
    private Stream<Doubles> splitByChecksum(Doubles doubles) {
        return doubles.getDoubles()
                .stream()
                .collect(groupingBy(Forms::getChecksum))
                .values()
                .stream()
                .filter(l -> l.size() > 1)
                .map(Doubles::new);
    }
}