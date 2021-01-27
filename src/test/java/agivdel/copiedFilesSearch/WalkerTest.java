package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

public class WalkerTest {
    Walker walker = new Walker();

    @Test
    public void test0() throws IOException {
//        FileSystem fileSystem = Jimfs.newFileSystem();
//        String fileName = "newFile.txt";
//        String fileName2 = "newFile2.txt";
//        String fileName3 = "newFile3.txt";
//        Path pathToStore = fileSystem.getPath("");
//        FileRepository fileRepository = new FileRepository();
//        fileRepository.create(pathToStore, fileName);
//        fileRepository.create(pathToStore, fileName2);
//        Assert.assertTrue(Files.exists(pathToStore.resolve(fileName)));
//        Assert.assertTrue(Files.exists(pathToStore.resolve(fileName2)));
//        System.out.println(fileName);
//        System.out.println(Files.getLastModifiedTime(Path.of(fileName)));
//        Assert.assertTrue(Files.exists(pathToStore.resolve(fileName3)));
//        Stream<Path> pathStream = Files.walk(pathToStore);
//        System.out.println(pathStream.count());
//        System.out.println(Files.exists(Paths.get(fileName)));
//        System.out.println(Files.exists(Paths.get(fileName2)));
    }

    /**
     * обход директории поиска возвращает список всех ее файлов (не директорий), включая файлы во вложенных директориях
     */
    @Test
    public void iterationAllFiles_Test() {
        List<Forms> files = walker.iterationFilesFrom("src/test/resources");
        Assert.assertEquals(8, files.size());
    }

    /**
     * обход пустой директории возвращает пустой список
     */
    @Test
    public void iterationEmptyDir_Test() {
        List<Forms> files = walker.iterationFilesFrom("src/test/resources/data/movie");
        Assert.assertTrue(files.isEmpty());
    }

    /**
     * удаляются только те файлы, размер которых равен 0 байт
     */
    @Test
    public void removeZeroSizeFiles_Test() {
        List<Forms> files = walker.iterationFilesFrom("src/test/resources");
        files = walker.removeZeroSizeForm(files);
        Assert.assertEquals(7, files.size());
    }
}
