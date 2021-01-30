package agivdel.copiedFilesSearch;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class TestReadWriteApproach {

    @Test
    public void testPipedStreams() throws IOException, InterruptedException {
        PipedOutputStream outThread = new PipedOutputStream();
        PipedInputStream inTest = new PipedInputStream(outThread, 1024 * 16);

        Thread thread = new Thread(() -> {
            try {
                PrintStream printStream = new PrintStream(outThread);
                Thread.sleep(1000);
                printStream.println("хуй");
                Thread.sleep(2000);
                printStream.println("пизда");
                Thread.sleep(5000);
                printStream.println("джигурда");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();

        Scanner scanner = new Scanner(inTest);

        System.out.println(scanner.nextLine());
        System.out.println(scanner.nextLine());
        System.out.println(scanner.nextLine());
        System.out.println(scanner.hasNextLine());
    }

    @Test
    public void test() throws IOException, InterruptedException {
        PipedOutputStream outThread = new PipedOutputStream();
        PipedInputStream inTest = new PipedInputStream(outThread, 1024 * 16);

        PipedOutputStream outTest = new PipedOutputStream();
        PipedInputStream inThread = new PipedInputStream(outTest, 1024 * 16);

        Thread thread = new Thread(() -> {
            PrintStream outThreadWriter = new PrintStream(outThread);
            Scanner inThreadScanner = new Scanner(inThread);

            outThreadWriter.println("Hello!");

            String onHello = inThreadScanner.nextLine();

            outThreadWriter.println("Bye! " + onHello);

            String onBye = inThreadScanner.nextLine();

            outThreadWriter.println("Zbs! " + onBye);
        }, "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        thread.start();

        PrintStream outTestWriter = new PrintStream(outTest);
        Scanner inTestScanner = new Scanner(inTest);

        String hello = inTestScanner.nextLine();
        Assert.assertEquals("Hello!", hello);

        outTestWriter.println("Zhopa!");

        String byeZhopa = inTestScanner.nextLine();
        Assert.assertEquals("Bye! Zhopa!", byeZhopa);

        outTestWriter.println("Xxx!");

        String zbs = inTestScanner.nextLine();
        Assert.assertEquals("Zbs! Xxx!", zbs);
    }
}
