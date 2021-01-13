package agivdel.copiedFilesSearch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Searcher {

    public void run() throws IOException {
        while (true) {
            System.out.println("Для поиска скопированных файлов введите адрес директории поиска, для выхода нажмите z:");
            String selectedDirectory = input();
            List<File> fileList = iterationFilesFrom(selectedDirectory);
            System.out.println("Число файлов в данной директории: " + fileList.size());
            List<List<File>> timeDoubles = getTimeDoubles(fileList);
            List<List<List<File>>> sizeDoubles = getSizeDoubles(timeDoubles);
            printThis(sizeDoubles);
        }
    }

    private void printThis(List<List<List<File>>> sizeDoubles) throws IOException {
        for (List<List<File>> oneTime : sizeDoubles) {
            if (oneTime.isEmpty()) continue;
            System.out.println("Время " + Files.getLastModifiedTime(oneTime.get(0).get(0).toPath()));
            for (List<File> sizes : oneTime) {
                if (sizes.isEmpty()) continue;
                System.out.println("- Размер " + Files.size(sizes.get(0).toPath()) + " bytes");
                sizes.stream().map(File::getName).forEach(System.out::println);
            }
        }
        System.out.println("\nВсего файлов, имеющих копии: " + sizeDoubles.size());
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

    private List<List<File>> getTimeDoubles(List<File> fileList) throws IOException {
        Map<Long, List<File>> map = fileList.stream().collect(Collectors.groupingBy(File::lastModified));
        return map.values().stream().filter(l -> l.size() > 1).collect(Collectors.toList());
    }

    //TODO и это
    private List<List<List<File>>> getSizeDoubles(List<List<File>> timeDoubles) throws IOException {
        List<List<List<File>>> sizeDoubles = new ArrayList<>();
        for (List<File> oneTimeDoubles : timeDoubles) {
            Map<Long, List<File>> sizeMap = new HashMap<>();
            List<File> oneSizePaths;
            for (File file : oneTimeDoubles) {
                long size = Files.size(file.toPath());
                sizeMap.putIfAbsent(size, new ArrayList<>());
                oneSizePaths = sizeMap.get(size);
                oneSizePaths.add(file);
                sizeMap.put(size, oneSizePaths);
            }
            List<List<File>> sizes = sizeMap.values().stream().filter(l -> l.size() > 1).collect(Collectors.toList());
            sizeDoubles.add(sizes);
        }
        return sizeDoubles;
    }
}