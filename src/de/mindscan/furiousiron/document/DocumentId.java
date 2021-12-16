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

import java.nio.file.Path;

/**
 * This class maps a single documentKey to a documentLocation.
 * 
 * This class may be extended by a documentContentKey.   
 */
public class DocumentId {

    private String documentKey;
    private Path documentLocation;

    /**
     * Ctor.
     * 
     * @param documentKey - The unique key representing a document
     * @param documentLocation - The document location represented by this key
     */
    DocumentId( String documentKey, Path documentLocation ) {
        this.documentKey = documentKey;
        this.documentLocation = documentLocation;
    }

    /**
     * @return the documentKey for this document
     */
    public String getDocumentKey() {
        return documentKey;
    }

    /**
     * @return the original documentLocationPath object for this document.
     */
    public Path getDocumentLocationPath() {
        return documentLocation;
    }

    /**
     * @return the original documentLocation for this document
     */
    public String getDocumentLocation() {
        return documentLocation.toString();
    }

}
