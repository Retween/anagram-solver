import com.siberteam.edu.zernest.asolver.process.WordsToAnagramHandlerConsumer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class AnagramFinderTest {
    private Map<String, Set<String>> expectedAnagrams;
    private Map<String, Set<String>> actualAnagrams;
    private WordsToAnagramHandlerConsumer consumer;
    public final static String POISON = "#";


    @Before
    public void setUp() {
        expectedAnagrams = new HashMap<>();
        actualAnagrams = new HashMap<>();
        expectedAnagrams.put("кот", new HashSet<>());
        Set<String> value = expectedAnagrams.get("кот");
        value.add("кот");
        value.add("ток");
        value.add("окт");
        value.add("кто");

        BlockingQueue<String> words = new ArrayBlockingQueue<>(10);
        words.add("кот");
        words.add("ток");
        words.add("окт");
        words.add("кто");
        words.add("угги");
        words.add("коотт");
        words.add("привет");
        words.add("кресло");
        words.add("world");
        words.add(POISON);

        consumer = new WordsToAnagramHandlerConsumer(words, actualAnagrams,
                new CountDownLatch(1));
    }

    @Test
    public void testAnagramFinder() {
        consumer.run();
        Assert.assertEquals(expectedAnagrams.get("кот"),
                actualAnagrams.get("кот"));
    }
}
