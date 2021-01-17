package agivdel.copiedFilesSearch;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.CRC32;

import static java.util.stream.Collectors.*;

/**
 * 1.получили лист файлов
 * 2.лист файлов преобразовали в стрим дублей, где каждый дубль - лист файлов с совпадающими временем последнего редактирования
 * 3.каждый дубль разделили на несколько по контрольной сумме, где каждый итоговый дубль - лист файлов с одинаковой контрольной суммой
 * 4.каждый новый дубль, полученный на стадии №3, добавили в общий стрим дублей
 */

public class Searcher {

    //группировка файлов по времени последнего редактирования и контрольной сумме CRC32
    public List<Doubles> getDoublesList(List<File> fileList) {
        return getTimeDoubles(fileList)
                .flatMap(this::splitByChecksum)
                .collect(toList());
    }

    //каждый лист соответствует определенному времени последнего редактирования
    private Stream<Doubles> getTimeDoubles(List<File> fileList) {
        return fileList.stream()
                .collect(groupingBy(File::lastModified))
                .values()
                .stream()
                .filter(l -> l.size() > 1)
                .map(Doubles::new);
    }

    //каждый лист - своя контрольная сумма (по CRC32); равна для файлов (не копий) одного (в том числе нулевого) размера
    private Stream<Doubles> splitByChecksum(Doubles doubles) {
        return doubles.getDoubles().stream()
                .collect(groupingBy(this::getCRC32))
                .values()
                .stream()
                .filter(l -> l.size() > 1)
                .map(Doubles::new);
    }

    private Long getCRC32(File file) {
        CRC32 check = new CRC32();
        byte[] buf = new byte[8000];//для чтения блоками по 8 КБ
        try (FileInputStream fis = new FileInputStream(file)) {
            while (true) {
                int length = fis.read(buf);
                if (length < 0) {
                    break;
                }
                check.update(buf, 0, length);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("IO error");
        }
        return check.getValue();
    }
}