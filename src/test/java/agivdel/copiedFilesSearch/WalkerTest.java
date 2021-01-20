package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.util.List;

public class WalkerTest {
    Walker walker = new Walker();

    /**
     * обход директории поиска возвращает список всех ее файлов (не директорий), включая файлы во вложенных директориях
     */
    @Test
    public void iterationAllFiles_Test() {
        List<File> fileList = walker.iterationFilesFrom("src/test/resources");
        Assert.assertEquals(27, fileList.size());
    }

    /**
     * обход пустой директории возвращает пустой список
     */
    @Test
    public void iterationEmptyDir_Test() {
        List<File> fileList = walker.iterationFilesFrom("src/test/resources/data/movie");
        Assert.assertTrue(fileList.isEmpty());
    }

    /**
     * удаляются только те файлы, размер которых равен 0 байт
     */
    @Test
    public void removeZeroSizeFiles_Test() {
        List<File> fileList = walker.iterationFilesFrom("src/test/resources");
        fileList = walker.removeZeroSize(fileList);
        Assert.assertEquals(23, fileList.size());
    }
}
