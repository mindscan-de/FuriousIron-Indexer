package de.mindscan.furiousiron.indexer.main;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class IndexerMainTest {

    @Test
    public void testRun_simpleCtorAndJustRunning_expectNoException() {
        // Arrange
        IndexerMain indexerMain = new IndexerMain();

        Path crawlFolder = Paths.get( "D:\\Analysis\\CrawlerProjects", "NonGPL" );
        Path indexFolder = Paths.get( "D:\\Analysis\\CrawlerProjects", "Indexed" );

        // Act
        indexerMain.run( crawlFolder, indexFolder );

        // Assert
        // intentionally left blank.
    }

}
