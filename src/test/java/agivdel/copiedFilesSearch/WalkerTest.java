package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class WalkerTest {
    File testResources = new File("src/test/resources");
    Walker walker = new Walker();
    List<File> fileList;

    @Before
    public void iteration() {
        fileList = walker.iterationFilesFrom(testResources.toString());
    }

    //обход директории поиска возвращает список всех ее файлов (не директорий), включая файлы во вложенных директориях
    @Test
    public void iterationAllFiles_Test() {
        Assert.assertEquals(27, fileList.size());
    }

    //обход пустой директории возвращает пустой список
    @Test
    public void iterationEmptyDir_Test() {
        fileList = walker.iterationFilesFrom(testResources.toString() + "/data/movie");
        Assert.assertTrue(fileList.isEmpty());
    }

    //удаляются только те файлы, размер которых равен 0
    @Test
    public void removeZeroSizeFiles_Test() {
        fileList = walker.removeZeroSize(fileList);
        Assert.assertEquals(23, fileList.size());
    }
}
