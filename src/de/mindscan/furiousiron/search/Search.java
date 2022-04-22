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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import de.mindscan.furiousiron.document.DocumentId;
import de.mindscan.furiousiron.document.DocumentIdFactory;
import de.mindscan.furiousiron.index.cache.DocumentCache;
import de.mindscan.furiousiron.index.cache.MetadataCache;
import de.mindscan.furiousiron.index.cache.SearchQueryCache;
import de.mindscan.furiousiron.index.cache.WordlistCache;
import de.mindscan.furiousiron.index.trigram.SeachMetadataTrigramIndex;
import de.mindscan.furiousiron.index.trigram.SearchTrigramIndex;
import de.mindscan.furiousiron.index.trigram.TrigramOccurrence;
import de.mindscan.furiousiron.index.trigram.TrigramUsage;
import de.mindscan.furiousiron.index.trigram.TrigramUsage.TrigramUsageState;
import de.mindscan.furiousiron.indexer.SimpleWordUtils;
import de.mindscan.furiousiron.util.StopWatch;

/**
 * 
 */
public class Search {

    // for content
    private final DocumentCache theFileCache;
    // for ranking
    private final MetadataCache theMetadataCache;
    // for ranking the results
    private final WordlistCache theWordlistCache;
    // for ranking
    private final SearchTrigramIndex theSearchTrigramIndex;
    // 
    private final SeachMetadataTrigramIndex theSearchMetadataTrigramIndex;
    // for performance
    private final SearchQueryCache theSearchQueryCache;

    private SearchExecutionDetails searchDetails;
    private SearchExecutionDetails metadataSearchDetails;

