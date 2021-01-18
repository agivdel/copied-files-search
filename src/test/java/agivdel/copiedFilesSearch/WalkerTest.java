package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class WalkerTest {
    String searchDir = "C:\\Users\\agivd\\JavaProjects\\copiedFilesSearch\\src\\main\\resources";
    Walker walker = new Walker();
    List<File> fileList;

    @Before
    public void iteration() {
        fileList = walker.iterationFilesFrom(searchDir);
    }

    @Test
    public void iterationFiles_Test() {
        Assert.assertEquals(27, fileList.size());
    }

    @Test
    public void removeZeroSize_Test() {
        fileList = walker.removeZeroSize(fileList);
        Assert.assertEquals(23, fileList.size());
    }
}
