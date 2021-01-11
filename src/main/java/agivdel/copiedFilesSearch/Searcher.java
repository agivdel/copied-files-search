package agivdel.copiedFilesSearch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//сравнение имен - ненадежно
//сравнение расширений - ненадежно
//сравнение размеров - ненадежно
//сравнение побитово - ненадежно
//сравнение времени последнего изменения

public class Searcher {

    public void run() throws IOException {
        String selectedDirectory = directorySelect();
        iterationFilesFrom(selectedDirectory);
    }

    private String directorySelect() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Для поиска скопированных файлов введите адрес директории поиска:");
            String selectedDirectory = scanner.nextLine();
            if (selectedDirectory.equals("z")) {
                System.exit(0);
            }
            if (Files.exists(Path.of(selectedDirectory))) {
                return selectedDirectory;
            }
        }
    }

    private void iterationFilesFrom(String selectedDirectory) throws IOException {
        List<File> fileList = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(Paths.get(selectedDirectory))) {
            fileList = pathStream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO дописать обработку исключения
        }
        System.out.println("Число файлов в данной директории: " + fileList.size());
        //Вариант №1, с мапой времени и списка имен файлов
        findDuplicates(fileList);
        //Вариант №2, с дополнительным классом Doubles(2 поля: время и список имен дублей)
        findDuplicates2(fileList);
    }

    //Вариант №1, с мапой времени и списка имен файлов
    private void findDuplicates(List<File> fileList) throws IOException {
        Map<FileTime, List<String>> fileMap = new HashMap<>();
        List<String> fileNameList;
        for (File file : fileList) {
            FileTime fileTime = Files.getLastModifiedTime(file.toPath());
            fileMap.putIfAbsent(fileTime, new ArrayList<>());
            fileNameList = fileMap.get(fileTime);
            fileNameList.add(file.getName());
            fileMap.put(fileTime, fileNameList);
        }

        Map<FileTime, List<String>> doubleFiles = fileMap
                .entrySet()
                .stream()
                .filter(l -> l.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        doubleFiles.entrySet().forEach(System.out::println);
    }

    //Вариант №2, с дополнительным классом Doubles(2 поля: время и список имен дублей)
    private void findDuplicates2(List<File> fileList) throws IOException {
        System.out.println("____________");
        Map<FileTime, Doubles> doublesMap = new HashMap<>();
        for (File file : fileList) {
            FileTime fileTime = Files.getLastModifiedTime(file.toPath());
            Doubles doubles = new Doubles(fileTime, List.of(file.getName()));
            doublesMap.merge(fileTime, doubles, Doubles::add);
        }
        System.out.println("doubles: ");
        System.out.println("size of doublesMap: " + doublesMap.size());
        doublesMap.values().stream().filter(d -> d.getDoubles().size() > 1).forEach(System.out::println);
    }

}