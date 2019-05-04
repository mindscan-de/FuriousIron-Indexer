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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;

import de.mindscan.furiousiron.document.DocumentId;
import de.mindscan.furiousiron.document.DocumentMetadata;

/**
 * 
 */
public class MetadataCache {

    /**
     * folder in index Folder, where the 'metadata' documents should be cached. 
     */
    private static final String CACHED_METADATA_FOLDER = "cachedMetadata";

    /**
     * file suffix for files containing the metadata of the original document content 
     */
    public final static String METADATA_FILE_SUFFIX = ".metadata";

    private Path cacheFolder;

    /**
     * @param indexFolder
     */
    public MetadataCache( Path indexFolder ) {
        this.cacheFolder = indexFolder.resolve( CACHED_METADATA_FOLDER );
    }

    public void addDocumentMetadata( DocumentId documentId, DocumentMetadata documentMetaData ) {
        Path metadataDocumentPath = CachingPathUtils.getDocumentPath( cacheFolder, documentId, METADATA_FILE_SUFFIX );

        CachingPathUtils.createTargetDirectoryIfNotExist( metadataDocumentPath );

        try (BufferedWriter writer = Files.newBufferedWriter( metadataDocumentPath, StandardCharsets.UTF_8 )) {
            Gson gson = new Gson();
            writer.write( gson.toJson( documentMetaData ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public DocumentMetadata loadMetadata( String documentId ) {
        Path metadataDocumentPath = CachingPathUtils.getDocumentPathFromMD5( cacheFolder, documentId, METADATA_FILE_SUFFIX );

        try (BufferedReader jsonBufferedReader = Files.newBufferedReader( metadataDocumentPath, StandardCharsets.UTF_8 )) {
            Gson gson = new Gson();
            DocumentMetadata result = gson.fromJson( jsonBufferedReader, DocumentMetadata.class );
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
