/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.furiousiron.indexer.main;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

import de.mindscan.furiousiron.crawler.MetaDataFileCrawler;
import de.mindscan.furiousiron.indexer.MetadataFileIndexer;
import picocli.CommandLine;

/**
 * 
 */
public class MetaIndexerMain {

    /**
     * @param crawlFolder
     * @param indexFolder
     */
    public void run( Path crawlFolder, Path indexFolder ) {
        Deque<Path> filesToBeIndexed = new ArrayDeque<Path>();

        MetaDataFileCrawler metaIndexCrawler = new MetaDataFileCrawler();
        metaIndexCrawler.crawl( filesToBeIndexed::add, crawlFolder );

        System.out.println( String.format( "%d files found for meta data indexing.", filesToBeIndexed.size() ) );

        MetadataFileIndexer metadataIndexer = new MetadataFileIndexer();
        metadataIndexer.buildIndex( filesToBeIndexed, crawlFolder, indexFolder );

    }

    public static void main( String[] args ) {
        int exitCode = new CommandLine( new MetaIndexerMainParameters() ).execute( args );
        System.exit( exitCode );
    }

}
