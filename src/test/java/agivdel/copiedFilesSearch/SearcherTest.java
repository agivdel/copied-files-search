package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class SearcherTest {
    File testResources = new File("src/test/resources");
    Searcher searcher = new Searcher();
    Walker walker = new Walker();
    List<File> fileList;
    List<Doubles> doublesList;

    private List<Doubles> getDoublesListFrom(String dir) {
        fileList = walker.iterationFilesFrom(dir);
        return searcher.getDoublesList(fileList);
    }

    //метод getDoublesList() возвращает список объектов Doubles,
    // внутри каждого из которых - список копий файла, включая оригинал
    @Test
    public void doublesList_forMultipleOriginalFiles_Test() {
        doublesList = getDoublesListFrom(testResources.toString());
        Assert.assertEquals(6, doublesList.size());
    }

    //при наличии в папке поиска лишь одного оригинального файла с его копиями,
    // в результирующем списке только один объект Doubles
    @Test
    public void doublesList_forOneOriginalFiles_Test() {
        doublesList = getDoublesListFrom(testResources.toString() + "/data/photo/people");
        Assert.assertEquals(1, doublesList.size());
    }

    //каждый элемент списка дубликатов содержит список всех файлов-копий, считая и файл-оригинал
    @Test
    public void fileList_forOneOriginalFiles_Test() {
        doublesList = getDoublesListFrom(testResources.toString() + "/data/photo/people");
        fileList = doublesList.get(0).getDoubles();
        Assert.assertEquals(3, fileList.size());
    }
}
