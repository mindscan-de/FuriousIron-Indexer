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
package de.mindscan.furiousiron.index.trigram;

/**
 * This class contains a trigram and its count of occurrences. In case of the trigram
 * index occurence means, in how many documents this particular trigram occurrs. But
 * this class may also be used to count the number of word occurrences as well.
 * 
 * Actually a word could be seen as an ngram-occurrence.
 */
public class TrigramOccurrence {

    private String trigram;
    private long occurrenceCount;

    public TrigramOccurrence( String trigram, long occurrenceCount ) {
        this.trigram = trigram;
        this.occurrenceCount = occurrenceCount;
    }

    /**
     * @return the occurenceCount
     */
    public long getOccurenceCount() {
        return occurrenceCount;
    }

    /**
     * @return the trigram
     */
    public String getTrigram() {
        return trigram;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "'" + trigram + "': " + occurrenceCount;
    }
}
