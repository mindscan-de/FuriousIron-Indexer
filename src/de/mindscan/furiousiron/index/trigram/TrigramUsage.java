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
 * This class will help to keep track with the successful or failed usage of a 
 * trigram for whatever reason you can think of. 
 * 
 * The original use case is to determine, whether a trigram was successful in
 * eliminating part of the list of document id's. If no reduction was achieved
 * it is reported as a Failed attempt. This helps later on to sort the successful
 * words (eliminating words) over the non-successful ones and maybe to project
 * their probable success for the later document-to-word-elimination.
 */
public class TrigramUsage {

    public enum TrigramUsageState {
        UNKNOWN, SUCCESS, FAILED
    }

    private String trigram;
    private TrigramUsageState state;

    public TrigramUsage( String trigram, TrigramUsageState state ) {
        this.trigram = trigram;
        this.state = state;
    }

    public TrigramUsage( TrigramOccurrence trigram, TrigramUsageState state ) {
        this.trigram = trigram.getTrigram();
        this.state = state;
    }

    public String getTrigram() {
        return trigram;
    }

    public TrigramUsageState getState() {
        return state;
    }

    public boolean isSuccess() {
        return state == TrigramUsageState.SUCCESS;
    }

    public boolean isFailure() {
        return state == TrigramUsageState.FAILED;
    }

    public boolean isUnknown() {
        return state == TrigramUsageState.UNKNOWN;
    }
}
