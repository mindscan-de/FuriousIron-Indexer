/**
 * 
 * MIT License
 *
 * Copyright (c) 2019, 2021 Maxim Gansert, Mindscan
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
package de.mindscan.furiousiron.index.trigram;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.Gson;

import de.mindscan.furiousiron.index.trigram.model.TrigramDocumentCountJsonModel;
import de.mindscan.furiousiron.index.trigram.model.TrigramIndexJsonModel;

/**
 * 
 */
public class TrigramIndex {
    // This should be configurable.
    public final static int MAX_TRIGRAMS_PER_REFERENCEFILE = 3720;

    private static final String TRIGRAM_REFERENCE_SUFFIX = ".reference";
    private static final String TRIGRAM_COUNT_SUFFIX = ".reference_count";

    private final String trigram;
    private int indexGeneration;

    private Set<String> relatedDocuments;
    private int relatedDocumentsCounter;
    private long globalRelatedDocumentsCounter;
    private final Path trigramsBasePath;

    /**
     * @param trigram
     * @param indexGeneration
     * @param trigramsBasePath 
     */
    public TrigramIndex( String trigram, int indexGeneration, Path trigramsBasePath ) {
        this.trigram = trigram;
        this.indexGeneration = indexGeneration;
        this.trigramsBasePath = trigramsBasePath;
        this.relatedDocuments = new TreeSet<>();
        this.relatedDocumentsCounter = 0;
        this.globalRelatedDocumentsCounter = 0L;
    }

    /**
     * Add the document key to a list of related documents to the current trigram.
     * @param documentKey the document key to add to this index
     */
    public void add( String documentKey ) {
        relatedDocuments.add( documentKey );
        relatedDocumentsCounter++;
        globalRelatedDocumentsCounter++;

        if (relatedDocumentsCounter >= MAX_TRIGRAMS_PER_REFERENCEFILE) {
            saveInternal();
        }
    }

    /**
     *
     */
    public void save() {
        saveInternal();
    }

    private synchronized void saveInternal() {
        // prevent double saving or saving empty documents - in case of add / save concurrency.
        if (relatedDocumentsCounter == 0) {
            return;
        }

        final TrigramIndexJsonModel model = new TrigramIndexJsonModel( relatedDocuments, indexGeneration, trigram );
        final TrigramDocumentCountJsonModel count = new TrigramDocumentCountJsonModel( trigram, globalRelatedDocumentsCounter );

        indexGeneration++;
        relatedDocuments = new TreeSet<>();
        relatedDocumentsCounter = 0;

        // put that save action and all unnecessary path calculations into a thread pool, 
        // no need that other word can't be indexed, because of someone's save action... 

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Path trigramsPath = TrigramSubPathCalculator.getPathForTrigram( trigramsBasePath, trigram,
                                "." + model.getIndexGeneration() + TRIGRAM_REFERENCE_SUFFIX );
                createTargetDirectoryIfNotExist( trigramsPath.getParent() );

                // write content of one part of the index
                try (BufferedWriter writer = Files.newBufferedWriter( trigramsPath, StandardCharsets.UTF_8 )) {
                    Gson gson = new Gson();
                    writer.write( gson.toJson( model ) );
                }
                catch (IOException e) {
                    System.out.println( String.format( "saving file: '%s' caused this error...", trigramsPath ) );
                    e.printStackTrace();
                }

                // write+overwrite the global documents counter as well
                Path trigramCountPath = TrigramSubPathCalculator.getPathForTrigram( trigramsBasePath, trigram, TRIGRAM_COUNT_SUFFIX );

                try (BufferedWriter writer = Files.newBufferedWriter( trigramCountPath, StandardCharsets.UTF_8 )) {
                    Gson gson = new Gson();
                    writer.write( gson.toJson( count ) );
                }
                catch (IOException e) {
                    System.out.println( String.format( "saving file: '%s' caused this error...", trigramCountPath ) );
                    e.printStackTrace();
                }

            }
        };

        // TODO: put into task thread-pool (deque) for really saving indexing time.
        runnable.run();
    }

    public String getTrigram() {
        return trigram;
    }

    public int getIndexGeneration() {
        return indexGeneration;
    }

    private void createTargetDirectoryIfNotExist( Path path ) {
        if (!Files.isDirectory( path )) {
            try {
                Files.createDirectories( path );
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
