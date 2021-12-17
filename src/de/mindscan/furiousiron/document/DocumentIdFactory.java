/**
 * 
 * MIT License
 *
 * Copyright (c) 2021 Maxim Gansert, Mindscan
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

import de.mindscan.furiousiron.document.impl.DocumentKeyStrategyMD5Impl;

/**
 * 
 * A DocumentId is calculated from a location of a document. 
 * 
 * Each location should result for the most time in a unique document id. This makes the forward step, 
 * if a query asks for a document at a particular location easy. Also all Paths in between can also be 
 * calculated, without some kind of inverse index.
 * 
 * Attention: 
 * 
 * But this also means, you can enumerate the indexed directories. It depends on your security requirements
 * whether this is a desired feature or not. A search engine for public code may be able to do this trade-off. 
 * If your thread model differs, you might need a secure DocumentId function, where the directories can not
 * be enumerated using a dictionary attack or something similar. You may avoid dictionary lookups by a signing
 * key for for each requested DocumentId. But maybe even then you may still be exploitable by measuring timing
 * information.
 * 
 * The cryptographic hash function used is MD5.
 * 
 * Our reason to use this hash function is not necessarily for security but we use its property to rarely
 * produce collisions.
 * 
 * For a search engine we also want to efficiently know, whether a certain Path or URL is already in the 
 * index. This kind of information can be stored in a BloomFilter without accessing the index. The appealing
 * thing is, that the index, can be stored in a flat manner and can be scaled/distributed horizontally, but
 * can also managed to be saved in a directory structure. by using parts of the DocumentId (the hash code)
 * as the "directory"-key.
 * 
 * If you want to be able to de-duplicate the documents you would choose a DocumentID based on the content
 * hash or a keyed MAC.
 *
 * A combination of both allows a de-duplication and a flat storage.
 * 
 * If you need security a totally random DocumentId would be the way to go. That means we might be interested
 * in different strategies for finding documents.
 *   
 */

public class DocumentIdFactory {

    private static DocumentKeyStrategy documentKeyStrategy = new DocumentKeyStrategyMD5Impl();

    /**
     * This method will produce a DocumentId Object, from a documentKey, without a Path reference. 
     * (e.g. Useful for accessing a cached document)
     * 
     * @param documentKey The documentKey
     * @return the DocumentId
     */
    public static DocumentId createDocumentIDFromDocumentKey( String documentKey ) {
        return new DocumentId( documentKey, null );
    }

    /**
     * This method will produce a DocumentId Object, from a fileToIdex, located in a baseFolder.
     * This will relativize the fileToIndex to a given BaseFolder.
     * 
     * @param fileToIndex file to index, where we want a DocumentId for
     * @param baseFolder base directory to relitivize the indexed file.  
     * @return the DocumentId
     */
    public static DocumentId createDocumentID( Path fileToIndex, Path baseFolder ) {
        return createDocumentIDFromRelativePath( baseFolder.relativize( fileToIndex ) );
    }

    /**
     * This method will produce a DocumentId Object, from a relativized Path.
     * 
     * @param relativePath the path to calculate the documentId for
     * @return the DocumentId
     */
    public static DocumentId createDocumentIDFromRelativePath( Path relativePath ) {
        return new DocumentId( documentKeyStrategy.generateDocumentKey( relativePath ), relativePath );
    }

}
