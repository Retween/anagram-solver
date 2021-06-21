import com.siberteam.edu.zernest.asolver.input.InputStreamToQueueReader;
import com.siberteam.edu.zernest.asolver.process.UrlToWordsParserProducer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class ParserTest {
    private BlockingQueue<String> expectedQueue;
    private BlockingQueue<String> actualQueue;
    private UrlToWordsParserProducer parserProducer;

    @Before
    public void setUp() throws IOException {
        expectedQueue = new ArrayBlockingQueue<>(10);
        expectedQueue.add("когда");
        expectedQueue.add("привет");
        expectedQueue.add("крым");

        InputStream testFile = new FileInputStream("src/test/data/testURL.txt");
        Queue<String> process = new InputStreamToQueueReader(testFile)
                .getUrlQueue();
        actualQueue = new ArrayBlockingQueue<>(10);
        Set<String> set = new HashSet<>();

        parserProducer = new UrlToWordsParserProducer(process, actualQueue, set,
                new CountDownLatch(1));
    }

    @Test
    public void testParser() {
        parserProducer.run();
        Assert.assertArrayEquals(expectedQueue.toArray(), actualQueue.toArray());
    }
}
