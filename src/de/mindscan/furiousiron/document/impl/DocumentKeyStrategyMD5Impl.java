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
package de.mindscan.furiousiron.document.impl;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.mindscan.furiousiron.document.DocumentKeyStrategy;

/**
 * 
 */
public class DocumentKeyStrategyMD5Impl implements DocumentKeyStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateDocumentKey( Path relativePath ) {
        try {
            byte[] relativePathAsBytes = relativePath.toString().getBytes( StandardCharsets.UTF_8 );

            MessageDigest md5sum = MessageDigest.getInstance( "MD5" );
            byte[] md5 = md5sum.digest( relativePathAsBytes );
            return convertToHex( md5 );
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String convertToHex( byte[] md5 ) {
        BigInteger md5bi = new BigInteger( 1, md5 );
        String md5hex = md5bi.toString( 16 );

        while (md5hex.length() < 32) {
            md5hex = "0" + md5hex;
        }
        return md5hex;
    }

    @SuppressWarnings( "unused" )
    private String convertToHex2( byte[] md5 ) {
        BigInteger md5bi = new BigInteger( 1, md5 );
        return String.format( "%032x", md5bi );
    }

}
