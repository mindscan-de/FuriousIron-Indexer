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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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

    /**
     * file suffix for files containing the count of the trigrams of the original document 
     */
    public final static String TRIGRAMSTERMFREQUENCY_FILE_SUFFIX = ".ttfcount";

    private Path cacheFolder;

    /**
     * @param indexFolder
     */
    public WordlistCache( Path indexFolder ) {
        this.cacheFolder = indexFolder.resolve( CACHED_WORDLISTS_FOLDER );
    }

    public void addUniqueWordlist( DocumentId documentId, List<String> uniqueWordlist ) {
        Path wordlistDocumentPath = CachingPathUtils.getDocumentPath( cacheFolder, documentId, WORDLIST_FILE_SUFFIX );

        CachingPathUtils.createTargetDirectoryIfNotExist( wordlistDocumentPath );

        try (BufferedWriter writer = Files.newBufferedWriter( wordlistDocumentPath, StandardCharsets.UTF_8 )) {
            Gson gson = new Gson();
            writer.write( gson.toJson( uniqueWordlist ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> loadWordList( String documentId ) {
        Path wordlistDocumentPath = CachingPathUtils.getDocumentPathFromMD5( cacheFolder, documentId, WORDLIST_FILE_SUFFIX );

        try (BufferedReader jsonBufferedReader = Files.newBufferedReader( wordlistDocumentPath, StandardCharsets.UTF_8 )) {
            Gson gson = new Gson();
            String[] result = gson.fromJson( jsonBufferedReader, String[].class );
            return Arrays.asList( result );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    /**
     * @param documentId
     * @param uniqueTrigramlist
     */
    public void addUniqueTrigrams( DocumentId documentId, Set<String> uniqueTrigramlist ) {
        Path trigramsDocumentPath = CachingPathUtils.getDocumentPath( cacheFolder, documentId, TRIGRAMS_FILE_SUFFIX );

        CachingPathUtils.createTargetDirectoryIfNotExist( trigramsDocumentPath );

        try (BufferedWriter writer = Files.newBufferedWriter( trigramsDocumentPath, StandardCharsets.UTF_8 )) {
            Gson gson = new Gson();
            writer.write( gson.toJson( new TreeSet<>( uniqueTrigramlist ) ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param documentId
     * @param ttfList
     */
    public void addTTFList( DocumentId documentId, Map<String, Integer> ttfList ) {
        Path trigramsDocumentPath = CachingPathUtils.getDocumentPath( cacheFolder, documentId, TRIGRAMSTERMFREQUENCY_FILE_SUFFIX );

        CachingPathUtils.createTargetDirectoryIfNotExist( trigramsDocumentPath );

        try (BufferedWriter writer = Files.newBufferedWriter( trigramsDocumentPath, StandardCharsets.UTF_8 )) {
            Gson gson = new Gson();
            writer.write( gson.toJson( new TreeMap<>( ttfList ) ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> loadTTFData( String documentId ) {
        Path wordlistDocumentPath = CachingPathUtils.getDocumentPathFromMD5( cacheFolder, documentId, TRIGRAMSTERMFREQUENCY_FILE_SUFFIX );

        try (BufferedReader jsonBufferedReader = Files.newBufferedReader( wordlistDocumentPath, StandardCharsets.UTF_8 )) {
            Gson gson = new Gson();
            @SuppressWarnings( "unchecked" )
            Map<String, Integer> result = gson.fromJson( jsonBufferedReader, TreeMap.class );
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyMap();
    }

}
