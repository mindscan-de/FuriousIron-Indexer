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
package de.mindscan.furiousiron.indexer;

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

    public static DocumentId createDocumentID( Path fileToIndex, Path baseCrawlerFolder ) {
        return createDocumentID( baseCrawlerFolder.relativize( fileToIndex ) );
    }

    public static DocumentId createDocumentID( Path relativePathToCrawlingDirectory ) {
        try {
            // TODO: calculate universal path and use this for calculation of Path digest instead.

            byte[] relativePathAsBytes = relativePathToCrawlingDirectory.toString().getBytes( "UTF-8" );

            MessageDigest md5sum = MessageDigest.getInstance( "MD5" );
            byte[] md5 = md5sum.digest( relativePathAsBytes );

            BigInteger md5bi = new BigInteger( 1, md5 );
            String md5hex = md5bi.toString( 16 );

            while (md5hex.length() < 32) {
                md5hex = "0" + md5hex;
            }

            return new DocumentId( md5hex, relativePathToCrawlingDirectory );
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    DocumentId( String md5hex, Path relativePathToCrawlingDirectory ) {
        this.md5hex = md5hex;
        this.relativePathToCrawlingDirectory = relativePathToCrawlingDirectory;
    }

    public String getMd5hex() {
        return md5hex;
    }

    public Path getRelativePathToCrawlingDirectory() {
        return relativePathToCrawlingDirectory;
    }

}
