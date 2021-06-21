package com.siberteam.edu.zernest.asolver.process;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.*;

public class ProducerConsumerStarter {
    public final static String POISON = "#";
    private final ExecutorService producersExecutor;
    private final ExecutorService consumersExecutor;
    private final int producersCount;
    private final int consumersCount;
    private final CountDownLatch producersLatch;
    private final CountDownLatch consumersLatch;
    private final Queue<String> process;
    private final BlockingQueue<String> words;
    private final Set<String> dictionary;
    private final Map<String, Set<String>> anagramsMap;

    public ProducerConsumerStarter(Queue<String> process, int producersCount,
                                   int consumersCount) {
        this.process = process;
        this.producersCount = producersCount;
        this.consumersCount = consumersCount;
        producersExecutor = Executors.newFixedThreadPool(producersCount);
        consumersExecutor = Executors.newFixedThreadPool(consumersCount);
        words = new ArrayBlockingQueue<>(1000);
        dictionary = Sets.newConcurrentHashSet();
        anagramsMap = new ConcurrentHashMap<>();
        producersLatch = new CountDownLatch(producersCount);
        consumersLatch = new CountDownLatch(consumersCount);
    }

    public void startThreads() throws InterruptedException {
        for (int i = 0; i < producersCount; i++) {
            producersExecutor.execute(new UrlToWordsParserProducer(process, words, dictionary, producersLatch));
        }

        for (int i = 0; i < consumersCount; i++) {
            consumersExecutor.execute(new WordsToAnagramHandlerConsumer(words, anagramsMap, consumersLatch));
        }

        producersLatch.await();
        producersExecutor.shutdown();

        words.put(POISON);

        consumersLatch.await();
        consumersExecutor.shutdown();
    }

    public Map<String, Set<String>> getAnagramsMap() {
        return anagramsMap;
    }

    @Override
    public String toString() {
        return "ProducerConsumerStarter" + "[" +
                "producersExecutor=" + producersExecutor +
                ", consumersExecutor=" + consumersExecutor +
                ", producersCount=" + producersCount +
                ", consumersCount=" + consumersCount +
                ", process=" + process.size() +
                ", words=" + words.size() +
                ", dictionary=" + dictionary.size() +
                ", anagramsMap=" + anagramsMap.size() +
                ", consumersLatch=" + consumersLatch +
                ']';
    }
}
