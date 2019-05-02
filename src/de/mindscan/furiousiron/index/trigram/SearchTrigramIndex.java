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
import java.util.Collections;
import java.util.Set;

import com.google.gson.Gson;

import de.mindscan.furiousiron.index.trigram.model.TrigramIndexJsonModel;

/**
 * 
 */
public class SearchTrigramIndex {

    private static final String TRIGRAM_INVERSE_INDEX_LOCATION = "inverseTrigram.index";

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

    private Set<String> loadFromDisk( String trigram ) {
        Path pathForTrigrams = TrigramSubPathCalculator.getPathForTrigram( searchTrigramsPath, trigram, ".0.reference" );

        if (Files.exists( pathForTrigrams, LinkOption.NOFOLLOW_LINKS )) {
            Gson gson = new Gson();
            try (Reader json = Files.newBufferedReader( pathForTrigrams )) {
                TrigramIndexJsonModel fromJson = gson.fromJson( json, TrigramIndexJsonModel.class );
                return fromJson.getRelatedDocuments();
            }
            catch (Exception e) {
            }
        }

        // TODO: more References needed... Not only ".0.reference

        return Collections.emptySet();

    }

}
