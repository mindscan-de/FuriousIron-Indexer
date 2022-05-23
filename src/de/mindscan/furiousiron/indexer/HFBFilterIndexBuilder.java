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

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Deque;

import com.google.gson.Gson;

import de.mindscan.furiousiron.index.Index;
import de.mindscan.furiousiron.index.trigram.SearchMetadataTrigramIndex;
import de.mindscan.furiousiron.index.trigram.TrigramOccurrence;
import de.mindscan.furiousiron.index.trigram.model.TrigramDocumentCountJsonModel;

/**
 * 
 */
public class HFBFilterIndexBuilder {

    private Index index;

    public void buildIndex( Deque<Path> filesToBeIndexed, Path crawlFolder, Path indexFolder ) {
        setIndex( new Index( indexFolder ) );

        // read access to the MetaDataTrigramIndex  
        SearchMetadataTrigramIndex searchMetadataTrigramIndex = new SearchMetadataTrigramIndex( indexFolder );

        for (Path referenceCountFile : filesToBeIndexed) {
            TrigramOccurrence trigramOccurrence = loadTrigramOccurrence( referenceCountFile );
            if (trigramOccurrence == null) {
                // TODO: log that shit.. this can be problematic, if a filter does not exist.
                continue;
            }

            // use SearchMetadataTrigramIndex load all documentids for a trigram  
            Collection<String> documentIdsForTrigram = searchMetadataTrigramIndex.getDocumentIdsForTrigram( trigramOccurrence.getTrigram() );

            // do some consistency check first...
            if (trigramOccurrence.getOccurrenceCount() == documentIdsForTrigram.size()) {
                System.out.println(
                                String.format( "'%s' is consistent: %d elements", trigramOccurrence.getTrigram(), trigramOccurrence.getOccurrenceCount() ) );
                // TODO: compile each documentid list into a HFB Filter
                // TODO: save filter for this particular trigram.
            }
            else {
                System.out.println( String.format( "'%s' is _not_ consistent; %d vs %d", trigramOccurrence.getTrigram(), trigramOccurrence.getOccurrenceCount(),
                                documentIdsForTrigram.size() ) );
            }
        }
    }

    TrigramOccurrence loadTrigramOccurrence( Path pathForTrigramCount ) {
        Gson gson = new Gson();

        try (Reader json = Files.newBufferedReader( pathForTrigramCount )) {
            TrigramDocumentCountJsonModel fromJson = gson.fromJson( json, TrigramDocumentCountJsonModel.class );
            long occurence = fromJson.getRelatedDocumentsCount();
            String trigram = fromJson.getTrigram();
            return new TrigramOccurrence( trigram, occurence );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setIndex( Index index ) {
        this.index = index;
    }

}
