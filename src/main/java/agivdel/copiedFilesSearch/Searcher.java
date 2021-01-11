package agivdel.copiedFilesSearch;

import java.io.File;
import java.io.IOException;
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

    private String directorySelect() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите адрес директории поиска скопированых файлов:");
            String selectedDirectory = scanner.nextLine();
            if (Files.exists(Path.of(selectedDirectory))) {
                return selectedDirectory;
            }
        }
    }

    private void iterationFilesFrom(String selectedDirectory) throws IOException {
        List<File> fileList = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(Paths.get(selectedDirectory))) {
            fileList = pathStream.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO дописать обработку исключения
        }
        System.out.println("Число файлов в данной директории: " + fileList.size());
        Map<FileTime, List<String>> fileMap = new HashMap<>();
        List<String> nameList;
        for (File file : fileList) {
            FileTime fileTime = Files.getLastModifiedTime(file.toPath());
            nameList = fileMap.get(fileTime);
            if (nameList == null) {
                nameList = new ArrayList<>();
            }
            nameList.add(file.getName());
            fileMap.put(fileTime, nameList);
        }
        Map<FileTime, List<String>> doubleFiles = fileMap
                .entrySet()
                .stream()
                .filter(l -> l.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        doubleFiles.entrySet().forEach(System.out::println);
    }
}