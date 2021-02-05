package agivdel.copiedFilesSearch.framework;

import java.util.List;

public class TestFormsMaker {
    public static List<Form> get5FormsWith1CopyFirstFormAnd3CopiesSecondForm() {
        TestForm form1 = new TestForm("1", 10, 50800, 100500);
        TestForm form1_copy1 = TestForm.copy(form1);
        TestForm form2 = new TestForm("2", 20, 10600, 300400);
        TestForm form2_copy1 = TestForm.copy(form2);
        TestForm form2_copy2 = TestForm.copy(form2_copy1);
        return List.of(form1, form1_copy1, form2, form2_copy1, form2_copy2);
    }

    public static List<Form> get5FormsWith3CopiesSecondForm() {
        TestForm form1 = new TestForm("1", 10, 50800, 100500);
        TestForm form2 = new TestForm("2", 20, 10600, 300400);
        TestForm form2_copy1 = TestForm.copy(form2);
        TestForm form2_copy2 = TestForm.copy(form2_copy1);
        TestForm form2_copy3 = TestForm.copy(form2_copy2);
        return List.of(form1, form2, form2_copy1, form2_copy2, form2_copy3);
    }

    public static List<Form> get5FormsNoCopies() {
        return List.of(
                new TestForm("1", 10, 50800, 100500),
                new TestForm("2", 20, 10600, 300400),
                new TestForm("3", 30, 100, 421405),
                new TestForm("4", 40, 9400, 499079),
                new TestForm("5", 50, 300, 25000)
        );
    }
}