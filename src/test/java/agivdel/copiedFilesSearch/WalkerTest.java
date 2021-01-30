package agivdel.copiedFilesSearch;

import com.google.common.jimfs.Jimfs;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class WalkerTest {
    Walker walker = new Walker();

    @Test
    public void test0() throws IOException {
        FileSystem fileSystem = Jimfs.newFileSystem();
        String fileName = "newFile.txt";
        String fileName2 = "newFile2.txt";
        String fileName3 = "newFile3.txt";
        Path pathToStore = fileSystem.getPath(".\\");
        FileRepository fileRepository = new FileRepository();
        fileRepository.create(pathToStore, fileName);
        fileRepository.create(pathToStore, fileName2);
        fileRepository.create(pathToStore, fileName3);
        Assert.assertTrue(Files.exists(pathToStore.resolve(fileName)));
        Assert.assertTrue(Files.exists(pathToStore.resolve(fileName2)));
        Assert.assertTrue(Files.exists(pathToStore.resolve(fileName3)));
        System.out.println(fileName);
        Stream<Path> pathStream = Files.walk(pathToStore);
        System.out.println(Files.exists(Paths.get(fileName)));
        System.out.println(Paths.get(fileName));
        System.out.println(Paths.get(fileName2));
    }

    /**
     * обход директории поиска возвращает список всех ее файлов (не директорий), включая файлы во вложенных директориях
     */
    @Test
    public void iterationAllFiles_Test() {
        List<Forms> files = walker.allFilesFrom("src/test/resources");
        Assert.assertEquals(8, files.size());
    }

    /**
     * обход пустой директории возвращает пустой список
     */
    @Test
    public void iterationEmptyDir_Test() {
        List<Forms> files = walker.allFilesFrom("src/test/resources/data/movie");
        Assert.assertTrue(files.isEmpty());
    }

    /**
     * удаляются только те файлы, размер которых равен 0 байт
     */
    @Test
    public void removeZeroSizeFiles_Test() {
        List<Forms> files = walker.allFilesFrom("src/test/resources");
        files = walker.removeZeroSizeForm(files);
        Assert.assertEquals(7, files.size());
    }
}
