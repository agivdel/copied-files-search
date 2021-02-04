package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Test;
import java.nio.file.Path;
import java.util.List;

public class SearcherTest {
    Searcher searcher = new Searcher(Form::size);

    /**
     * Метод getDoublesByTimeFrom() возвращает список объектов Doubles.
     * Размер списка Doubles равен числу оригинальных файлов, подвергнутых копированию.
     */
    @Test
    public void doublesByTime_forMultipleOriginalFiles() throws InterruptedException {
        List<Form> forms = getCopiesFromTwoOriginals();
        List<Doubles> doublesByTime = searcher.getDoublesByTimeFirst(forms);

        Assert.assertEquals(2, doublesByTime.size());
    }

    /**
     * Метод getDoublesByChecksumFrom() возвращает список объектов Doubles.
     * Размер списка Doubles равен числу оригинальных файлов, подвергнутых копированию.
     */
    @Test
    public void doublesByChecksum_forMultipleOriginalFiles() throws InterruptedException {
        List<Form> forms = getCopiesFromTwoOriginals();
        List<Doubles> doublesByChecksum = searcher.getDoublesByChecksumFirst(forms);

        Assert.assertEquals(2, doublesByChecksum.size());
    }

    /**
     * Размер списка внутри объекта Doubles равен числу копий файла, включая оригинал.
     */
    @Test
    public void sizeOfListInDoubles_equalsNumbersOfCopiesIncludingOriginal() throws InterruptedException {
        List<Form> forms = getCopiesFromOneOriginal();
        List<Doubles> doubles = searcher.getDoublesByTimeFirst(forms);

        Assert.assertEquals(4, doubles.get(0).getDoubles().size());
    }

    /**
     * В списке внутри каждого объекта Doubles файл-оригинал и все его копии
     * имеют одинаковое время последнего редактирования.
     */
    @Test
    public void sameLastModifiedTimeInDoubles() throws InterruptedException {
        List<Form> forms = getCopiesFromOneOriginal();
        List<Doubles> doubles = searcher.getDoublesByTimeFirst(forms);

        long original = doubles.get(0).getDoubles().get(0).lastModified();
        long copy1 = doubles.get(0).getDoubles().get(1).lastModified();
        long copy2 = doubles.get(0).getDoubles().get(2).lastModified();
        long copy3 = doubles.get(0).getDoubles().get(3).lastModified();

        Assert.assertEquals(original, copy1);
        Assert.assertEquals(original, copy2);
        Assert.assertEquals(original, copy3);
    }

    /**
     * На поиск файлов-копий не оказывает влияние время создания файла.
     */
    @Test
    public void searchAmongDifferentCreateTimes() throws InterruptedException {
        List<Form> forms = getCopiesFromOneOriginal();
        List<Doubles> doubles = searcher.getDoublesByTimeFirst(forms);

        long original = ((TestForm) doubles.get(0).getDoubles().get(0)).createTime();
        long copy1 = ((TestForm) doubles.get(0).getDoubles().get(1)).createTime();
        long copy2 = ((TestForm) doubles.get(0).getDoubles().get(2)).createTime();
        long copy3 = ((TestForm) doubles.get(0).getDoubles().get(3)).createTime();

        Assert.assertNotEquals(original, copy1);
        Assert.assertNotEquals(original, copy2);
        Assert.assertNotEquals(original, copy3);
    }

    /**
     * На поиск файлов-копий не оказывают влияние имя и расширение файлов
     */
    @Test
    public void searchAmongDifferentNamesAndExtensions() throws InterruptedException {
        Form form1 = new TestForm("1.txt", 10);
        Thread.sleep(1000);
        Form form1_copy1 = TestForm.copy(form1, "1.jpeg");
        Thread.sleep(1000);
        Form form1_copy2 = TestForm.copy(form1, "2.txt");
        Form form2 = new TestForm("1.jpeg", 20);
        Form form3 = new TestForm("2.txt", 30);
        List<Form> forms = List.of(form1, form1_copy1, form1_copy2, form2, form3);
        List<Doubles> doubles = searcher.getDoublesByTimeFirst(forms);

        Path name0 = doubles.get(0).getDoubles().get(0).toPath();
        Path name1 = doubles.get(0).getDoubles().get(1).toPath();
        Path name2 = doubles.get(0).getDoubles().get(2).toPath();

        Assert.assertNotEquals(name0, name1);
        Assert.assertNotEquals(name0, name2);
    }

    private List<Form> getCopiesFromTwoOriginals() throws InterruptedException {
        //TODO задавать время самостоятельно, sleep() удалить
        TestForm form1 = new TestForm("1", 10);
        Thread.sleep(1000);
        TestForm form1_copy1 = TestForm.copy(form1);
        Thread.sleep(1000);
        TestForm form2 = new TestForm("2", 20);
        Thread.sleep(1000);
        TestForm form2_copy1 = TestForm.copy(form2);
        Thread.sleep(1000);
        TestForm form2_copy2 = TestForm.copy(form2_copy1);
        return List.of(form1, form1_copy1, form2, form2_copy1, form2_copy2);
    }

    private List<Form> getCopiesFromOneOriginal() throws InterruptedException {
        TestForm form1 = new TestForm("1", 10);
        Thread.sleep(1000);
        TestForm form2 = new TestForm("2", 20);
        Thread.sleep(1000);
        TestForm form2_copy1 = TestForm.copy(form2);
        Thread.sleep(1000);
        TestForm form2_copy2 = TestForm.copy(form2_copy1);
        Thread.sleep(1000);
        TestForm form2_copy3 = TestForm.copy(form2_copy2);
        return List.of(form1, form2, form2_copy1, form2_copy2, form2_copy3);
    }
}
