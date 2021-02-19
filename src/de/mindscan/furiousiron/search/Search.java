/**
 * 
 * MIT License
 *
 * Copyright (c) 2019, 2021 Maxim Gansert, Mindscan
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import de.mindscan.furiousiron.document.DocumentId;
import de.mindscan.furiousiron.index.cache.DocumentCache;
import de.mindscan.furiousiron.index.cache.MetadataCache;
import de.mindscan.furiousiron.index.cache.SearchQueryCache;
import de.mindscan.furiousiron.index.cache.WordlistCache;
import de.mindscan.furiousiron.index.trigram.SearchTrigramIndex;
import de.mindscan.furiousiron.index.trigram.TrigramOccurence;
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
    private SearchTrigramIndex theSearchTrigramIndex;
    // for performance
    private SearchQueryCache theSearchQueryCache;
    private List<String> unprocessedTrigrams;

    /**
     * @param indexFolder The folder, where the index root is located
     * 
     */
    public Search( Path indexFolder ) {
        theFileCache = new DocumentCache( indexFolder );
        theMetadataCache = new MetadataCache( indexFolder );
        theWordlistCache = new WordlistCache( indexFolder );
        theSearchTrigramIndex = new SearchTrigramIndex( indexFolder );
        theSearchQueryCache = new SearchQueryCache( indexFolder );
    }

    /**
     * @param searchterm the searchterm to search for in the index.
     * @return the collection of documents
     */
    public Collection<SearchResultCandidates> search( String searchterm ) {
        // assume current search term is exactly one word.
        String processedSearchTerm = searchterm.toLowerCase();

        // extract words from searchterm 
        Collection<String> uniqueTrigramsFromWord = SimpleWordUtils.getUniqueTrigramsFromWord( processedSearchTerm );

        Set<String> documentIdsForOneWord = collectDocumentIdsForTrigrams( uniqueTrigramsFromWord );

        // check, that a word is part of a page
        List<SearchResultCandidates> searchResult = new ArrayList<>();
        // convert these into a List of searchResultCandidate
        for (String documentId : documentIdsForOneWord) {
            SearchResultCandidates candidate = new SearchResultCandidates( documentId );
            candidate.loadFrom( theMetadataCache, theWordlistCache );
            if (candidate.containsWord( processedSearchTerm )) {
                searchResult.add( candidate );
            }
        }

        return searchResult;
    }

    /**
     * @param searchterm the searchterm to search for in the index.
     * @return a map of documents
     */
    public Map<String, SearchResultCandidates> searchToMap( String searchterm ) {
        // assume current search term is exactly one word.
        String processedSearchTerm = searchterm.toLowerCase();

        // extract words from searchterm 
        Collection<String> uniqueTrigramsFromWord = SimpleWordUtils.getUniqueTrigramsFromWord( processedSearchTerm );

        Set<String> documentIdsForOneWord = collectDocumentIdsForTrigrams( uniqueTrigramsFromWord );

        // check, that a word is part of a page
        Map<String, SearchResultCandidates> searchResult = new HashMap<>();
        // convert these into a List of searchResultCandidate
        for (String documentId : documentIdsForOneWord) {
            SearchResultCandidates candidate = new SearchResultCandidates( documentId );
            candidate.loadFrom( theMetadataCache, theWordlistCache );
            if (candidate.containsWord( processedSearchTerm )) {
                searchResult.put( documentId, candidate );
            }
        }

        return searchResult;
    }

    private Set<String> collectDocumentIdsForTrigrams( Collection<String> uniqueTrigramsFromWord ) {
        Map<String, AtomicInteger> documentIdCount = new HashMap<>();

        int maxSize = uniqueTrigramsFromWord.size();

        for (String trigram : uniqueTrigramsFromWord) {
            Collection<String> documentIds = theSearchTrigramIndex.getDocumentIdsForTrigram( trigram );

            documentIds.stream().forEach( docId -> documentIdCount.computeIfAbsent( docId, x -> new AtomicInteger( 0 ) ).incrementAndGet() );
        }

        return documentIdCount.entrySet().stream().filter( entry -> (entry.getValue().intValue() == maxSize) ).map( entry -> entry.getKey() )
                        .collect( Collectors.toSet() );
    }

    public Set<String> collectDocumentIdsForTrigramsOpt( Collection<String> uniqueTrigramsFromWord ) {
        HashSet<String> resultSet = new HashSet<String>();

        // convert trigrams to TrigramOccurences
        List<TrigramOccurence> collectedOccurences = uniqueTrigramsFromWord.stream().map( ( trigram ) -> this.getTrigramOccurence( trigram ) )
                        .collect( Collectors.toList() );

        // sort uniqueTrigramsFromWord by number of expected results in increasing order.
        collectedOccurences.sort( Comparator.<TrigramOccurence> comparingLong( occurence -> occurence.getOccurenceCount() ) );

        for (TrigramOccurence trigramOccurence : collectedOccurences) {
            System.out.println( "Debug-TrigramOccurence: " + trigramOccurence.toString() );
        }
        // fill with first document list (shortest), it can only get shorter at this time, 
        // so we don't need to reallocate like in the previous implementation, and adding 
        // even more atomic integers and do atomic increments... 

        long start = System.currentTimeMillis();

        Iterator<TrigramOccurence> collectedOccurencesIterator = collectedOccurences.iterator();
        if (collectedOccurencesIterator.hasNext()) {
            String firstTrigram = collectedOccurencesIterator.next().getTrigram();
            resultSet = new HashSet<String>( theSearchTrigramIndex.getDocumentIdsForTrigram( firstTrigram ) );
            System.out.println( "Reduction starts from: " + resultSet.size() + " for " + firstTrigram );
        }

        while (collectedOccurencesIterator.hasNext()) {
            TrigramOccurence trigram = collectedOccurencesIterator.next();

            // TODO if reject rate is too small compared to the number of documentIds, we might want to skip anyways.
            // attention at first search ...
            Collection<String> documentIds = theSearchTrigramIndex.getDocumentIdsForTrigram( trigram.getTrigram() );

            // sorting trigrams leads to a highly imbalanced (retain) comparison, 
            // resultSet will become smaller and smaller and the documentIds are becoming bigger and bigger.
            // so it might be important to optimize this implementation.

            // in case of majority of time consumed here, this retainAll operation has to be switched to a 
            // more efficient mode e.g. Skiplists or we are looking for each resultset-item via a bloomfilter
            // in documentIds-Collection, where the documentIds are the bloomfilter-hashed eleemnts.
            resultSet.retainAll( documentIds );
            System.out.println( "Reduction to: " + resultSet.size() + " using trigram: " + trigram.getTrigram() );

            if ((documentIds.size() >> 5) > resultSet.size()) {
                // stop if it is too imbalanced... we probably already are in X+10% range of maximal search results
                break;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println( "Time to reduce via retainAll: " + (end - start) );

        List<String> skippedTrigrams = new ArrayList<>();
        long ignoredElements = 0L;
        while (collectedOccurencesIterator.hasNext()) {
            TrigramOccurence skippedTrigram = collectedOccurencesIterator.next();
            ignoredElements += skippedTrigram.getOccurenceCount();
            skippedTrigrams.add( skippedTrigram.getTrigram() );
        }

        // save the skipped tri-grams for later optimized/optimizing searches.
        this.setSkippedTrigramsInOptSearch( skippedTrigrams );

        System.out.println( "Skipped Elements: " + ignoredElements );

        // TODO: if trigram documentid lists are too big for direct filtering, then use bloom 
        //       filters but don't double check the positive findings, whether they are false 
        //       positives, just use the negatives to kick out the elements in the hope that, 
        //       that a later Bloom filter for a different trigram would also kick out the 
        //       documentid.

        return resultSet;
    }

    private void setSkippedTrigramsInOptSearch( List<String> unprocessedTrigrams ) {
        this.unprocessedTrigrams = unprocessedTrigrams;
    }

    public Collection<String> getSkippedTrigramsInOptSearch() {
        return unprocessedTrigrams;
    }

    public String getDocumentContent( String path ) {
        DocumentId documentId = DocumentId.createDocumentIDFromRelativePath( Paths.get( path ) );

        try (InputStream fileContentReader = theFileCache.getContentAsStream( documentId ); ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileContentReader.read( buffer )) != -1) {
                result.write( buffer, 0, length );
            }
            return result.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "The Content you looked for, could not be retrived.";
    }

    /**
     * @param documentID
     * @return 
     */
    public List<String> getDocumentWordlist( String documentID ) {
        return theWordlistCache.loadWordList( documentID );
    }

    public TrigramOccurence getTrigramOccurence( String trigram ) {
        return this.theSearchTrigramIndex.loadDocumentCountForTrigram( trigram );
    }

    /**
     * @return
     */
    public MetadataCache getMetaDataCache() {
        return theMetadataCache;
    }

    /**
     * @return
     */
    public WordlistCache getWordlistCache() {
        return theWordlistCache;
    }

    public SearchQueryCache getSearchQueryCache() {
        return theSearchQueryCache;
    }

}
