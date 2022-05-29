package de.mindscan.furiousiron.classifier;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class EncodingClassifierTest {

    @Test
    public void testClassify() throws Exception {
        // arrange
        EncodingClassifier classifier = new EncodingClassifier();

        classifier.classify( Paths.get(
                        "D:\\Analysis\\CrawlerProjects\\NonGPL\\ElasticSearch_elasticsearch-master\\elasticsearch-master\\client\\rest-high-level\\src\\main\\java\\org\\elasticsearch\\client\\SyncedFlushResponse.java" ),
                        "d:\\DoubleArray1.png" );

        classifier.classify( Paths.get(
                        "D:\\Analysis\\CrawlerProjects\\NonGPL\\gncloud_fastcatsearch-master\\fastcatsearch-master\\core\\src\\main\\java\\org\\tartarus\\snowball\\SnowballProgram.java" ),
                        "d:\\DoubleArray2.png" );

        // act

        // assert

    }

}
