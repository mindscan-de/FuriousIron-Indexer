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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 */
public class SimpleWordUtils {

    public static List<String> buildUniqueWordlist( Path fileToIndex ) throws IOException {
        List<String> allLines = Files.readAllLines( fileToIndex );

        List<List<String>> collectedWordsPerLine = allLines.stream().map( SimpleWordUtils::toLowerCase ).map( SimpleWordUtils::nonwordsplitter )
                        .filter( SimpleWordUtils::onlyNonEmpy ).collect( Collectors.toList() );

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

    public static Set<String> buildUniqueTrigrams( List<String> flatWordList ) {
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
