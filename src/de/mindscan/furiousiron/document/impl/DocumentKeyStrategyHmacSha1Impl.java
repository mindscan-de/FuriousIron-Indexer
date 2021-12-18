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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import de.mindscan.furiousiron.document.DocumentKeyStrategy;

/**
 * Proof-of-concept code, to provide and test a HMAC based Strategy, but this code is not yet integrated.
 */
public class DocumentKeyStrategyHmacSha1Impl implements DocumentKeyStrategy {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    // TODO: provide the key either by constructor or via a specialized generateDocumentKey method.
    // doing it in the constructor spreads the secret key into the memory and we can't control it's
    // lifetime. Do there should be a secret key provider, providing a preinitialized mac on request, 
    // and this method should only invoke doFinal.
    // Also Mac can be reused, doFinal calculates the MAC and then restores the state after the init(Key) step

    private String key = "key";
    private int base;

    public DocumentKeyStrategyHmacSha1Impl() {
        this( 16 );
    }

    public DocumentKeyStrategyHmacSha1Impl( int base ) {
        this.base = base;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String generateDocumentKey( Path relativePath ) {
        try {
            byte[] relativePathAsBytes = relativePath.toString().getBytes( StandardCharsets.UTF_8 );

            SecretKeySpec signingKey = new SecretKeySpec( key.getBytes(), HMAC_SHA1_ALGORITHM );
            Mac mac = Mac.getInstance( HMAC_SHA1_ALGORITHM );

            mac.init( signingKey );
            return convertBase( mac.doFinal( relativePathAsBytes ) );
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException( e );
        }
        catch (InvalidKeyException e) {
            throw new IllegalArgumentException( e );
        }
    }

    private String convertBase( byte[] sha1 ) {
        BigInteger sha1bi = new BigInteger( 1, sha1 );

        switch (base) {
            case 36: {
                String alphaNum = sha1bi.toString( 36 );

                // ATTN: This happens 1/14 times (for once), 1/14*36 (for two rounds), 1/14*36*36 (for three times)
                // no need to optimize on this level
//                while (alphaNum.length() < 25) {
//                    alphaNum = "0" + alphaNum;
//                }

                return alphaNum;
            }
            case 16:
            default: {
                String sha1hex = sha1bi.toString( 16 );

                // ATTN: This happens 1/16 times (for once), 1/256 (for two rounds), 1/4096(for three times)
                // no need to optimize on this level                
//                while (sha1hex.length() < 32) {
//                    sha1hex = "0" + sha1hex;
//                }

                return sha1hex;
            }
        }
    }

}
