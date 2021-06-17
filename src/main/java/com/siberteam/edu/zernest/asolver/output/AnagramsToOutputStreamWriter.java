package com.siberteam.edu.zernest.asolver.output;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AnagramsToOutputStreamWriter {
    private final OutputStream outputStream;

    public AnagramsToOutputStreamWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeListToFile(List<Set<String>> list)
            throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                outputStream))) {
            for (Set<String> stringSet : list) {
                Iterator<String> value = stringSet.iterator();
                bw.write(value.next() + ": ");
                while (value.hasNext()) {
                    bw.write(value.next() + ", ");
                }
                bw.write("\n");
            }
        }

    }

    @Override
    public String toString() {
        return "MapToOutputStreamWriter" + "[" +
                "outputStream=" + outputStream +
                ']';
    }
}
