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
package de.mindscan.furiousiron.index.cache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.mindscan.furiousiron.document.DocumentId;

/**
 * 
 */
public class CachingPathUtils {

//     public static final int NUMBER_OF_DOCUMENT_ID_LAYERS = 1;

    public static Path getDocumentPath( Path basePath, DocumentId documentId, String fileSuffix ) {
        return getDocumentPathFromMD5( basePath, documentId.getDocumentKey(), fileSuffix );
    }

    public static Path getDocumentPathFromMD5( Path basePath, String documentIdMD5, String fileSuffix ) {
        String filename = documentIdMD5 + fileSuffix;
        String firstLayer = documentIdMD5.substring( 0, 2 );

        return basePath.resolve( Paths.get( firstLayer, filename ) );
//        switch (NUMBER_OF_DOCUMENT_ID_LAYERS) {
//            case 1:
//                return basePath.resolve( Paths.get( firstLayer, filename ) );
//            case 2:
//            default:
//                String secondLayer = documentId.getMD5hex().substring( 2, 4 );
//                return basePath.resolve( Paths.get( firstLayer, secondLayer, filename ) );
//        }
    }

    /**
     * @param wordlistDocumentPath
     */
    static void createTargetDirectoryIfNotExist( Path documentPath ) {
        Path targetDirectoryPath = documentPath.getParent();
        if (!Files.isDirectory( targetDirectoryPath )) {
            try {
                Files.createDirectories( targetDirectoryPath );
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
