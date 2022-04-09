/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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

import java.nio.file.Path;
import java.util.Collection;
import java.util.Deque;
import java.util.Set;

import de.mindscan.furiousiron.document.DocumentId;
import de.mindscan.furiousiron.document.DocumentIdFactory;
import de.mindscan.furiousiron.document.DocumentMetadata;
import de.mindscan.furiousiron.index.Index;

/**
 * 
 */
public class MetadataFileIndexer {

    private Index index;

    public void buildIndex( Deque<Path> filesToBeIndexed, Path crawlFolder, Path indexFolder ) {
        setIndex( new Index( indexFolder ) );

        index.getInverseMetadataTrigramIndex().init();

        for (Path fileToIndex : filesToBeIndexed) {
            try {
                updateMetaIndexWithSingleFile( fileToIndex, crawlFolder, indexFolder );
            }
            catch (Exception ignore) {
                // intentionally left blank
            }
        }

        index.getInverseMetadataTrigramIndex().save();
    }

    private void updateMetaIndexWithSingleFile( Path fileToIndex, Path crawlFolder, Path indexFolder ) {
        String documentKey = extractDocumentKeyFromPath( fileToIndex );
        DocumentId documentId = DocumentIdFactory.createDocumentIDFromDocumentKey( documentKey );

        // load known metadata for document
        DocumentMetadata documentMetaData = index.getMetadataCache().loadMetadata( documentKey );

        System.out.println( documentKey );
        System.out.println( documentMetaData.getRelativePath() );

        // get all "values" and treat them as words
        Collection<String> uniqueWordlist = documentMetaData.getAllValues();
        Set<String> uniqueTrigramlist = SimpleWordUtils.getUniqueTrigramsFromWordList( uniqueWordlist );

        index.getInverseMetadataTrigramIndex().addTrigramsForMetadata( documentId, uniqueTrigramlist );
    }

    private String extractDocumentKeyFromPath( Path fileToIndex ) {
        String simpleFilename = fileToIndex.getFileName().toString();
        return simpleFilename.substring( 0, simpleFilename.length() - ".metadata".length() );
    }

    private void setIndex( Index index ) {
        this.index = index;
    }

    public Index getIndex() {
        return index;
    }

}
