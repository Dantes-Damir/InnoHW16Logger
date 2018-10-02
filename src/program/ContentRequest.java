package program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class ContentRequest implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ContentRequest.class.getName());
    private static String[] words;
    private static BlockingQueue<String> queue;
    private String source;

    public ContentRequest(String source, String[] words, BlockingQueue<String> queueInstance) {
        this.source = source;
        this.words = words;
        queue = queueInstance;
    }

    private static void containsWord(String sentence) throws InterruptedException {
        for (String word : words) {
            if (sentence.toLowerCase().contains(word.toLowerCase())) {
                queue.put(sentence);
                break;
            }
        }
    }

    @Override
    public void run() {
        URL url = getUrl();
        if (url != null) {
            try (InputStream inputStream = url.openStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));) {

                StringBuilder sentenceBuffer = new StringBuilder();
                int simbol = 0;
                while ((simbol = reader.read()) != -1) {
                    char simbolChar = (char) simbol;
                    if (simbolChar == '.' || simbolChar == '!' || simbolChar == '?') {
                        containsWord(sentenceBuffer.toString());
                        sentenceBuffer = new StringBuilder();
                    } else {
                        sentenceBuffer.append(simbolChar);
                    }
                }
            } catch (IOException | InterruptedException e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }

    private URL getUrl() {
        URL url = null;
        try {
            url = new URL(this.source);
            LOGGER.info("URL created: " + url.toString());

        } catch (MalformedURLException e) {
            LOGGER.warning(e.getMessage());
        }
        return url;
    }
}
