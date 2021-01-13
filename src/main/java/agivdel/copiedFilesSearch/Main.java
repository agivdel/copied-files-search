package agivdel.copiedFilesSearch;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.zip.CRC32;

/**
 * Некто скопировал на компьютере какое-то количество файлов,
 * изменив у копий названия и, возможно, расширения.
 * Найти все копии.
 */

public class Main {
    private static final int SIZE = 100 * 1024 * 1024;

    public static void main(String[] args) throws IOException {
        new Searcher().run();
//        compareCRC(args);
//        tryDoubles2();
//        System.out.println(getDoubles());
    }

    private static void tryDoubles2() {
        String header = "header";
        Integer integer0 = 0;
        Integer integer1 = 1;
        Integer integer2 = 2;
        Doubles2<Integer> integerDoubles2 = new Doubles2<>(header, List.of(integer0, integer1, integer2));
        System.out.println("integerDoubles2: " + integerDoubles2);

        Long header2 = 200L;
        String s0 = "0";
        String s1 = "1";
        String s2 = "2";
        Doubles2<String> stringDoubles2 = new Doubles2<>(header2, List.of(s0, s1, s2));
        System.out.println("stringDoubles2: " + stringDoubles2);

        System.out.println(getNewDoubles2());
        System.out.println(getNewDoubles2().getList() == null);
        System.out.println(getNewDoubles2().getList().size());
        System.out.println(getNewDoubles2().getList().isEmpty());
    }

    private static Doubles2<String> getNewDoubles2() {
        return new Doubles2<>(1, new ArrayList<>());
    }

    private static Doubles2<Doubles2> getDoubles() {
        Doubles2<Integer> intDoubles2 = new Doubles2<>("int", List.of(0, 1, 2));
        Doubles2<String> stringDoubles2 = new Doubles2<>("string", List.of("s0", "s1", "s2"));
        Doubles2<Boolean> booleanDoubles2 = new Doubles2<>("boolean", List.of(true, true, false));
        return new Doubles2<>("doubles2", List.of(intDoubles2, stringDoubles2, booleanDoubles2));
    }

    public static void compareCRC(String[] args) {
        System.out.println("Creating arrays...");
        byte[] a1 = getArray();
        byte[] a2 = getArray();
        System.out.println("Starting comparison...");

        long startOfCompare = System.currentTimeMillis();
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] != a2[i]) {
                System.out.println("Failed on " + i);
                break;
            }
        }
        long endOfCompare = System.currentTimeMillis();
        System.out.println("Comparison takes " + (endOfCompare - startOfCompare) + " ms.");


        CRC32 crc1 = new CRC32();
        CRC32 crc2 = new CRC32();

        long startCRC = System.currentTimeMillis();
        crc1.update(a1);
        crc2.update(a2);
        if (crc1.getValue() != crc2.getValue()) {
            System.out.println("CRC Failed");
        }
        long endCRC = System.currentTimeMillis();
        System.out.println("CRC takes " + (endCRC - startCRC) + " ms.");
    }

    private static byte[] getArray() {
        Random r = new Random(1);
        byte[] array = new byte[SIZE];
        r.nextBytes(array);
        return array;
    }
}
