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
 * This is basically Step 4 of building a comprehensive search index.
 * 
 * This builds the data structures and stores these data structures, for
 * a very efficient candidate filter mechanism.
 * 
 * 1. IndexerMain - creates a cache structure and the inverse index for the content 
 * 2. ClassifierMain - updates the metadata
 * 3. MetaIndexerMain - creates an inverse index on the metadata
 * 4. HFBCompilerMain - compiles the inverse metadata index to HFB-Filters
 * 
 */
public class HFBCompilerMain {

    /**
     * @param crawlFolder
     * @param indexFolder
     */
    public void run( Path crawlFolder, Path indexFolder ) {
        // TODO Auto-generated method stub

    }

}