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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.mindscan.furiousiron.document.DocumentMetadata;

/**
 * 
 */
public class SimpleWordUtils {

    /**
     * @param documentMetaData
     * @param fileToIndex
     * @return
     */
    public static Map<String, Integer> buildTrigramTermFrequency( DocumentMetadata documentMetaData, Path fileToIndex ) throws IOException {
        List<String> allLines = Files.readAllLines( fileToIndex );

        // collect the words per line
        List<List<String>> collectedWordsPerLine = allLines.stream().map( SimpleWordUtils::toLowerCase ).map( SimpleWordUtils::nonwordsplitter )
                        .filter( SimpleWordUtils::onlyNonEmpy ).collect( Collectors.toList() );

        // collect the 
        Map<String, Integer> collectedWords = new HashMap<>();

        // collect count of words per document
        collectedWordsPerLine.stream().flatMap( List::stream ).filter( SimpleWordUtils::atLeastThreeCharsLong )
                        .forEach( word -> increaseWordCount( collectedWords, word ) );

        // now 
        Map<String, Integer> ttfMap = new HashMap<>();
        collectedWords.forEach( ( word, count ) -> increaseTTFCount( ttfMap, word, count ) );

        return ttfMap;
    }

    private static void increaseTTFCount( Map<String, Integer> ttfMap, String word, Integer count ) {
        Collection<String> uniqueTrigramsFromWord = getUniqueTrigramsFromWord( word );
        for (String trigram : uniqueTrigramsFromWord) {
            if (!ttfMap.containsKey( trigram )) {
                ttfMap.put( trigram, count );
            }
            else {
                ttfMap.put( trigram, ttfMap.get( trigram ) + count );
            }
        }
    }

    private static void increaseWordCount( Map<String, Integer> collectedWords, String word ) {
        if (!collectedWords.containsKey( word )) {
            collectedWords.put( word, 1 );
        }
        else {
            collectedWords.put( word, collectedWords.get( word ) + 1 );
        }
    }

    public static List<String> buildUniqueWordlist( DocumentMetadata documentMetaData, Path fileToIndex ) throws IOException {
        List<String> allLines = Files.readAllLines( fileToIndex );

        // collect the number of lines
        documentMetaData.setNumberOfLines( allLines.size() );

        // collect the words per line
        List<List<String>> collectedWordsPerLine = allLines.stream().map( SimpleWordUtils::toLowerCase ).map( SimpleWordUtils::nonwordsplitter )
                        .filter( SimpleWordUtils::onlyNonEmpy ).collect( Collectors.toList() );

        // collect the unique words per document
        List<String> flatUniqueWordList = collectedWordsPerLine.stream().flatMap( List::stream ).filter( SimpleWordUtils::atLeastThreeCharsLong ).distinct()
                        .collect( Collectors.toList() );

        return flatUniqueWordList;
    }

    static String toLowerCase( String string ) {
        return string.toLowerCase();
    }

    static List<String> nonwordsplitter( String string ) {
        String[] splitted = string.split( "[ /\\+\\-\\*\t\n\r\\.:;,\\(\\)\\{\\}\\[\\]]" );
        return Arrays.stream( splitted ).map( x -> x.trim() ).filter( x -> x != null && x.length() > 0 ).collect( Collectors.toList() );
    }

    static boolean onlyNonEmpy( Collection<String> x ) {
        return x.size() > 0;
    }

    static boolean atLeastThreeCharsLong( String x ) {
        return x.length() >= 3;
    }

    public static Collection<String> getUniqueTrigramsFromWord( String word ) {
        return new HashSet<>( trigramsplitter( word ) );
    }

    public static Set<String> getUniqueTrigramsFromWordList( List<String> flatWordList ) {
        List<List<String>> collectedTrigramsForEachWord = flatWordList.stream().map( SimpleWordUtils::trigramsplitter ).distinct()
                        .collect( Collectors.toList() );

        Set<String> uniqueTrigrams = collectedTrigramsForEachWord.stream().flatMap( List::stream ).collect( Collectors.toSet() );

        return uniqueTrigrams;
    }

    static List<String> trigramsplitter( String string ) {
        List<String> result = new ArrayList<>();
        for (int startIndex = 0; startIndex <= string.length() - 3; startIndex++) {
            result.add( string.substring( startIndex, startIndex + 3 ) );
        }
        return result;
    }

}
