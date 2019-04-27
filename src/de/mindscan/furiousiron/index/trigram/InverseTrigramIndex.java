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

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.mindscan.furiousiron.document.DocumentId;

/**
 * 
 */
public class InverseTrigramIndex {

    private static final String TRIGRAM_INVERSE_INDEX = "inverseTrigram.index";

    private Map<String, TrigramIndex> inverseIndex = new HashMap<>();
    private final Path inverseTrigramsPath;

    /**
     * @param indexFolder
     */
    public InverseTrigramIndex( Path indexFolder ) {
        this.inverseTrigramsPath = indexFolder.resolve( TRIGRAM_INVERSE_INDEX );
    }

    /**
     * 
     */
    public void init() {
        inverseIndex = new HashMap<>();
    }

    /**
     * @param documentId the document id to add to each trigram
     * @param uniqueTrigramlist the collection of trigrams contained in the document
     */
    public void addTrigramsForDocument( DocumentId documentId, Collection<String> uniqueTrigramlist ) {
        for (String trigramKey : uniqueTrigramlist) {
            inverseIndex.computeIfAbsent( trigramKey, this::createEmptyTrigramIndex ).add( documentId.getMD5hex() );
        }
    }

    /**
     * This method implements the save operation for the whole inverse index. 
     */
    public void save() {
        for (Entry<String, TrigramIndex> entry : inverseIndex.entrySet()) {
            entry.getValue().save();
        }
    }

    private TrigramIndex createEmptyTrigramIndex( String trigram ) {
        return new TrigramIndex( trigram, 0, inverseTrigramsPath );
    }
}
