/**
 * 
 * MIT License
 *
 * Copyright (c) 2019 Maxim Gansert, Mindscan
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
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;

import de.mindscan.furiousiron.crawler.SimpleFileCrawler;

/**
 * This is a very basic / simple Indexer for source code.
 * 
 * Requirements:
 * - Source is accessible via Path.
 * 
 */
public class IndexerMain {

    void run( Path crawlFolder, Path indexFolder ) {
        Deque<Path> filesToBeIndexed = new ArrayDeque<Path>();

        SimpleFileCrawler crawler = new SimpleFileCrawler();
        crawler.crawl( filesToBeIndexed::add, crawlFolder );

        System.out.println( String.format( "%d files found for indexing.", filesToBeIndexed.size() ) );

        // SimpleFileIndexer indexer = new SimpleFileIndexer();
        // indexer.buildIndex( filesToBeIndexed, crawlFolder, indexFolder );

    }

    public static void main( String[] args ) {
        if (args.length < 2) {
            return;
        }

        Path crawlFolder = Paths.get( args[0] );
        Path indexFolder = Paths.get( args[1] );

        if (!crawlFolder.toFile().exists()) {
            return;
        }

        if (!indexFolder.toFile().exists()) {
            return;
        }

        new IndexerMain().run( crawlFolder, indexFolder );
    }

}
