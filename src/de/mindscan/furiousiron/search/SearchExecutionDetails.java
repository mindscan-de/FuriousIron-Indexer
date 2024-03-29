/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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
package de.mindscan.furiousiron.search;

import java.util.Collections;
import java.util.List;

import de.mindscan.furiousiron.index.trigram.TrigramOccurrence;
import de.mindscan.furiousiron.index.trigram.TrigramUsage;

/**
 * This is basically a data object, which contains useful details, for other 
 * search steps.
 * 
 * Because this kind of data is required for metdata search and the normal 
 * search, this concept needs to be more general to cover both statistics
 * in the same way.
 */
public class SearchExecutionDetails {

    private List<TrigramOccurrence> skippedTrigrams;
    private List<TrigramUsage> trigramUsage;
    private List<TrigramOccurrence> lastSearchOccurences;

    /**
     * 
     */
    public SearchExecutionDetails() {
        skippedTrigrams = Collections.emptyList();
        trigramUsage = Collections.emptyList();
        lastSearchOccurences = Collections.emptyList();
    }

    public void setSkippedTrigramsInOptSearch( List<TrigramOccurrence> skippedTrigrams ) {
        this.skippedTrigrams = skippedTrigrams;
    }

    /**
     * @return the unprocessedTrigrams
     */
    public List<TrigramOccurrence> getSkippedTrigramsInOptSearch() {
        return skippedTrigrams;
    }

    /**
     * @param trigramUsage the trigramUsage to set
     */
    public void setTrigramUsage( List<TrigramUsage> trigramUsage ) {
        this.trigramUsage = trigramUsage;
    }

    /**
     * @return the trigramUsage
     */
    public List<TrigramUsage> getTrigramUsage() {
        return trigramUsage;
    }

    public void setLastQueryTrigramOccurrences( List<TrigramOccurrence> lastSearchOccurences ) {
        this.lastSearchOccurences = lastSearchOccurences;
    }

    public List<TrigramOccurrence> getLastQueryTrigramOccurences() {
        return lastSearchOccurences;
    }

}
