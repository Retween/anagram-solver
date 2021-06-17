package com.siberteam.edu.zernest.asolver.process;

import com.siberteam.edu.zernest.asolver.interfaces.ILogger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WordsToAnagramHandlerConsumer implements Runnable, ILogger {
    private final BlockingQueue<String> words;
    private final Map<String, Set<String>> anagramsMap;
    private final CountDownLatch consumersLatch;

    public WordsToAnagramHandlerConsumer(BlockingQueue<String> words,
                                         Map<String, Set<String>> anagramsMap,
                                         CountDownLatch consumersLatch) {
        this.words = words;
        this.anagramsMap = anagramsMap;
        this.consumersLatch = consumersLatch;
    }

    @Override
    public void run() {
        try {
            String word;
            while ((word = words.poll(5, TimeUnit.SECONDS)) != null) {
                String alphabeticalWord = getAlphabeticalString(word);
                anagramsMap.computeIfAbsent(alphabeticalWord, k ->
                        new HashSet<>()).add(word);
            }

        } catch (InterruptedException e) {
            log("Thread was interrupted " + "\nException: " + e);
        } finally {
            log("Finised CConsumer");
            consumersLatch.countDown();
        }
    }

    private String getAlphabeticalString(String string) {
        char[] chars = string.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    @Override
    public String toString() {
        return "WordsToAnagramConsumer" + "[" +
                "words=" + words.size() +
                ", anagramsMap=" + anagramsMap.size() +
                ", consumersLatch=" + consumersLatch +
                ']';
    }
}
