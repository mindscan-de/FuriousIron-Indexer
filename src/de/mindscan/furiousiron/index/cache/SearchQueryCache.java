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
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 */
public class SearchQueryCache extends DiskBasedCache {

    /**
     * folder in index Folder, where the SearchQueries and the document ids should be cached. 
     */
    private static final String CACHED_QUERY_FOLDER = "cachedQueries";

    /**
     * file suffix for files containing the queryCache 
     */
    public final static String CACHE_FILE_SUFFIX = ".querycache";

    /** 
     * file suffix for files containing the latest preview data 
     */
    public final static String CACHE_PREVIEW_SUFFIX = ".previewcache";

    /**
     * 
     */
    public SearchQueryCache( Path indexFolder ) {
        super( indexFolder.resolve( CACHED_QUERY_FOLDER ) );
    }

    public boolean isQueryResultAvailable( String queryKey ) {
        Path searchQueryDocumentPath = buildCacheTargetPathFromKey( queryKey, CACHE_FILE_SUFFIX );

        return Files.exists( searchQueryDocumentPath );
    }

    public boolean isPreviewAvailable( String queryKey ) {
        Path searchQueryDocumentPath = buildCacheTargetPathFromKey( queryKey, CACHE_PREVIEW_SUFFIX );

        return Files.exists( searchQueryDocumentPath );
    }

    public List<String> loadQueryResult( String queryKey ) {
        Path searchQueryDocumentPath = buildCacheTargetPathFromKey( queryKey, CACHE_FILE_SUFFIX );

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

    public Map<String, Map<Integer, String>> loadPreviewResult( String queryKey ) {
        Path searchQueryDocumentPath = buildCacheTargetPathFromKey( queryKey, CACHE_PREVIEW_SUFFIX );

        Type type = new TypeToken<TreeMap<String, TreeMap<Integer, String>>>() {
        }.getType();

        try (BufferedReader jsonBufferedReader = Files.newBufferedReader( searchQueryDocumentPath, StandardCharsets.UTF_8 )) {
            Gson gson = new Gson();
            @SuppressWarnings( "unchecked" )
            Map<String, Map<Integer, String>> result = (Map<String, Map<Integer, String>>) gson.fromJson( jsonBufferedReader, type );
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyMap();
    }

    public void saveQueryResult( String queryKey, Collection<String> documentIds ) {
        Path searchQueryDocumentPath = buildCacheTargetPathFromKey( queryKey, CACHE_FILE_SUFFIX );

        createCacheTargetPath( searchQueryDocumentPath );

        try (BufferedWriter writer = Files.newBufferedWriter( searchQueryDocumentPath, StandardCharsets.UTF_8 )) {
            Gson gson = new Gson();
            writer.write( gson.toJson( new LinkedList<>( documentIds ) ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePreviewResult( String queryKey, Map<String, Map<Integer, String>> previewData ) {
        Path searchQueryDocumentPath = buildCacheTargetPathFromKey( queryKey, CACHE_PREVIEW_SUFFIX );

        createCacheTargetPath( searchQueryDocumentPath );

        try (BufferedWriter writer = Files.newBufferedWriter( searchQueryDocumentPath, StandardCharsets.UTF_8 )) {
            Gson gson = new Gson();
            writer.write( gson.toJson( previewData ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveQuery( String qkey, String prettyPrintedQuery ) {
        // TODO Auto-generated method stub
        // TODO: save the search query to learn something about the queries, so that these can be
        //       pre-cached after creating a new index.

    }

}
