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
package de.mindscan.furiousiron.classifier.impl;

/**
 * 
 */
public class CoOccurrenceMatrixImpl {

    // 256kbyte
    private int[][] com = new int[256][256];

    /**
     * 
     */
    public CoOccurrenceMatrixImpl() {
    }

    public void reset() {
        com = new int[256][256];
    }

    public void update( byte[] byteSequence ) {
        if (byteSequence.length <= 2) {
            return;
        }

        for (int i = 0; i < byteSequence.length - 1; i++) {
            byte first = byteSequence[i];
            byte second = byteSequence[i + 1];

            com[first][second]++;
        }

        // TODO: save the last byte and add it next time as first byte to the very first byte to the matrix... But maybe making an error here is just negligible, if
        // the sequences are small enough / actually the result just must be good enough for a classification.
    }

    public int[][] getMatrix() {
        return com;
    }
}
