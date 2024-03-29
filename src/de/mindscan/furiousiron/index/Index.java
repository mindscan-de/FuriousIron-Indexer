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
package de.mindscan.furiousiron.index;

import java.nio.file.Path;

import de.mindscan.furiousiron.index.cache.DocumentCache;
import de.mindscan.furiousiron.index.cache.MetadataCache;
import de.mindscan.furiousiron.index.cache.SearchQueryCache;
import de.mindscan.furiousiron.index.cache.WordlistCache;
import de.mindscan.furiousiron.index.hfb.InverseMetadataHFBFilterIndex;
import de.mindscan.furiousiron.index.trigram.InverseMetadataTrigramIndex;
import de.mindscan.furiousiron.index.trigram.InverseTrigramIndex;

/**
 * 
 */
public class Index {

    private final DocumentCache theFileCache;
    private final MetadataCache theMetadataCache;
    private final WordlistCache theWordlistCache;
    private final InverseTrigramIndex theInverseTrigramIndex;
    private final InverseMetadataTrigramIndex theInverseMetadataTrigramIndex;
    private final InverseMetadataHFBFilterIndex theInverseMetadataHFBFilterIndex;
    private final SearchQueryCache theSearchQueryCache;

    /**
     * 
     */
    public Index( Path indexFolder ) {
        theFileCache = new DocumentCache( indexFolder );
        theMetadataCache = new MetadataCache( indexFolder );
        theWordlistCache = new WordlistCache( indexFolder );
        theInverseTrigramIndex = new InverseTrigramIndex( indexFolder );
        theInverseMetadataTrigramIndex = new InverseMetadataTrigramIndex( indexFolder );
        theSearchQueryCache = new SearchQueryCache( indexFolder );
        theInverseMetadataHFBFilterIndex = new InverseMetadataHFBFilterIndex( indexFolder );
    }

    /**
     * @return the document cache access (document id -> document content)
     */
    public DocumentCache getDocumentCache() {
        return theFileCache;
    }

    /**
     * @return the meta data cache access (document id -> metadata)
     */
    public MetadataCache getMetadataCache() {
        return theMetadataCache;
    }

    /**
     * @return the wordlist cache access (document id -> wordlist)
     */
    public WordlistCache getWordlistCache() {
        return theWordlistCache;
    }

    /**
     * @return the (inverse) trigram index access (trigram -> document id)
     */
    public InverseTrigramIndex getInverseTrigramIndex() {
        return theInverseTrigramIndex;
    }

    /**
     * @return the (inverse) metadata trigram index access (trigram -> document id)
     */
    public InverseMetadataTrigramIndex getInverseMetadataTrigramIndex() {
        return theInverseMetadataTrigramIndex;
    }

    /**
     * @return the (inverse) metadata trigram index filter access (trigram -> hfb filter bank)
     */
    public InverseMetadataHFBFilterIndex getInverseMetadataHFBFilterIndex() {
        return theInverseMetadataHFBFilterIndex;
    }

    public SearchQueryCache getSearchQueryCache() {
        return theSearchQueryCache;
    }

}
