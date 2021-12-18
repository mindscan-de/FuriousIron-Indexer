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
import java.util.HashSet;
import java.util.Set;

import de.mindscan.furiousiron.document.DocumentId;

/**
 * 
 */
public class CachingPathUtils {

    public static Set<Path> directoryAlreadyExists = new HashSet<>();

//     public static final int NUMBER_OF_DOCUMENT_ID_LAYERS = 1;

    public static Path buildCachePathFromDocumentId( Path basePath, DocumentId documentId, String fileSuffix ) {
        return buildCachePathFromDocumentKey( basePath, documentId.getDocumentKey(), fileSuffix );
    }

    public static Path buildCachePathFromDocumentKey( Path basePath, String documentKey, String indexFileSuffix ) {
        String filename = documentKey + indexFileSuffix;
        String firstLayer = documentKey.substring( 0, 2 );

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
    static void createTargetDirectoryIfNotExist( Path targetDocumentPath ) {
        Path targetDirectoryPath = targetDocumentPath.getParent();

        if (directoryAlreadyExists.contains( targetDirectoryPath )) {
            return;
        }

        if (!Files.isDirectory( targetDirectoryPath )) {
            try {
                Files.createDirectories( targetDirectoryPath );
                directoryAlreadyExists.add( targetDirectoryPath );
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            directoryAlreadyExists.add( targetDirectoryPath );
        }
    }

}
