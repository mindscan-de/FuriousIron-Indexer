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

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public static DocumentId createDocumentIDFromMD5( String md5sum ) {
        return new DocumentId( md5sum, null );
    }

    public static DocumentId createDocumentID( Path fileToIndex, Path baseCrawlerFolder ) {
        return createDocumentIDFromRelativePath( baseCrawlerFolder.relativize( fileToIndex ) );
    }

    public static DocumentId createDocumentIDFromRelativePath( Path relativePathToCrawlingDirectory ) {
        try {
            byte[] relativePathAsBytes = relativePathToCrawlingDirectory.toString().getBytes( "UTF-8" );

            MessageDigest md5sum = MessageDigest.getInstance( "MD5" );
            byte[] md5 = md5sum.digest( relativePathAsBytes );

            return new DocumentId( DocumentIdFactory.convertToHex( md5 ), relativePathToCrawlingDirectory );
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String convertToHex( byte[] md5 ) {
        BigInteger md5bi = new BigInteger( 1, md5 );
        String md5hex = md5bi.toString( 16 );

        while (md5hex.length() < 32) {
            md5hex = "0" + md5hex;
        }
        return md5hex;
    }

    public static String convertToHex2( byte[] md5 ) {
        BigInteger md5bi = new BigInteger( 1, md5 );
        return String.format( "%032x", md5bi );
    }

}
