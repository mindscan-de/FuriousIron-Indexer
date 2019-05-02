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
package de.mindscan.furiousiron.search;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import de.mindscan.furiousiron.index.cache.DocumentCache;
import de.mindscan.furiousiron.index.cache.MetadataCache;
import de.mindscan.furiousiron.index.cache.WordlistCache;
import de.mindscan.furiousiron.index.trigram.InverseTrigramIndex;
import de.mindscan.furiousiron.indexer.SimpleWordUtils;

/**
 * 
 */
public class Search {

    // for content
    private DocumentCache theFileCache;
    // for ranking
    private MetadataCache theMetadataCache;
    // for ranking the results
    private WordlistCache theWordlistCache;
    // for ranking
    private InverseTrigramIndex theInverseTrigramIndex;

    /**
     * @param indexFolder The folder, where the index root is located
     * 
     */
    public Search( Path indexFolder ) {
        theFileCache = new DocumentCache( indexFolder );
        theMetadataCache = new MetadataCache( indexFolder );
        theWordlistCache = new WordlistCache( indexFolder );
        theInverseTrigramIndex = new InverseTrigramIndex( indexFolder );
    }

    /**
     * @param searchterm the searchterm to search for in the index.
     * @return the collection of documents
     */
    public Collection<SearchResultCandidates> search( String searchterm ) {
        // assume current search term is exactly one word.
        Collection<String> uniqueTrigramsFromWord = SimpleWordUtils.getUniqueTrigramsFromWord( searchterm );

        searchTrigrams( uniqueTrigramsFromWord );

        // 
        return Collections.emptyList();
    }

    /**
     * @param uniqueTrigramsFromWord
     */
    private void searchTrigrams( Collection<String> uniqueTrigramsFromWord ) {
        // TODO: count occurences for each trigram... by documentID
        Map<String, AtomicInteger> documentIdCount = new HashMap<>();

        for (String trigram : uniqueTrigramsFromWord) {
            Collection<String> documentIds = theInverseTrigramIndex.getDocumentIdsForTrigram( trigram );

            documentIds.stream().forEach( docId -> documentIdCount.computeIfAbsent( docId, x -> new AtomicInteger( 0 ) ).incrementAndGet() );
        }

        System.out.println( documentIdCount );
    }
}