    /**
     * Ctor.
     * 
     * @param indexFolder The folder, where the index root is located
     */
    public Search( Path indexFolder ) {
        theFileCache = new DocumentCache( indexFolder );
        theMetadataCache = new MetadataCache( indexFolder );
        theWordlistCache = new WordlistCache( indexFolder );
        theSearchTrigramIndex = new SearchTrigramIndex( indexFolder );
        theSearchMetadataTrigramIndex = new SeachMetadataTrigramIndex( indexFolder );
        theSearchQueryCache = new SearchQueryCache( indexFolder );

        searchDetails = new SearchExecutionDetails();
        metadataSearchDetails = new SearchExecutionDetails();
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

        Set<String> documentIdsForOneWord = collectDocumentIdsForTrigramsOpt( uniqueTrigramsFromWord );

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

        Set<String> documentIdsForOneWord = collectDocumentIdsForTrigramsOpt( uniqueTrigramsFromWord );

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

    public Map<String, SearchResultCandidates> searchMetadataToMap( String metadatasearchterm ) {
        // assume current search term is exactly one word.
        String processedSearchTerm = metadatasearchterm.toLowerCase();

        // extract word  from searchterm 
        Collection<String> uniqueTrigramsFromWord = SimpleWordUtils.getUniqueTrigramsFromWord( processedSearchTerm );

        Set<String> documentIdsForOneWord = collectDocumentIdsForMetadataTrigramsOpt( uniqueTrigramsFromWord );

        // check, that a word is part of a page
        Map<String, SearchResultCandidates> searchResult = new HashMap<>();
        for (String documentId : documentIdsForOneWord) {
            SearchResultCandidates candidate = new SearchResultCandidates( documentId );

            // TODO: i would like to check the metadata values, but this might be way too many documents.
            // this load of metadata should only be done for the candidates 

            searchResult.put( documentId, candidate );
        }

        return searchResult;
    }

    /**
     * This is the most recent and most universal search using combined trigrams, other methods for search above are
     * here for legacy reasons, in case this search fail for some reason. (To be adapted for metadata search as well.)
     * @param uniqueTrigramsFromWord
     * @return
     */
    public Set<String> collectDocumentIdsForTrigramsOpt( Collection<String> uniqueTrigramsFromWord ) {
        HashSet<String> resultSet = new HashSet<String>();
        List<TrigramUsage> trigramUsage = new ArrayList<>( uniqueTrigramsFromWord.size() );

        List<TrigramOccurrence> sortedTrigramOccurrences = getTrigramOccurrencesSortedByOccurrence( uniqueTrigramsFromWord );

        SearchExecutionDetails executionDetails = new SearchExecutionDetails();
        executionDetails.setLastQueryTrigramOccurrences( sortedTrigramOccurrences );

        for (TrigramOccurrence trigramOccurence : sortedTrigramOccurrences) {
            System.out.println( "Debug-TrigramOccurence: " + trigramOccurence.toString() );
        }

        long previousSetSize = 0L;

        StopWatch retainAllStopWatch = StopWatch.createStarted();

        // fill resultSet with first document list (shortest), it will only get shorter 
        Iterator<TrigramOccurrence> collectedOccurencesIterator = sortedTrigramOccurrences.iterator();
        if (collectedOccurencesIterator.hasNext()) {
            TrigramOccurrence firstTrigramOccurence = collectedOccurencesIterator.next();
            resultSet = new HashSet<String>( getDocumentsForTrigram( firstTrigramOccurence.getTrigram() ) );

            trigramUsage.add( new TrigramUsage( firstTrigramOccurence, TrigramUsageState.SUCCESS ) );
            previousSetSize = firstTrigramOccurence.getOccurrenceCount();

            System.out.println( "Reduction starts from: " + resultSet.size() + " for " + firstTrigramOccurence.getTrigram() );
        }

        // we make at least one round of reducing the number of document candidates by combining the set of 
        // the first and second trigram's associated documents and continue until it becomes inefficient
        while (collectedOccurencesIterator.hasNext()) {
            TrigramOccurrence trigram = collectedOccurencesIterator.next();

            Collection<String> documentIds = theSearchTrigramIndex.getDocumentIdsForTrigram( trigram.getTrigram() );

            // At the moment this code is fast enough. the only bottleneck is to load
            // the json documents from disk. The retainall operation is not that slow,
            // as i was expecting.

            // sorting trigrams leads to a highly imbalanced (retain) comparison, 
            // resultSet will become smaller and smaller and the documentIds are becoming bigger and bigger.
            // so it might be important to optimize this implementation?

            // in case of majority of time consumed here, this retainAll operation has to be switched to a 
            // more efficient mode e.g. Skiplists or we are looking for each resultset-item via a bloomfilter
            // in documentIds-Collection, where the documentIds are the bloomfilter-hashed eleemnts.

            resultSet.retainAll( documentIds );
            int remainingSetSize = resultSet.size();

            trigramUsage.add( getTrigramUsageByReduction( trigram, remainingSetSize < previousSetSize ) );

            System.out.println( "Reduction to: " + remainingSetSize + " using trigram: " + trigram.getTrigram() );

            if (trigram.getOccurrenceCount() > (48 * remainingSetSize)) {
                // stop if it is too unbalanced... we probably already are in X+10% range of maximal search results
                break;
            }

            previousSetSize = remainingSetSize;
        }
        retainAllStopWatch.stop();

        List<TrigramOccurrence> skippedTrigrams = new ArrayList<>();

        long ignoredElements = collectSkippedTrigrams( collectedOccurencesIterator, skippedTrigrams::add );

        // save the skipped tri-grams for later optimized/optimizing searches.
        executionDetails.setSkippedTrigramsInOptSearch( skippedTrigrams );
        executionDetails.setTrigramUsage( trigramUsage );

        setSearchDetails( executionDetails );

        System.out.println( "Time to reduce via retainAll: " + (retainAllStopWatch.getElapsedTime()) );
        System.out.println( "Skipped Elements: " + ignoredElements );

        return resultSet;
    }

    // implementation of search algorithm on metadata using same idea as in collectDocumentIdsForTrigramOpt 
    public Set<String> collectDocumentIdsForMetadataTrigramsOpt( Collection<String> uniqueTrigramsFromWord ) {
        HashSet<String> resultSet = new HashSet<String>();
        List<TrigramUsage> trigramUsage = new ArrayList<>( uniqueTrigramsFromWord.size() );

        List<TrigramOccurrence> sortedMetadataTrigramOccurrences = getMetadataTrigramOccurrencesSortedByOccurrence( uniqueTrigramsFromWord );

        SearchExecutionDetails executionDetails = new SearchExecutionDetails();
        executionDetails.setLastQueryTrigramOccurrences( sortedMetadataTrigramOccurrences );

        for (TrigramOccurrence trigramOccurence : sortedMetadataTrigramOccurrences) {
            System.out.println( "Debug-MetadataTrigramOccurence: " + trigramOccurence.toString() );
        }

        long previousSetSize = 0L;

        StopWatch retainAllStopWatch = StopWatch.createStarted();

        // fill resultSet with first document list (shortest), it will only get shorter 
        Iterator<TrigramOccurrence> collectedOccurencesIterator = sortedMetadataTrigramOccurrences.iterator();
        if (collectedOccurencesIterator.hasNext()) {
            TrigramOccurrence firstTrigramOccurence = collectedOccurencesIterator.next();
            resultSet = new HashSet<String>( getDocumentsForMetadataTrigram( firstTrigramOccurence.getTrigram() ) );

            trigramUsage.add( new TrigramUsage( firstTrigramOccurence, TrigramUsageState.SUCCESS ) );
            previousSetSize = firstTrigramOccurence.getOccurrenceCount();

            System.out.println( "Reduction starts from: " + resultSet.size() + " elements for " + firstTrigramOccurence.getTrigram() );
        }

        // we make at least one round of reducing the number of document candidates by combining the set of 
        // the first and second trigram's associated documents and continue until it becomes inefficient
        while (collectedOccurencesIterator.hasNext()) {
            TrigramOccurrence trigram = collectedOccurencesIterator.next();

            Collection<String> documentIds = getDocumentsForMetadataTrigram( trigram.getTrigram() );

            // At the moment this code is fast enough. the only bottleneck is to load
            // the json documents from disk. The retainall operation is not that slow,
            // as i was expecting.

            // using sorted trigrams leads to a highly unbalanced (retain) comparison, (the first two or 
            // three retain operations will do most of the heavy lifting, at the same time, the resultSet
            // will become smaller and smaller and the documentIds are becoming bigger and bigger. So most
            // of the time we can just skip the loading of the document ids, when >95% of the maximum 
            // reduction is already done.
            // Each following iteration of this loop will become more and more expensive IO wise, but 
            // leads to less and less reduction of the final set, therefore breaking this loop early 
            // is highly encouraged.

            resultSet.retainAll( documentIds );
            int remainingSetSize = resultSet.size();

            trigramUsage.add( getTrigramUsageByReduction( trigram, remainingSetSize < previousSetSize ) );

            System.out.println( "Reduction to: " + remainingSetSize + " elemenets using trigram: " + trigram.getTrigram() );

            if (trigram.getOccurrenceCount() > (128 * remainingSetSize)) {
                // stop if it is too unbalanced... we probably already are in X+10% range of maximal search results
                break;
            }

            previousSetSize = remainingSetSize;
        }
        retainAllStopWatch.stop();

        List<TrigramOccurrence> skippedTrigrams = new ArrayList<>();

        long ignoredElements = collectSkippedTrigrams( collectedOccurencesIterator, skippedTrigrams::add );

        executionDetails.setSkippedTrigramsInOptSearch( skippedTrigrams );
        executionDetails.setTrigramUsage( trigramUsage );

        setMetadataSearchDetails( executionDetails );

        System.out.println( "Time to reduce via retainAll: " + (retainAllStopWatch.getElapsedTime()) );
        System.out.println( "Skipped Elements: " + ignoredElements );

        return resultSet;
    }

    private long collectSkippedTrigrams( Iterator<TrigramOccurrence> collectedOccurencesIterator, Function<TrigramOccurrence, Boolean> collector ) {
        long ignoredElements = 0L;
        while (collectedOccurencesIterator.hasNext()) {
            TrigramOccurrence skippedTrigram = collectedOccurencesIterator.next();
            ignoredElements += skippedTrigram.getOccurrenceCount();
            collector.apply( skippedTrigram );
        }
        return ignoredElements;
    }

    private TrigramUsage getTrigramUsageByReduction( TrigramOccurrence trigram, boolean isReduction ) {
        if (isReduction) {
            return new TrigramUsage( trigram, TrigramUsageState.SUCCESS );
        }

        return new TrigramUsage( trigram, TrigramUsageState.FAILED );
    }

    public List<TrigramOccurrence> getTrigramOccurrencesSortedByOccurrence( Collection<String> uniqueTrigramsFromWord ) {
        // convert trigrams to TrigramOccurences
        List<TrigramOccurrence> collectedOccurrences = uniqueTrigramsFromWord.stream().map( ( trigram ) -> this.getTrigramOccurrence( trigram ) )
                        .collect( Collectors.toList() );

        // sort uniqueTrigramsFromWord by number of expected results in increasing order.
        collectedOccurrences.sort( Comparator.<TrigramOccurrence> comparingLong( occurence -> occurence.getOccurrenceCount() ) );
        return collectedOccurrences;
    }

    public List<TrigramOccurrence> getMetadataTrigramOccurrencesSortedByOccurrence( Collection<String> uniqueTrigramsFromWord ) {
        // convert trigrams to TrigramOccurences
        List<TrigramOccurrence> collectedOccurrences = uniqueTrigramsFromWord.stream().map( ( trigram ) -> this.getMetadataTrigramOccurrence( trigram ) )
                        .collect( Collectors.toList() );

        // sort uniqueTrigramsFromWord by number of expected results in increasing order.
        collectedOccurrences.sort( Comparator.<TrigramOccurrence> comparingLong( occurence -> occurence.getOccurrenceCount() ) );
        return collectedOccurrences;
    }

    public String getDocumentContent( String path ) {
        DocumentId documentId = DocumentIdFactory.createDocumentIDFromRelativePath( Paths.get( path ) );

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

    public List<String> getDocumentContentLines( String documentIdmd5 ) {
        DocumentId documentId = DocumentIdFactory.createDocumentIDFromDocumentKey( documentIdmd5 );

        try (BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader( theFileCache.getContentAsStream( documentId ), StandardCharsets.UTF_8 ) )) {
            return bufferedReader.lines().collect( Collectors.toList() );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return List.of();
    }

    public List<String> getDocumentWordlist( String documentID ) {
        return theWordlistCache.loadWordList( documentID );
    }

    public Map<String, String> getDocumentMetadataMap( String documentID ) {
        return theMetadataCache.loadMetadata( documentID ).getKeyValuesAsMap();
    }

    public Map<String, Integer> getTrigramTermFrequencyData( String documentID ) {
        return theWordlistCache.loadTTFData( documentID );
    }

    private TrigramOccurrence getTrigramOccurrence( String trigram ) {
        return theSearchTrigramIndex.loadDocumentCountForTrigram( trigram );
    }

    private TrigramOccurrence getMetadataTrigramOccurrence( String trigram ) {
        return theSearchMetadataTrigramIndex.loadDocumentCountForTrigram( trigram );
    }

    private Collection<String> getDocumentsForTrigram( String trigram ) {
        return theSearchTrigramIndex.getDocumentIdsForTrigram( trigram );
    }

    private Collection<String> getDocumentsForMetadataTrigram( String trigram ) {
        return theSearchMetadataTrigramIndex.getDocumentIdsForTrigram( trigram );
    }

    public MetadataCache getMetaDataCache() {
        return theMetadataCache;
    }

    public WordlistCache getWordlistCache() {
        return theWordlistCache;
    }

    public SearchQueryCache getSearchQueryCache() {
        return theSearchQueryCache;
    }

    private void setSearchDetails( SearchExecutionDetails executionDetails ) {
        this.searchDetails = executionDetails;
    }

    public SearchExecutionDetails getSearchDetails() {
        return searchDetails;
    }

    private void setMetadataSearchDetails( SearchExecutionDetails executionDetails ) {
        this.metadataSearchDetails = executionDetails;
    }

    public SearchExecutionDetails getMetadataSearchDetails() {
        return metadataSearchDetails;
    }

}
