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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import de.mindscan.furiousiron.document.DocumentId;

/**
 * This class implements a simple document cache, for the original documents. The original file structure is 
 * not preserved, instead it is built from the document id. The file structure / file name and so on, should 
 * be obtained from the metadata / metainformation of the document. 
 */
public class DocumentCache {

    /**
     * folder in index Folder, where the 'downloaded' documents should be cached. 
     */
    private static final String CACHED_DOCUMENTS_FOLDER = "cachedDocuments";

    /**
     * file suffix for files containing copies of the original content in the cache folder
     */
    public final static String CACHED_FILE_SUFFIX = ".originalContent";

    private Path documentCacheFolder;

    /**
     * C'tor 
     * @param indexFolder path of folder, where to store the cached Documents. The documents 
     */
    public DocumentCache( Path indexFolder ) {
        // this.indexFolder = indexFolder;
        this.documentCacheFolder = indexFolder.resolve( CACHED_DOCUMENTS_FOLDER );
    }

    /**
     * Create a copy of the content of the file given via fileToIndex. The content itself is 
     * stored in a file accessible by the documentId. A file with the same documentId will
     * overwrite (replace) the former file content.  
     * 
     * @param documentId the documentId which this document should be stored in the cache
     * @param fileToIndex the path to the document to store
     */
    public void createDocumentCopy( DocumentId documentId, Path fileToIndex ) {
        Path documentTargetFilePath = CachingPathUtils.buildCachePathFromDocumentId( documentCacheFolder, documentId, CACHED_FILE_SUFFIX );

        // TODO: create cache directory structure beforehand.
        // can be enforced completely beforehand, so this calculation doesn't need 
        // to be part of each document copy operation 
        // it would be simple to create either 256 or 65536 folders / shards beforehand
        CachingPathUtils.createTargetDirectoryIfNotExist( documentTargetFilePath );

        try {
            Files.copy( fileToIndex, documentTargetFilePath, StandardCopyOption.REPLACE_EXISTING );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getContentAsStream( DocumentId documentId ) throws IOException {
        Path documentContentPath = CachingPathUtils.buildCachePathFromDocumentId( documentCacheFolder, documentId, CACHED_FILE_SUFFIX );

        if (Files.exists( documentContentPath )) {
            return Files.newInputStream( documentContentPath, StandardOpenOption.READ );
        }
        else {
            String content = "Sorry, the document you requested, is not present in the cache.";
            return new ByteArrayInputStream( content.getBytes( StandardCharsets.UTF_8 ) );
        }
    }

}
