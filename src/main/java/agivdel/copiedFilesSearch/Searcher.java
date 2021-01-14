package agivdel.copiedFilesSearch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.CRC32;

public class Searcher {

    public void run() throws IOException {
        while (true) {
            System.out.println("Для поиска скопированных файлов введите адрес директории поиска, для выхода нажмите z:");
            String selectedDirectory = input();
            List<File> fileList = iterationFilesFrom(selectedDirectory);
            System.out.println("Число файлов в данной директории: " + fileList.size());
            System.out.println("I`m working, don`t fuck me...");

            System.out.println("Всего " + getStreamByTimeAndChecksum(fileList).filter(s -> s.count() >= 1).count() + " групп копий");

            System.out.println("doubles: " + timeDoublesFromList(fileList).count());
            System.out.println("doubles with size() >= 1: " + timeDoublesFromList(fileList).filter(d -> d.size() >= 1).count());

            //каждый doubles проверить на CRC

        }
    }



    private Stream<Doubles> timeDoublesFromList(List<File> fileList) {
        return getTimeDoubles(fileList).map(Doubles::new);
    }

    private Stream<Stream<List<File>>> getStreamByTimeAndChecksum(List<File> fileList) {
        Stream<List<File>> timeDoublesStream = getTimeDoubles(fileList);
        return timeDoublesStream.map(timeDoubles -> {
            Stream<List<File>> checksumDoubleStream = null;
            try {
                checksumDoubleStream = getChecksumStreamFrom(timeDoubles);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return checksumDoubleStream;
        });
//        }).filter(Objects::nonNull).filter(s -> s.count() >= 1);//оставляем только стримы с одним и более листами -
//        вызывает ошибку из-за повторого использования стрима (stream has already been operated upon or closed)
    }

    private Stream<Stream<List<File>>> getStreamByChecksumAndTime(List<File> fileList) throws IOException {
        Stream<List<File>> checksumDoublesStream = getChecksumStreamFrom(fileList);
        return checksumDoublesStream.map(this::getTimeDoubles);
    }

    private void printStreamOfStream(Stream<Stream<List<File>>> stream) {
        stream.forEach(s -> s.forEach(l -> l.stream().map(File::getName).forEach(System.out::println)));
        //при подаче сюда фильтрованного стрима вознкиает ошибка повторного использвоания стрима
        //stream has already been operated upon or closed
    }

    private void printTimeOfProcess(List<File> fileList, long calcStart, long calcEnd, long printStart, long printEnd) {
        System.out.println("Число файлов в данной директории: " + fileList.size());
        System.out.println("время расчета: " + (calcEnd - calcStart) + " нс.");
        System.out.println("время печати: " + (printEnd - printStart) + " нс.");
        System.out.println("общее время: " + (printEnd - calcStart) + " нс.");
    }


    private String input() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String selectedDirectory = scanner.nextLine();
            if (selectedDirectory.equals("z")) {
                System.exit(0);
            }
            if (Files.exists(Path.of(selectedDirectory))) {
                return selectedDirectory;
            }
        }
    }

    private List<File> iterationFilesFrom(String selectedDirectory) {
        List<File> fileList = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(Paths.get(selectedDirectory))) {
            fileList = pathStream.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO дописать обработку исключения
        }
        return fileList;
    }

    //каждый лист - своя контрольная сумма (по CRC32); не подходит для файлов нулевого размера
    private Stream<List<File>> getChecksumStreamFrom(List<File> fileList) throws IOException {
        Map<Long, List<File>> checksumMap = new HashMap<>();
        for (File file : fileList) {
            Long key;
            key = getCRC32(file);
            checksumMap.putIfAbsent(key, new ArrayList<>());
            List<File> list = checksumMap.get(key);
            list.add(file);
            checksumMap.put(key, list);
        }
        return checksumMap.values().stream().filter(l -> l.size() > 1);
    }

    private Long getCRC32(File file) throws IOException {
        CRC32 check = new CRC32();
        byte[] buf = new byte[8000];//для чтения блоками по 8 КБ
        int length = 0;
        try (FileInputStream fis = new FileInputStream(file)) {
            while (true) {
                length = fis.read(buf);
                if (length < 0) {
                    break;
                }
                check.update(buf, 0, length);
            }
        }
        return check.getValue();
    }

    //каждый лист соответствует определенному времени последнего редактирования
    private Stream<List<File>> getTimeDoubles(List<File> fileList) {
        Map<Long, List<File>> map = fileList.stream().collect(Collectors.groupingBy(File::lastModified));
        return map.values().stream().filter(l -> l.size() > 1);
    }
}