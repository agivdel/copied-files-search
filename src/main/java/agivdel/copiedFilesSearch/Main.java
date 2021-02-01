package agivdel.copiedFilesSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Некто скопировал на компьютере какое-то количество файлов,
 * изменив у копий названия и, возможно, расширения.
 * Найти все копии.
 */

public class Main {
    public static void main(String[] args) {
        List<Instruction> instructionList = new ArrayList<>();
        new UI().run(instructionList);
    }
}