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

public class WordScore {
    private String word;
    private int score;

    public WordScore( String word ) {
        this( word, 1 );
    }

    public WordScore( String word, int score ) {
        this.word = word;
        this.score = score;
    }

    /**
     * @param i
     */
    public void increase( int i ) {
        this.score += i;
    }

    public void increase( long i ) {
        this.score += (int) i;
    }

    /**
     * @param i
     */
    public void decrease( int i ) {
        this.score -= i;
    }

    public void decrease( long i ) {
        this.score -= (int) i;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "'" + word + "': " + score;
    }
}