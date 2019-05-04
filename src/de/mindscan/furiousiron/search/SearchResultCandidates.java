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
package de.mindscan.furiousiron.search;

import java.util.List;

import de.mindscan.furiousiron.document.DocumentMetadata;
import de.mindscan.furiousiron.index.cache.MetadataCache;
import de.mindscan.furiousiron.index.cache.WordlistCache;

/**
 * 
 */
public class SearchResultCandidates {

    private String documentId;
    private DocumentMetadata metadata;
    private List<String> worddata;

    /**
     * 
     */
    public SearchResultCandidates( String documentId ) {
        this.documentId = documentId;

    }

    // load the metadata, and the wordlists
    public void loadFrom( MetadataCache theMetadataCache, WordlistCache theWordlistCache ) {
        metadata = theMetadataCache.loadMetadata( documentId );
        worddata = theWordlistCache.loadWordList( documentId );
    }

    public boolean containsWord( String wordtoLookFor ) {
        // check, whether the searched word is in the word data

        int wordLength = wordtoLookFor.length();
        for (String wordInDocument : worddata) {

            // TODO: should be removed from here later / tree structure, that sorts words by length
            if (wordInDocument.length() < wordLength) {
                continue;
            }

            if (wordInDocument.contains( wordtoLookFor )) {
                return true;
            }
        }

        // check, whether the searched word is in the meta data

        return false;
    }
}
