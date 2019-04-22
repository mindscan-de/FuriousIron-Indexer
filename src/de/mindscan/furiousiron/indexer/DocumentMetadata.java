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

import java.nio.file.Path;

/**
 * This class contains information about a given document, while indexing. It also contains the truth about the filename and the paths.
 */
public class DocumentMetadata {

    private final String documentId;

    private final String relativePath;
    private final String simpleFilename;

    /**
     * @param documentId The documentId 
     * @param fileToIndex The file which should be indexed. This file is given for reference, so some file details can be collected.
     * @return
     */
    public static DocumentMetadata createDocumentMetadata( DocumentId documentId, Path fileToIndex ) {

        String relativePath = documentId.getRelativePathToCrawlingDirectory().toString();
        String simpleFilename = fileToIndex.getFileName().toString();
        String documentMD5 = documentId.getMD5hex();

        DocumentMetadata documentMetadata = new DocumentMetadata( documentMD5, relativePath, simpleFilename );

        // TODO: collect some file statistics / information ( filelength, filetype, contenttype, last modified date, created date, ...)
        // TODO: date of indexing
        // TODO: number of unique words
        // TODO: number of unique trigrams in document
        // TODO: get/create documentid of parent directory(ies). -> source code navigation

        return documentMetadata;
    }

    /**
     * @param relativePath 
     * @param documentId 
     */
    DocumentMetadata( String documentId, String relativePath, String simpleFilename ) {
        this.documentId = documentId;
        this.relativePath = relativePath;
        this.simpleFilename = simpleFilename;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getSimpleFilename() {
        return simpleFilename;
    }

}
