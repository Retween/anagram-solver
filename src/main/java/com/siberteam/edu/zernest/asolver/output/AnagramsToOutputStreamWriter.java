package com.siberteam.edu.zernest.asolver.output;

import java.io.*;
import java.util.*;

public class AnagramsToOutputStreamWriter {
    private final OutputStream outputStream;

    public AnagramsToOutputStreamWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeListToFile(Map<String, Set<String>> anagramsMap)
            throws IOException {
        List<Set<String>> anagramsList = trimMapToList(anagramsMap);

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            for (Set<String> stringSet : anagramsList) {
                Iterator<String> value = stringSet.iterator();
                bw.write(value.next() + ": ");
                while (value.hasNext()) {
                    bw.write(value.next() + ", ");
                }
                bw.write("\n");
            }
        }
    }

    private List<Set<String>> trimMapToList(Map<String, Set<String>> map) {
        Map<String, Set<String>> trimmedMap = new HashMap<>(map);
        trimmedMap.entrySet().removeIf(entry -> entry.getValue().size() <= 1);
        return new ArrayList<>(trimmedMap.values());
    }

    @Override
    public String toString() {
        return "MapToOutputStreamWriter" + "[" +
                "outputStream=" + outputStream +
                ']';
    }
}
