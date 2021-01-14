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

    public void run() {
        while (true) {
            System.out.println("Для поиска скопированных файлов введите адрес директории поиска, для выхода нажмите z:");
            String selectedDirectory = input();
            List<File> fileList = iterationFilesFrom(selectedDirectory);
            System.out.println("Число файлов в данной директории: " + fileList.size());
            System.out.println("I`m working, don`t fuck me...");

            //Вар.№1, поиск (среди 30 т.файлов) менее 1 с
            long calculateStart1 = System.nanoTime();
            Stream<List<File>> timeDoublesStream = getTimeDoubles(fileList);
            Stream<Stream<List<File>>> streamOfStream = timeDoublesStream.map(timeDoubles -> {
                Stream<List<File>> checksumDoubleStream = null;
                try {
                    checksumDoubleStream = getChecksumStreamFrom(timeDoubles);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return checksumDoubleStream;
            });
            long calculateEnd1 = System.nanoTime();
            //печать
            long printStart1 = System.nanoTime();
            streamOfStream.forEach(s -> s.forEach(l -> l.stream().map(File::getName).forEach(System.out::println)));
            long printEnd1 = System.nanoTime();

            System.out.println("Число файлов в данной директории: " + fileList.size());
            System.out.println("время расчета: " + (calculateEnd1 - calculateStart1));
            System.out.println("время печати: " + (printEnd1 - printStart1));
            System.out.println("общее время: " + (printEnd1 - calculateStart1));

            //Вар.№2, поиск (среди 30 т.файлов) около 13 мин
/*            long calculateStart2 = System.nanoTime();
            Stream<List<File>> checksumDoublesStream = getChecksumStreamFrom(fileList);
            Stream<Stream<List<File>>> streamOfStream2 = checksumDoublesStream.map(this::getTimeDoubles);
            long calculateEnd2 = System.nanoTime();
            //печать
            long printStart2 = System.nanoTime();
            streamOfStream2.forEach(s -> s.forEach(l -> l.stream().map(File::getName).forEach(System.out::println)));
            long printEnd2 = System.nanoTime();

            System.out.println("Число файлов в данной директории: " + fileList.size());
            System.out.println("время расчета: " + (calculateEnd2 - calculateStart2));
            System.out.println("время печати: " + (printEnd2 - printStart2));
            System.out.println("общее время: " + (printEnd2 - calculateStart2));*/
        }
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

    //не подходит для файлов нулевого размера
    private Stream<List<File>> getChecksumStreamFrom(List<File> fileList) throws IOException {
        Map<Long, List<File>> crcMap = new HashMap<>();
        for (File file : fileList) {
            Long key;
            key = getFileCRC32(file);
            crcMap.putIfAbsent(key, new ArrayList<>());
            List<File> list = crcMap.get(key);
            list.add(file);
            crcMap.put(key, list);
        }
        return crcMap.values().stream().filter(l -> l.size() > 1);
    }

    private Long getFileCRC32(File file) throws IOException {
        CRC32 check = new CRC32();
        byte[] buf = new byte[8000];//для чтения блоками по 8 КБ
        int length = 0;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (true) {
            assert fis != null;
            length = fis.read(buf);
            if (length < 0) {
                break;
            }
            check.update(buf, 0, length);
        }
        fis.close();
        return check.getValue();
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
    
    private Stream<List<File>> getTimeDoubles(List<File> fileList) {
        Map<Long, List<File>> map = fileList.stream().collect(Collectors.groupingBy(File::lastModified));
        return map.values().stream().filter(l -> l.size() > 1);
    }
}