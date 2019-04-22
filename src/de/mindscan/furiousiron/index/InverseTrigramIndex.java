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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.mindscan.furiousiron.document.DocumentId;

/**
 * 
 */
public class InverseTrigramIndex {

    private static final String TRIGRAM_INVERSE_INDEX = "inverseTreegram.index";

    private Map<String, Set<String>> inverseIndex = new HashMap<>();
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
     * @param documentId
     * @param uniqueTrigramlist
     */
    public void addTrigramsForDocument( DocumentId documentId, Set<String> uniqueTrigramlist ) {
        for (String trigramKey : uniqueTrigramlist) {
            inverseIndex.computeIfAbsent( trigramKey, x -> emptyTreeSet() ).add( documentId.getMD5hex() );
        }
    }

    /**
     * 
     */
    public void save() {
        try (BufferedWriter writer = Files.newBufferedWriter( inverseTrigramsPath, Charset.forName( "UTF-8" ) )) {
            // TODO: not sure, whether this is a cool idea...
//            Gson gson = new Gson();
//            writer.write( gson.toJson( inverseIndex ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TreeSet<String> emptyTreeSet() {
        return new TreeSet<>();
    }
}
