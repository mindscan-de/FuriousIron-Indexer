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

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 */
public class DocumentId {

    private String md5hex;
    private Path relativePathToCrawlingDirectory;

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

            return new DocumentId( convertToHex( md5 ), relativePathToCrawlingDirectory );
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

    DocumentId( String md5hex, Path relativePathToCrawlingDirectory ) {
        this.md5hex = md5hex;
        this.relativePathToCrawlingDirectory = relativePathToCrawlingDirectory;
    }

    public String getMD5hex() {
        return md5hex;
    }

    public Path getRelativePathToCrawlingDirectory() {
        return relativePathToCrawlingDirectory;
    }

}
