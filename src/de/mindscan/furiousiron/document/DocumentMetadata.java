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
package de.mindscan.furiousiron.document;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains information about a given document, while indexing. It also contains the truth about the filename and the paths.
 */
public class DocumentMetadata {

    // The DocumentId - Usually a hash sum based on the (relative) path. currently encoded in hex. (This should be more compact, like base_62 (0-9a-zA-Z))
    // thus the index size can be reduced, and a larger hash function can be used like SHA256,SHA384 instead of MD5
    private final String documentKey;
    private final String documentLocation;
    private final String documentSimpleFilename;

    // 
    private long fileSize = 0;
    private long numberOfLines = 0;
    private final Map<String, String> classifierMap;

    /**
     * @param documentLocation 
     * @param documentKey 
     */
    DocumentMetadata( String documentKey, String documentLocation, String documentSimpleFilename ) {
        this.documentKey = documentKey;
        this.documentLocation = documentLocation;
        this.documentSimpleFilename = documentSimpleFilename;
        this.classifierMap = new HashMap<>();
    }

    public String getDocumentKey() {
        return documentKey;
    }

    public String getRelativePath() {
        return documentLocation;
    }

    public String getDocumentSimpleName() {
        return documentSimpleFilename;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return documentKey + "@" + documentLocation;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize( long fileSize ) {
        this.fileSize = fileSize;
    }

    public long getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines( long numberOfLines ) {
        this.numberOfLines = numberOfLines;
    }

    public void addClass( String key, String value ) {
        this.classifierMap.put( key, value );
    }

    public boolean containsClass( String key ) {
        return this.classifierMap.containsKey( key );
    }

    public Map<String, String> getClassifierMap() {
        return this.classifierMap;
    }
}
