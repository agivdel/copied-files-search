package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Test;
import java.nio.file.Path;
import java.util.List;

public class SearcherTest {
    Searcher searcher = new Searcher();

    /**
     * Метод getDoublesByTimeFrom() возвращает список объектов Doubles.
     * Размер списка Doubles равен числу оригинальных файлов, подвергнутых копированию.
     */
    @Test
    public void doublesByTime_forMultipleOriginalFiles() throws InterruptedException {
        List<Forms> forms = getOriginalAndCopies();
        List<Doubles> doublesByTime = searcher.getDoublesByTimeFirst(forms);

        Assert.assertEquals(2, doublesByTime.size());
    }

    /**
     * Метод getDoublesByChecksumFrom() возвращает список объектов Doubles.
     * Размер списка Doubles равен числу оригинальных файлов, подвергнутых копированию.
     */
    @Test
    public void doublesByChecksum_forMultipleOriginalFiles() throws InterruptedException {
        List<Forms> forms = getOriginalAndCopies();
        List<Doubles> doublesByChecksum = searcher.getDoublesByChecksumFirst(forms);

        Assert.assertEquals(2, doublesByChecksum.size());
    }

    /**
     * Размер списка внутри объекта Doubles равен числу копий файла, включая оригинал.
     */
    @Test
    public void sizeOfListInDoubles_equalsNumbersOfCopiesIncludingOriginal() throws InterruptedException {
        List<Forms> forms = getOriginalAndCopies();
        List<Doubles> doubles = searcher.getDoublesByTimeFirst(forms);

        Assert.assertEquals(2, doubles.get(0).getDoubles().size());
        Assert.assertEquals(3, doubles.get(1).getDoubles().size());
    }

    /**
     * В списке внутри каждого объекта Doubles файл-оригинал и все его копии
     * имеют одинаковое время последнего редактирования.
     */
    @Test
    public void sameLastModifiedTimeInDoubles() throws InterruptedException {
        List<Forms> forms = getOriginalAndCopies();
        List<Doubles> doubles = searcher.getDoublesByTimeFirst(forms);

        long original1 = doubles.get(0).getDoubles().get(0).lastModified();
        long copy1_1 = doubles.get(0).getDoubles().get(1).lastModified();
        long original2 = doubles.get(1).getDoubles().get(0).lastModified();
        long copy2_1 = doubles.get(1).getDoubles().get(1).lastModified();
        long copy2_2 = doubles.get(1).getDoubles().get(2).lastModified();

        Assert.assertEquals(original1, copy1_1);
        Assert.assertEquals(original2, copy2_1);
        Assert.assertEquals(original2, copy2_2);
    }

    /**
     * На поиск файлов-копий не оказывает влияние время создания файла.
     */
    @Test
    public void sortedByCreateTime_Test() throws InterruptedException {
        List<Forms> forms = getOriginalAndCopies();
        List<Doubles> doubles = searcher.getDoublesByTimeFirst(forms);

        TestForm originalForm = (TestForm) doubles.get(0).getDoubles().get(0);
        long original = originalForm.createTime();
        TestForm copy1Form = (TestForm) doubles.get(0).getDoubles().get(1);
        long copy1 = copy1Form.createTime();
        TestForm copy2Form = (TestForm) doubles.get(0).getDoubles().get(2);
        long copy2 = copy2Form.createTime();
        TestForm copy3Form = (TestForm) doubles.get(0).getDoubles().get(3);
        long copy3 = copy3Form.createTime();

        Assert.assertNotEquals(original, copy1);
        Assert.assertNotEquals(copy1, copy2);
        Assert.assertNotEquals(copy2, copy3);
    }

    /**
     * На поиск файлов-копий не оказывают влияние имя и расширение файлов
     */
    @Test
    public void searchAmongDifferentNamesAndExtensions_Test() throws InterruptedException {
        Forms forms1 = new TestForm("1.txt", 10);
        Thread.sleep(1000);
        Forms forms1_copy1 = TestForm.copy(forms1, "1.jpeg");
        Thread.sleep(1000);
        Forms forms1_copy2 = TestForm.copy(forms1, "2.txt");
        Forms form2 = new TestForm("1.jpeg", 20);
        Forms form3 = new TestForm("2.txt", 30);
        List<Forms> forms = List.of(forms1, forms1_copy1, forms1_copy2, form2, form3);
        List<Doubles> doubles = searcher.getDoublesByTimeFirst(forms);

        Path name0 = doubles.get(0).getDoubles().get(0).toPath();
        Path name1 = doubles.get(0).getDoubles().get(1).toPath();
        Path name2 = doubles.get(0).getDoubles().get(2).toPath();

        Assert.assertNotEquals(name0, name1);
        Assert.assertNotEquals(name0, name2);
    }

    private List<Forms> getOriginalAndCopies() throws InterruptedException {
        Forms form1 = new TestForm("1", 10);
        Thread.sleep(1000);
        Forms form2 = new TestForm("2", 20);
        Thread.sleep(1000);
        Forms form2_copy1 = TestForm.copy(form2);
        Thread.sleep(1000);
        Forms form2_copy2 = TestForm.copy(form2);
        Thread.sleep(1000);
        Forms form2_copy3 = TestForm.copy(form2_copy1);
        return List.of(form1, form2, form2_copy1, form2_copy2, form2_copy3);
    }
}
