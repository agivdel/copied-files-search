package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;

public class SearcherTest {
    Searcher searcher = new Searcher();
    Walker walker = new Walker();

    private List<Doubles> getDoublesFrom(String dir) {
        List<File> files = walker.iterationFilesFrom(dir);
        return searcher.getDoublesByTimeThenChecksum(files);
    }

    private List<File> getFilesOfFirstDoubleFrom(String dir) {
        return getDoublesFrom(dir).get(0).getDoubles();
    }

    /**
     * Метод getDoublesList() возвращает список объектов Doubles,
     * внутри каждого из которых - список копий файла, включая оригинал
     */
    @Test
    public void doublesList_forMultipleOriginalFiles_Test() {
        List<Doubles> doubles = getDoublesFrom("src/test/resources");
        Assert.assertEquals(6, doubles.size());
    }

    /**
     * При наличии в папке поиска лишь одного оригинального файла с его копиями,
     * в результирующем списке только один объект Doubles
     */
    @Test
    public void doublesList_forOneOriginalFiles_Test() {
        List<Doubles> doubles = getDoublesFrom("src/test/resources/data/photo/people");
        Assert.assertEquals(1, doubles.size());
    }

    /**
     * Каждый элемент списка дубликатов содержит список всех файлов-копий и файл-оригинал
     */
    @Test
    public void fileList_forOneOriginalFiles_Test() {
        List<File> fileList = getFilesOfFirstDoubleFrom("src/test/resources/data/photo/people");
        Assert.assertEquals(3, fileList.size());
    }

    /**
     * В списке файлов каждого объекта Doubles файл-оригинал и все его копии
     * имеют одинаковое время последнего редактирования
     */
    @Test
    public void sameLastModifiedTime_Test() throws IOException {
        List<File> files = getFilesOfFirstDoubleFrom("src/test/resources/data/photo/landscape");
        File original = files.get(0);
        FileTime originalTime = Files.getLastModifiedTime(original.toPath());
        File copy1 = files.get(1);
        FileTime copy1Time = Files.getLastModifiedTime(copy1.toPath());
        File copy2 = files.get(2);
        FileTime copy2Time = Files.getLastModifiedTime(copy2.toPath());

        Assert.assertEquals(originalTime, copy1Time);
        Assert.assertEquals(originalTime, copy2Time);
    }

    /**
     * В списке файлов каждого объекта Doubles файл-оригинал и все его копии
     * сортируются по времени создания
     */
    @Test
    public void sortedByCreateTime_Test() throws IOException {
        List<File> files = getFilesOfFirstDoubleFrom("src/test/resources/data/photo/landscape");
        Path original = files.get(0).toPath();
        FileTime originalTime =Files.readAttributes(original, BasicFileAttributes.class).creationTime();
        Path copy1 = files.get(1).toPath();
        FileTime copy1Time = Files.readAttributes(copy1, BasicFileAttributes.class).creationTime();;
        Path copy2 = files.get(2).toPath();
        FileTime copy2Time = Files.readAttributes(copy2, BasicFileAttributes.class).creationTime();;

        Assert.assertEquals(-1, originalTime.compareTo(copy1Time));
        Assert.assertEquals(-1, copy1Time.compareTo(copy2Time));
    }

    /**
     * на поиск файлов-копий не оказывают влияние имя и расширение файлов
     */
    @Test
    public void searchAmongDifferentNamesAndExtensions_Test() {
        List<File> files = getFilesOfFirstDoubleFrom("src/test/resources/data/photo/landscape");
        String originalExtension = getExtension(files.get(0).getName());
        String copy1Extension = getExtension(files.get(1).getName());
        String copy2Extension = getExtension(files.get(2).getName());

        Assert.assertNotEquals(files.get(0).getName(), files.get(1).getName());
        Assert.assertNotEquals(files.get(0).getName(), files.get(2).getName());
        Assert.assertNotEquals(files.get(1).getName(), files.get(2).getName());

        Assert.assertNotEquals(originalExtension, copy1Extension);
        Assert.assertNotEquals(copy1Extension, copy2Extension);
    }

    private String getExtension(String filename) {
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            return filename.substring(i + 1);
        }
        return "";
    }
}
