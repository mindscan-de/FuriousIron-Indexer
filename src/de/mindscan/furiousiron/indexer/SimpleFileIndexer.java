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
package de.mindscan.furiousiron.indexer;

import java.nio.file.Path;
import java.util.Deque;

/**
 * 
 */
public class SimpleFileIndexer {

    /**
     * @param filesToBeIndexed provider for files which should be indexed
     * @param crawlFolder the base folder, of what files to index
     * @param indexFolder the index folder, where to put the index
     */
    public void buildIndex( Deque<Path> filesToBeIndexed, Path crawlFolder, Path indexFolder ) {
        for (Path fileToIndex : filesToBeIndexed) {
            try {
                buildIndexForSingleFile( fileToIndex, crawlFolder, indexFolder );
            }
            catch (Exception ignore) {
                // intentionally left blank yet.
                // File should be appended again, if temporary problem or scheduled again for later indexing operation. 
            }
        }
    }

    /**
     * Index/Process one File
     * 
     * @param fileToIndex
     * @param crawlFolder
     * @param indexFolder
     */
    private void buildIndexForSingleFile( Path fileToIndex, Path crawlFolder, Path indexFolder ) {
        System.out.println( fileToIndex );

        // Execute pipeline, according to filecontent ...
        // Lowercase
        // Stopwords
        // trigram_filter
        // porter stemming

        // some ideas to come

        // copy in den cache / text only / ... / metadata / metainfo /  
    }

}
