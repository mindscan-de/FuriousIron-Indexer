/**
 * 
 * MIT License
 *
 * Copyright (c) 2021 Maxim Gansert, Mindscan
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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

/**
 * 
 */
public class SearchQueryCache {

    /**
     * folder in index Folder, where the SearchQueries and the document ids should be cached. 
     */
    private static final String CACHED_QUERY_FOLDER = "cachedQueries";

    /**
     * file suffix for files containing the queryCache 
     */
    public final static String CACHE_FILE_SUFFIX = ".querycache";

    private Path cacheFolder;

    /**
     * 
     */
    public SearchQueryCache( Path indexFolder ) {
        this.cacheFolder = indexFolder.resolve( CACHED_QUERY_FOLDER );
    }

    public boolean isQueryResultAvailable( String queryKeyId ) {
        Path searchQueryDocumentPath = CachingPathUtils.getDocumentPathFromMD5( cacheFolder, queryKeyId, CACHE_FILE_SUFFIX );

        return Files.exists( searchQueryDocumentPath );
    }

    public List<String> loadQueryResult( String queryKeyId ) {
        Path searchQueryDocumentPath = CachingPathUtils.getDocumentPathFromMD5( cacheFolder, queryKeyId, CACHE_FILE_SUFFIX );

        try (BufferedReader jsonBufferedReader = Files.newBufferedReader( searchQueryDocumentPath, StandardCharsets.UTF_8 )) {
            Gson gson = new Gson();
            String[] result = gson.fromJson( jsonBufferedReader, String[].class );
            return Arrays.asList( result );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public void saveQueryResult( String queryKeyId, Collection<String> documentIds ) {
        Path searchQueryDocumentPath = CachingPathUtils.getDocumentPathFromMD5( cacheFolder, queryKeyId, CACHE_FILE_SUFFIX );

        CachingPathUtils.createTargetDirectoryIfNotExist( searchQueryDocumentPath );

        try (BufferedWriter writer = Files.newBufferedWriter( searchQueryDocumentPath, StandardCharsets.UTF_8 )) {
            Gson gson = new Gson();
            writer.write( gson.toJson( new LinkedList<>( documentIds ) ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
