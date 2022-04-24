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
import java.nio.file.Path;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.mindscan.furiousiron.classifier.Classifier;
import de.mindscan.furiousiron.classifier.SimpleClassifier;
import de.mindscan.furiousiron.document.DocumentId;
import de.mindscan.furiousiron.document.DocumentIdFactory;
import de.mindscan.furiousiron.document.DocumentMetadata;
import de.mindscan.furiousiron.document.DocumentMetadataFactory;
import de.mindscan.furiousiron.index.Index;

/**
 * 
 */
public class SimpleFileIndexer {
    private Index index;
    private Classifier classifier;

    /**
     * @param filesToBeIndexed provider for files which should be indexed
     * @param crawlFolder the base folder, of what files to index
     * @param indexFolder the index folder, where to put the index
     */
    public void buildIndex( Deque<Path> filesToBeIndexed, Path crawlFolder, Path indexFolder ) {

        setIndex( new Index( indexFolder ) );
        setClassifier( new SimpleClassifier() );

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
        DocumentId documentId = DocumentIdFactory.createDocumentID( fileToIndex, crawlFolder );
        DocumentMetadata documentMetaData = DocumentMetadataFactory.createDocumentMetadata( documentId, fileToIndex );

        System.out.println( documentId.getDocumentKey() );
        System.out.println( documentId.getDocumentLocation() );

        // store a copy of the document in the cache
        index.getDocumentCache().createDocumentCopy( documentId, fileToIndex );

        // get unique word list for document
        Map<String, Integer> ttfList = SimpleWordUtils.buildTrigramTermFrequency( documentMetaData, fileToIndex );
        List<String> uniqueWordlist = SimpleWordUtils.buildUniqueWordlist( documentMetaData, fileToIndex );
        Set<String> uniqueTrigramlist = SimpleWordUtils.getUniqueTrigramsFromWordList( uniqueWordlist );

        index.getWordlistCache().addTTFList( documentId, ttfList );
        index.getWordlistCache().addUniqueWordlist( documentId, uniqueWordlist );
        index.getWordlistCache().addUniqueTrigrams( documentId, uniqueTrigramlist );
        index.getInverseTrigramIndex().addTrigramsForDocument( documentId, uniqueTrigramlist );

        // TODO: update the metadata object with more expensive information / statistics
        // Path, Virtual directory entry
        // Stopwords: since we don't index textual documents, we don't need this
        // Porter stemming: since we don't index textual documents, we actually don't need this
        // TODO: some ideas to come

        getClassifier().classify( documentId, documentMetaData, fileToIndex );
        getClassifier().classify( documentId, documentMetaData, uniqueWordlist );

        // this should be done after parsing/analysing/classifying/indexing the document
        index.getMetadataCache().addDocumentMetadata( documentId, documentMetaData );

    }

    public Index getIndex() {
        return index;
    }

    public void setIndex( Index index ) {
        this.index = index;
    }

    public Classifier getClassifier() {
        return classifier;
    }

    public void setClassifier( Classifier classifier ) {
        this.classifier = classifier;
    }
}
