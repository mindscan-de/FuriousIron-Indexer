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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 
 */
public class DocumentMetadataFactory {

    /**
     * @param documentId The documentId 
     * @param fileToIndex The file which should be indexed. This file is given for reference, so some file details can be collected.
     * @return
     */
    public static DocumentMetadata createDocumentMetadata( DocumentId documentId, Path fileToIndex ) {

        String documentLocation = documentId.getDocumentLocation();
        String documentSimpleName = fileToIndex.getFileName().toString();
        String documentKey = documentId.getDocumentKey();

        DocumentMetadata documentMetadata = new DocumentMetadata( documentKey, documentLocation, documentSimpleName );

        try {
            // Map<String, Object> attributes = Files.readAttributes( fileToIndex, "*", LinkOption.NOFOLLOW_LINKS );

            // The crawler already provides BasicFileAttributes, which can be used and may prevent another systemcall.
            // - size
            // lastModified
            // lastAccess

            documentMetadata.setFileSize( Files.size( fileToIndex ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: collect some file statistics / information ( filetype, contenttype, last modified date, created date, ...)
        // TODO: date of indexing
        // TODO: number of unique words
        // TODO: number of unique trigrams in document
        // TODO: get/create documentid of parent directory(ies). -> source code navigation

        return documentMetadata;
    }

}
