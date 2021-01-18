package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class SearcherTest {
    File testResources = new File("src/test/resources");

    //метод getDoublesList() возвращает список объектов Doubles, внутри каждого из которых - список копий файла, включая оригинал
    @Test
    public void getDoublesList_Test() {
        Searcher searcher = new Searcher();
        Walker walker = new Walker();
        List<File> fileList = walker.iterationFilesFrom(testResources.toString());

        List<Doubles> doublesList = searcher.getDoublesList(fileList);
        Assert.assertEquals(6, doublesList.size());
    }
}
