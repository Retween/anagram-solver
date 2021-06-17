package com.siberteam.edu.zernest.asolver.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputStreamToQueueReader {
    private final Queue<String> queue;
    private final InputStream inputStream;

    public InputStreamToQueueReader(InputStream inputStream) {
        this.inputStream = inputStream;
        queue = new ConcurrentLinkedQueue<>();
    }

    public Queue<String> getUrlQueue() throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(inputStream))) {
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                queue.add(inputLine);
            }
        }

        return queue;
    }

    @Override
    public String toString() {
        return "InputStreamToQueueReader" + "[" +
                "queueSize=" + queue.size() +
                ", inputStream=" + inputStream +
                ']';
    }
}
