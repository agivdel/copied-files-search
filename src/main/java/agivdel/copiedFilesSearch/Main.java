package agivdel.copiedFilesSearch;

/**
 * Некто скопировал на компьютере какое-то количество файлов,
 * изменив у копий названия и, возможно, расширения.
 * Найти все копии.
 */

public class Main {
    public static void main(String[] args) {
        while (new InstructionMaker()
                .getNew()
                .instruct(null)
        ) {}
    }
}