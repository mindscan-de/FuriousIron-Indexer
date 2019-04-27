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
package de.mindscan.furiousiron.index.cache;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.Gson;

import de.mindscan.furiousiron.document.DocumentId;

/**
 * 
 */
public class WordlistCache {

    /**
     * folder in index Folder, where the worlists and trigrams of the documents should be cached. 
     */
    private static final String CACHED_WORDLISTS_FOLDER = "cachedWordlists";

    /**
     * file suffix for files containing the worlist of the original document 
     */
    public final static String WORDLIST_FILE_SUFFIX = ".wordlist";

    /**
     * file suffix for files containing the trigrams of the original document 
     */
    public final static String TRIGRAMS_FILE_SUFFIX = ".trigrams";

    private Path cacheFolder;

    /**
     * @param indexFolder
     */
    public WordlistCache( Path indexFolder ) {
        this.cacheFolder = indexFolder.resolve( CACHED_WORDLISTS_FOLDER );
    }

    /**
     * @param documentId
     * @param uniqueWordlist
     */
    public void addUniqueWordlist( DocumentId documentId, List<String> uniqueWordlist ) {
        // FIXME: duplication of DocumentCache ... composition / inheritance / extraction ?
        createTargetDirectoryIfNotExist( documentId );

        Path wordlistFilePath = getTargetDirectoryPath( documentId ).resolve( documentId.getMD5hex() + WORDLIST_FILE_SUFFIX );

        try (BufferedWriter writer = Files.newBufferedWriter( wordlistFilePath, Charset.forName( "UTF-8" ) )) {
            Gson gson = new Gson();
            writer.write( gson.toJson( uniqueWordlist ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param documentId
     * @param uniqueTrigramlist
     */
    public void addUniqueTrigrams( DocumentId documentId, Set<String> uniqueTrigramlist ) {
        // FIXME: duplication of DocumentCache ... composition / inheritance / extraction ?
        createTargetDirectoryIfNotExist( documentId );

        Path wordlistFilePath = getTargetDirectoryPath( documentId ).resolve( documentId.getMD5hex() + TRIGRAMS_FILE_SUFFIX );

        try (BufferedWriter writer = Files.newBufferedWriter( wordlistFilePath, Charset.forName( "UTF-8" ) )) {
            Gson gson = new Gson();
            writer.write( gson.toJson( new TreeSet<>( uniqueTrigramlist ) ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    // FIXME: duplication of DocumentCache ... composition / inheritance / exraction ?
    private void createTargetDirectoryIfNotExist( DocumentId documentId ) {
        Path targetDirectoryPath = getTargetDirectoryPath( documentId );
        if (!Files.isDirectory( targetDirectoryPath )) {
            try {
                Files.createDirectories( targetDirectoryPath );
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // FIXME: duplication of DocumentCache ... composition / inheritance / exraction ?
    private Path getTargetDirectoryPath( DocumentId documentId ) {
        String firstLayer = documentId.getMD5hex().substring( 0, 2 );

        // TODO: if we are exceeding 4k files per directory (means 1 mio files in total to index, we should enable the second layer. 
        // String secondLayer = documentId.getMd5hex().substring( 2, 4 );

        return cacheFolder.resolve( firstLayer ); //.resolve(secondLayer)
    }

}