package com.siberteam.edu.zernest.asolver.process;

import com.siberteam.edu.zernest.asolver.interfaces.ILogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class UrlToWordsParserProducer implements Runnable, ILogger {
    private final Queue<String> process;
    private final BlockingQueue<String> words;
    private final Set<String> dictionary;
    private final CountDownLatch producersLatch;

    public UrlToWordsParserProducer(Queue<String> process,
                                    BlockingQueue<String> words,
                                    Set<String> dictionary,
                                    CountDownLatch producersLatch) {
        this.process = process;
        this.words = words;
        this.dictionary = dictionary;
        this.producersLatch = producersLatch;
    }

    @Override
    public void run() {
        String urlFilePath = null;
        try {
            while ((urlFilePath = process.poll()) != null) {
                URL url = new URL(urlFilePath);
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(url.openStream()))) {
                    String inputString;
                    while ((inputString = br.readLine()) != null) {
                        parseString(inputString);
                    }
                }
            }
        } catch (IOException e) {
            log("Invalid URL: " + urlFilePath);
        } catch (InterruptedException e) {
            log("Thread was interrupted " + "\tException: " + e);
        } finally {
            producersLatch.countDown();
        }
    }

    private void parseString(String inputString) throws InterruptedException {
        if (!inputString.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(inputString);
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken().toLowerCase();
                if (word.matches("[А-Яа-яЁё]{3,}")
                        && !dictionary.contains(word)) {
                    dictionary.add(word);
                    words.put(word);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "UrlWordsReaderProducer" + "[" +
                "process=" + process.size() +
                ", words=" + words.size() +
                ", dictionary=" + dictionary.size() +
                ']';
    }
}
