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
package de.mindscan.furiousiron.index.trigram;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.Gson;

import de.mindscan.furiousiron.index.trigram.model.TrigramDocumentCountJsonModel;
import de.mindscan.furiousiron.index.trigram.model.TrigramIndexJsonModel;

/**
 * 
 */
public class SearchTrigramIndex {

    private static final String TRIGRAM_INVERSE_INDEX_LOCATION = "inverseTrigram.index";

    private static final String TRIGRAM_REFERENCE_SUFFIX = ".reference";
    private static final String TRIGRAM_COUNT_SUFFIX = ".reference_count";

    // Means that we can inversely reference 1024 * 3072 documents
    private static final int MAX_INDEX_REFERENCES = 1024;

    private Path searchTrigramsPath;

    /**
     * @param indexFolder
     */
    public SearchTrigramIndex( Path indexFolder ) {
        this.searchTrigramsPath = indexFolder.resolve( TRIGRAM_INVERSE_INDEX_LOCATION );
    }

    /**
    * @param trigram
    * @return
    */
    public Collection<String> getDocumentIdsForTrigram( String trigram ) {
        return loadFromDisk( trigram );
    }

    /**
     * This will open and load the trigram count file from disk. This should be done once for the involved trigrams,
     * because of speed.
     * 
     * @param trigram
     * @return
     */
    public TrigramOccurrence loadDocumentCountForTrigram( String trigram ) {
        // TODO: LRU Cache for the number of trigrams, so the number is only calculated/read once from disk.

        Path pathForTrigramCount = TrigramSubPathCalculator.getPathForTrigram( searchTrigramsPath, trigram, TRIGRAM_COUNT_SUFFIX );

        if (Files.exists( pathForTrigramCount, LinkOption.NOFOLLOW_LINKS )) {
            Gson gson = new Gson();
            try (Reader json = Files.newBufferedReader( pathForTrigramCount )) {
                TrigramDocumentCountJsonModel fromJson = gson.fromJson( json, TrigramDocumentCountJsonModel.class );
                long occurence = fromJson.getRelatedDocumentsCount();
                return new TrigramOccurrence( trigram, occurence );
            }
            catch (Exception e) {
                return new TrigramOccurrence( trigram, 3072L );
            }
        }
        else {
            // No count file exist, means, that there are no documents to be found.
            return new TrigramOccurrence( trigram, 0L );
        }
    }

    private Set<String> loadFromDisk( String trigram ) {
        Set<String> result = new TreeSet<>();

        for (int counter = 0; counter < MAX_INDEX_REFERENCES; counter++) {
            Path pathForTrigrams = TrigramSubPathCalculator.getPathForTrigram( searchTrigramsPath, trigram, "." + counter + TRIGRAM_REFERENCE_SUFFIX );

            if (Files.exists( pathForTrigrams, LinkOption.NOFOLLOW_LINKS )) {
                Gson gson = new Gson();
                try (Reader json = Files.newBufferedReader( pathForTrigrams )) {
                    TrigramIndexJsonModel fromJson = gson.fromJson( json, TrigramIndexJsonModel.class );
                    result.addAll( fromJson.getRelatedDocuments() );
                }
                catch (Exception e) {
                    break;
                }
            }
            else {
                break;
            }
        }

        return result;
    }

}
