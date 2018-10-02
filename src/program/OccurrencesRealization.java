package program;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class OccurrencesRealization implements OccurrencesContact {

    private static final int MAX_Treads = 20;
    private static final Logger LOGGER = Logger.getLogger(ContentRequest.class.getName());
    private ExecutorService pool;

    public OccurrencesRealization() {
        pool = Executors.newFixedThreadPool(MAX_Treads);
    }

    @Override
    public void getOccurrences(String[] sources, String[] words, String res) throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        Thread fileWriter = new Thread(() -> {
            try (PrintWriter writer = new PrintWriter(new File(res))) {
                while (true) {
                    writer.println(queue.take());
                }
            } catch (IOException e) {
                LOGGER.warning(e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("Программа завершила работу");
            }
        });
        fileWriter.start();
        for (String source : sources) {
            pool.execute(new ContentRequest(source, words, queue));
        }
        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        fileWriter.interrupt();
    }
}
