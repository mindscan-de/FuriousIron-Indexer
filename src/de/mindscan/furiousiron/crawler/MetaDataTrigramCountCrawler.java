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
package de.mindscan.furiousiron.crawler;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import de.mindscan.furiousiron.index.trigram.InverseMetadataTrigramIndex;

/**
 * 
 */
public class MetaDataTrigramCountCrawler {

    public void crawl( Consumer<Path> pathCollector, Path crawlFolder ) {
        FileVisitor<Path> trigramCountVisitor = new TrigramCountFileVisitor<>( pathCollector );

        // only look into the metadata trigram index folder
        collectFiles( crawlFolder.resolve( InverseMetadataTrigramIndex.getLocalIndexFolder() ), trigramCountVisitor );
    }

    private void collectFiles( Path crawlFolder, FileVisitor<Path> trigramCountVisitor ) {
        try {
            Files.walkFileTree( crawlFolder, trigramCountVisitor );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
