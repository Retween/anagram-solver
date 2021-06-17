package com.siberteam.edu.zernest.asolver.process;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.*;

public class ProducerConsumerStarter {
    private final ExecutorService producersExecutor;
    private final ExecutorService consumersExecutor;
    private final int producersCount;
    private final int consumersCount;
    private final Queue<String> process;
    private final BlockingQueue<String> words;
    private final Set<String> dictionary;
    private final Map<String, Set<String>> anagramsMap;
    private final CountDownLatch consumersLatch;

    public ProducerConsumerStarter(Queue<String> process, int producersCount,
                                   int consumersCount) {
        this.process = process;
        this.producersCount = producersCount;
        this.consumersCount = consumersCount;
        producersExecutor = Executors.newFixedThreadPool(producersCount);
        consumersExecutor = Executors.newFixedThreadPool(consumersCount);
        words = new ArrayBlockingQueue<>(500);
        dictionary = Sets.newConcurrentHashSet();
        anagramsMap = new ConcurrentHashMap<>();
        consumersLatch = new CountDownLatch(consumersCount);
    }

    public void start() throws InterruptedException {
        for (int i = 0; i < producersCount; i++) {
            producersExecutor.execute(new UrlToWordsParserProducer(process,
                    words, dictionary));
        }
        producersExecutor.shutdown();

        for (int i = 0; i < consumersCount; i++) {
            consumersExecutor.execute(new WordsToAnagramHandlerConsumer(words,
                    anagramsMap, consumersLatch));
        }
        consumersExecutor.shutdown();

        consumersLatch.await();
    }

    public List<Set<String>> getAnagramsMap() {
        anagramsMap.entrySet().removeIf(entry -> entry.getValue().size() <= 1);
        return new ArrayList<>(anagramsMap.values());
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
