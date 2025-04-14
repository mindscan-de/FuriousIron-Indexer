package de.mindscan.furiousiron.indexer.main;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import picocli.CommandLine.Option;

public class AllInOneIndexerMainParameters implements Callable<Integer> {

    @Option( names = "--crawlFolder", defaultValue = "D:\\Analysis\\CrawlerProjects\\NonGPL", description = "The folder to index." )
    private Path crawlFolder;

    @Option( names = "--indexFolder", defaultValue = "D:\\Analysis\\CrawlerProjects\\IndexedNew", description = "The folder where the index shall be stored." )
    private Path indexFolder;

    /** 
     * {@inheritDoc}
     */
    @Override
    public Integer call() throws Exception {
        AllInOneIndexerMain main = new AllInOneIndexerMain();
        main.run( crawlFolder, indexFolder );
        return 0;
    }

    /**
     * @return the crawlFolder
     */
    public Path getCrawlFolder() {
        return crawlFolder;
    }

    /**
     * @return the indexFolder
     */
    public Path getIndexFolder() {
        return indexFolder;
    }

}
