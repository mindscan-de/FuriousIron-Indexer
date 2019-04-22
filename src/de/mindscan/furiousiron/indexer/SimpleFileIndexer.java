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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.mindscan.furiousiron.document.DocumentId;
import de.mindscan.furiousiron.document.DocumentMetadata;
import de.mindscan.furiousiron.index.Index;

/**
 * 
 */
public class SimpleFileIndexer {
    private Index index;

    /**
     * @param filesToBeIndexed provider for files which should be indexed
     * @param crawlFolder the base folder, of what files to index
     * @param indexFolder the index folder, where to put the index
     */
    public void buildIndex( Deque<Path> filesToBeIndexed, Path crawlFolder, Path indexFolder ) {

        setIndex( new Index( indexFolder ) );

        index.getInverseTrigramIndex().init();

        for (Path fileToIndex : filesToBeIndexed) {
            try {
                updateIndexWithSingleFile( fileToIndex, crawlFolder, indexFolder );

                // break after the first file to index
                // break;
            }
            catch (Exception ignore) {
                // intentionally left blank yet.
                // File should be appended again, if temporary problem or scheduled again for later indexing operation. 
            }
        }

        index.getInverseTrigramIndex().save();
    }

    private void updateIndexWithSingleFile( Path fileToIndex, Path crawlFolder, Path indexFolder ) throws IOException {
        DocumentId documentId = DocumentId.createDocumentID( fileToIndex, crawlFolder );
        DocumentMetadata documentMetaData = DocumentMetadata.createDocumentMetadata( documentId, fileToIndex );

        System.out.println( documentId.getMD5hex() );
        System.out.println( documentId.getRelativePathToCrawlingDirectory() );

        // store a copy of the document in the cache
        index.getDocumentCache().createDocumentCopy( documentId, fileToIndex );

        // get unique word list for document
        List<String> uniqueWordlist = buildUniqueWordlist( fileToIndex );
        Set<String> uniqueTrigramlist = buildUniqueTrigrams( uniqueWordlist );

        index.getWordlistCache().addUniqueWordlist( documentId, uniqueWordlist );
        index.getWordlistCache().addUniqueTrigrams( documentId, uniqueTrigramlist );
        index.getInverseTrigramIndex().addTrigramsForDocument( documentId, uniqueTrigramlist );

        // TODO: Number of Lines...
        // TODO: update the metadata object with more expensive information / statistics
        // Path, Virtual directory entry
        // TODO: Stopwords
        // TODO: porter stemming
        // TODO: some ideas to come

        // this should be done after parsing/analysing/indexing the document
        index.getMetadataCache().addDocumentMetadata( documentId, documentMetaData );

    }

    private Set<String> buildUniqueTrigrams( List<String> flatWordList ) {
        List<List<String>> collectedTrigramsForEachWord = flatWordList.stream().map( this::trigramsplitter ).distinct().collect( Collectors.toList() );

        Set<String> uniqueTrigrams = collectedTrigramsForEachWord.stream().flatMap( List::stream ).collect( Collectors.toSet() );

        return uniqueTrigrams;
    }

    private List<String> buildUniqueWordlist( Path fileToIndex ) throws IOException {
        List<String> allLines = Files.readAllLines( fileToIndex );

        List<List<String>> collectedWordsPerLine = allLines.stream().map( this::toLowerCase ).map( this::nonwordsplitter ).filter( this::onlyNonEmpy )
                        .collect( Collectors.toList() );

        List<String> flatUniqueWordList = collectedWordsPerLine.stream().flatMap( List::stream ).filter( this::atLeastThreeCharsLong ).distinct()
                        .collect( Collectors.toList() );

        return flatUniqueWordList;
    }

    private boolean atLeastThreeCharsLong( String x ) {
        return x.length() >= 3;
    }

    private boolean onlyNonEmpy( Collection<String> x ) {
        return x.size() > 0;
    }

    private String toLowerCase( String string ) {
        return string.toLowerCase();
    }

    private List<String> nonwordsplitter( String string ) {
        String[] splitted = string.split( "[ /\\+\\-\\*\t\n\r\\.:;,\\(\\)\\{\\}\\[\\]]" );
        return Arrays.stream( splitted ).map( x -> x.trim() ).filter( x -> x != null && x.length() > 0 ).collect( Collectors.toList() );
    }

    private List<String> trigramsplitter( String string ) {
        List<String> result = new ArrayList<>();
        for (int startIndex = 0; startIndex <= string.length() - 3; startIndex++) {
            result.add( string.substring( startIndex, startIndex + 3 ) );
        }
        return result;
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex( Index index ) {
        this.index = index;
    }
}
