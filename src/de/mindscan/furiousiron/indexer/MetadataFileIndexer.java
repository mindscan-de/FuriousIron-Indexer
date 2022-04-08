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
import java.util.Deque;

import de.mindscan.furiousiron.document.DocumentMetadata;
import de.mindscan.furiousiron.index.Index;

/**
 * 
 */
public class MetadataFileIndexer {

    private Index index;

    public void buildIndex( Deque<Path> filesToBeIndexed, Path crawlFolder, Path indexFolder ) {
        setIndex( new Index( indexFolder ) );

        // index.getMetadataInverseTrigramIndex().init();

        for (Path fileToIndex : filesToBeIndexed) {
            try {
                updateMetaIndexWithSingleFile( fileToIndex, crawlFolder, indexFolder );
            }
            catch (Exception ignore) {
                // intentionally left blank
            }
        }

        // index.getMetadataInverseTrigramIndex().save();
    }

    private void updateMetaIndexWithSingleFile( Path fileToIndex, Path crawlFolder, Path indexFolder ) {
        // TODO: do what ever is needed to understand the metadata.
        // extract documentid, 
        // read the document id from the filename?
        // DocumentID documentId = DocumentIdFactory.createDocumentID()
        String documentKey = "";

        // load metadata for documentid
        DocumentMetadata documentMetaData = index.getMetadataCache().loadMetadata( documentKey );

        // get all "values" and treat them as words
        // documentMetaData.getAllValues();

        // split these "words" into trigrams

        // index.getMetadataInverseTrigramIndex().addTrigramsForMetadata( documentId, .... );
    }

    private void setIndex( Index index ) {
        this.index = index;
    }

    public Index getIndex() {
        return index;
    }

}
