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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 
 */
public class Cache {

    /**
     * file suffix for files containing copies of the original content in the cache folder
     */
    public final static String CACHED_FILE_SUFFIX = ".originalContent";

    /**
     * file suffix for files containing unique word list of the document in the cache folder
     */
    public final static String WORDLIST_FILE_SUFFIX = ".wordlist";

    // private Path indexFolder;
    private Path cacheFolder;

    public Cache( Path indexFolder ) {
        // this.indexFolder = indexFolder;
        this.cacheFolder = indexFolder.resolve( "cachedDocuments" );
    }

    public void createDocumentCopy( DocumentId documentId, Path fileToIndex ) {
        String firstLayer = documentId.getMd5hex().substring( 0, 2 );
        String secondLayer = documentId.getMd5hex().substring( 2, 4 );

        Path targetPath = getTargetPath( firstLayer, secondLayer );
        if (!Files.isDirectory( targetPath )) {
            try {
                Files.createDirectories( targetPath );
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        Path targetFile = targetPath.resolve( documentId.getMd5hex() + CACHED_FILE_SUFFIX );
        try {
            Files.copy( fileToIndex, targetFile, StandardCopyOption.REPLACE_EXISTING );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path getTargetPath( String firstLayer, String secondLayer ) {
        return cacheFolder.resolve( firstLayer ); // .resolve( secondLayer );
    }

}