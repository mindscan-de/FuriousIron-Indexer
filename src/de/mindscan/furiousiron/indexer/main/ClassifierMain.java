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

/**
 * First step is to build the inverse content document index. 
 * 
 * After Building the first inverse index, we want to run classifiers and 
 * and analytics on the indexed files. The results will be added to the 
 * MetaData of the indexed file. This is intended to be a separate step.
 * 
 * The next step is to Build an index over the metadata.
 */
public class ClassifierMain {

    void run( Path crawlFolder, Path indexFolder ) {
        // use a file crawler to find all metadata
        // for each found metadata file, we want to load the current stage and read its content
        // according to the content we want to apply different classifiers and analyzers
        // then we want to update the metadata for this file
        // then we want to save the updated metadata file.

        // actually we want to employ a strategy, which we can either invoke on the first indexing, 
        // or do a second pass after we first indexed everything.

    }

    public static void main( String[] args ) {

        long starttime = System.nanoTime();
        int exitCode = 0;
        long endTime = System.nanoTime();

        long deltaTime = endTime - starttime;

        long seconds = deltaTime / 1000000000L;
        long nanoseconds = deltaTime % 1000000000L;

        System.out.println( String.format( "Classfication took %d.%09d seconds.", seconds, nanoseconds ) );

        System.exit( exitCode );

    }

}
