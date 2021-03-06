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
package de.mindscan.furiousiron.wordlists.wordorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mindscan.furiousiron.index.trigram.TrigramUsage;
import de.mindscan.furiousiron.indexer.SimpleWordUtils;

/**
 * 
 */
public class TrigramPenaltyStrategy {
    public Collection<String> buildWordlist( Collection<String> wordlist, Collection<TrigramUsage> usage ) {

        // no word list -> nothing to do here
        if (wordlist.isEmpty()) {
            return wordlist;
        }

        // create a map of the wordlist, and init with each word with a defaultscore
        Map<String, WordScore> wordMapScore = new HashMap<>();
        wordlist.stream().forEach( word -> initWordScoce( wordMapScore, word ) );

        // create a map of usages
        Map<String, TrigramUsage> usageMap = new HashMap<>();
        usage.stream().forEach( ngram -> usageMap.put( ngram.getTrigram(), ngram ) );

        for (String word : wordlist) {
            Collection<String> trigramsForWord = SimpleWordUtils.getUniqueTrigramsFromWord( word );

            long unused = trigramsForWord.stream().filter( trigram -> !usageMap.containsKey( trigram ) ).count();
            long successful = trigramsForWord.stream().filter( trigram -> usageMap.containsKey( trigram ) && usageMap.get( trigram ).isSuccess() ).count();
            long failure = trigramsForWord.stream().filter( trigram -> usageMap.containsKey( trigram ) && usageMap.get( trigram ).isFailure() ).count();

            WordScore wordScore = wordMapScore.get( word );

            if (successful + failure == 0) {
                // neither success nor failure - this word was not used
                wordScore.decrease( unused );
            }
            else {
                wordScore.increase( successful );
                wordScore.decrease( failure );

                if (unused == 0) {
                    // all parts were used
                    if (successful >= failure) {
                        wordScore.increase( 2 );
                    }
                    else {
                        wordScore.decrease( 1 );
                    }
                }
                else {
                    // someparts were used some unused
                    if (failure == 0) {
                        wordScore.increase( (unused + 1) / 2 );
                    }
                    else {
                        if (successful >= failure) {
                            wordScore.increase( 2 );
                        }
                        else {
                            wordScore.decrease( (unused + 1) / 2 );
                        }
                    }
                }
            }
        }

        List<String> wordlistToSort = new ArrayList<>( wordlist );
        wordlistToSort.sort( Comparator.comparingInt( word -> extracted( (String) word, wordMapScore ) ).reversed() );

        for (String word : wordlistToSort) {
            System.out.println( wordMapScore.get( word ).toString() );
        }

        return wordlistToSort;
    }

    private void initWordScoce( Map<String, WordScore> wordMapScore, String word ) {
        wordMapScore.put( word, new WordScore( word ) );
    }

    private int extracted( String word, Map<String, WordScore> wordMapScore ) {
        return wordMapScore.get( word ).getScore();
    }

}
