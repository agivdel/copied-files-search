package agivdel.copiedFilesSearch;

import java.util.List;

/**
 * Некто скопировал на компьютере какое-то количество файлов,
 * изменив у копий названия и, возможно, расширения.
 * Найти все копии.
 */

public class Main {
    public static void main(String[] args) {
        List<Instructions> instructionsList = new InstructionsMaker().getInstructions();
        new UI().run(instructionsList);
    }
}