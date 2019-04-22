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
import java.util.Deque;

/**
 * 
 */
public class SimpleFileIndexer {
    private Index index;

    /**
     * @param filesToBeIndexed provider for files which should be indexed
     * @param crawlFolder the base folder, of what files to index
     * @param indexFolder the index folder, where to put the index
     */
    public void buildIndex( Deque<Path> filesToBeIndexed, Path crawlFolder, Path indexFolder ) {

        setIndex( new Index( indexFolder ) );

        for (Path fileToIndex : filesToBeIndexed) {
            try {
                updateIndexWithSingleFile( fileToIndex, crawlFolder, indexFolder );

                // break after the first file to index
                // break;
            }
            catch (Exception ignore) {
                // intentionally left blank yet.
                // File should be appended again, if temporary problem or scheduled again for later indexing operation. 
            }
        }
    }

    private void updateIndexWithSingleFile( Path fileToIndex, Path crawlFolder, Path indexFolder ) {
        DocumentId documentId = DocumentId.createDocumentID( fileToIndex, crawlFolder );

        System.out.println( documentId.getMD5hex() );
        System.out.println( documentId.getRelativePathToCrawlingDirectory() );

        // store a copy of the document in the cache
        index.getDocumentCache().createDocumentCopy( documentId, fileToIndex );

        // index.getCache().addMetaInfo();

        // documentID is required for the cache and the additional information (metainfo)
        // metainfo
        // Path, Virtual directory entry

        // Execute pipeline, according to filecontent ...
        // Lowercase
        // Stopwords
        // trigram_filter
        // porter stemming

        // some ideas to come

//        try {
//            List<String> allLines = Files.readAllLines( fileToIndex );
//
//            for (String string : allLines) {
//                System.out.println( string );
//            }
//
//            // split into lists of words
//            List<List<String>> collect = allLines.stream().map( this::toLowerCase ).map( this::nonwordsplitter ).filter( this::onlyNonEmpy )
//                            .collect( Collectors.toList() );
//
////            for (List<String> string : collect) {
////                System.out.println( string );
////            }
//
//            // make this list of list a list of unique words
//            List<String> flatWordList = collect.stream().flatMap( List::stream ).filter( this::atLeastThreeCharsLong ).distinct()
//                            .collect( Collectors.toList() );
//
////            System.out.println( flatWordList );
//
//            // make trigrams out of every word...
//            List<List<String>> collectedTrigrams = flatWordList.stream().map( this::trigramsplitter ).distinct().collect( Collectors.toList() );
////            for (List<String> string : collectedTrigrams) {
////                System.out.println( string );
////            }
//
//            // flatWordList.stream().map( mapper )
//            Set<String> uniqueTrigrams = collectedTrigrams.stream().flatMap( List::stream ).collect( Collectors.toSet() );
//
//            System.out.println( uniqueTrigrams );
//
//        }
//        catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    }

//    private boolean atLeastThreeCharsLong( String x ) {
//        return x.length() >= 3;
//    }
//
//    private boolean onlyNonEmpy( Collection<String> x ) {
//        return x.size() > 0;
//    }
//
//    private String toLowerCase( String string ) {
//        return string.toLowerCase();
//    }
//
//    private List<String> nonwordsplitter( String string ) {
//        String[] splitted = string.split( "[ /\\+\\-\\*\t\n\r\\.:;,\\(\\)\\{\\}\\[\\]]" );
//        return Arrays.stream( splitted ).map( x -> x.trim() ).filter( x -> x != null && x.length() > 0 ).collect( Collectors.toList() );
//    }
//
//    private List<String> trigramsplitter( String string ) {
//        List<String> result = new ArrayList<>();
//        for (int startIndex = 0; startIndex <= string.length() - 3; startIndex++) {
//            result.add( string.substring( startIndex, startIndex + 3 ) );
//        }
//        return result;
//    }

    public Index getIndex() {
        return index;
    }

    public void setIndex( Index index ) {
        this.index = index;
    }
}
