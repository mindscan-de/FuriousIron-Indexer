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
package de.mindscan.furiousiron.classifier;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import de.mindscan.furiousiron.classifier.impl.CoOccurrenceMatrixImpl;

/**
 * Text is usually encoded in different formats. This class should provide a list of labels, for a given file,
 * which can then be used to convert the data correctly before building an index, the document can remain in its 
 * original form, but we can add these content information to the meta index, such that this content can be 
 * transformed to utf-8 if a non-raw cached copy is requested. 
 * 
 * This is a classifier, which should answer, what encoding (ascii, utf-8, utf-16, cp1251) is given, what the language 
 * content is, e.g. chinese, japanese, korean, russian. What kind of new line encoding (linux, windows, mac), is it 
 * high entropy? (maybe zipped or encrypted?)
 * 
 * How this will be done?
 *   * we read that file and we calculate the co-occurrence matrix for the first few kilobytes.
 *   * we can either train a machine learning classifier operating on an image or use a handcrafted algorithms
 *   * we can do both, by writing a handcrafted algorithm and then train a image classifier to do this job
 *   * may be we can train a support vector machine which can do this for us.
 *   
 * This information can later be used to implement a conversion from codepage to UTF-8, to index the documents 
 * properly and make them findable using utf-8 encodings.
 */
public class EncodingClassifier {

    /**
     * 
     */
    public EncodingClassifier() {
    }

    public Map<String, Boolean> classify( Path path, String tofile ) {

        CoOccurrenceMatrixImpl com = new CoOccurrenceMatrixImpl();

        try {
            byte[] readAllBytes;
            readAllBytes = Files.readAllBytes( path );
            com.update( readAllBytes );
            int[][] matrix = com.getMatrix();
            // just for understanding the results graphically...
            saveAsImage( matrix, tofile );
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Map<String, Boolean> classiferMap = new HashMap<>();

        // visually develop the rules for content classification.
        // first x second
        // Q1:(00-7f)x(00-7f)
        // Q2:(00-7f)x(80-ff)
        // Q3:(80-ff)x(00-7f)
        // Q4:(80-ff)x(80-ff)

        // maybe we should train a CNN for each of these quadrants 128x128 instead of one 256x256
        // by using with dilated convolutions to keep the number of layers low and then concatenate 
        // the results of the 4 quadrants with a 2 layer FC network to calculate the common classes
        // representation and a final decding layer for multi-class-predictions. 

        // 0a0a -> linux encodings
        // 0a20 -> linux encodings
        // mac and windows encodings
        // 0d0a > 0a0d  && 0a20 > 0
        // 0a0d > 0d0a  // 0d20 > 0

        // feff == 1 utf8

        return classiferMap;
    }

    /**
     * @param matrix
     */
    private void saveAsImage( int[][] matrix, String tofile ) {
        // TODO Auto-generated method stub

        int yLength = matrix.length;
        int xLength = matrix[0].length;
        BufferedImage b = new BufferedImage( xLength, yLength, BufferedImage.TYPE_INT_RGB );

        for (int y = 0; y < yLength; y += 16) {
            for (int x = 0; x < xLength; x += 8) {
                int rgb = 0x440000;
                b.setRGB( x, y, rgb );
            }
        }

        for (int y = 0; y < yLength; y += 8) {
            for (int x = 0; x < xLength; x += 16) {
                int rgb = 0x440000;
                b.setRGB( x, y, rgb );
            }
        }

        for (int y = 0; y < yLength; y++) {
            for (int x = 0; x < xLength; x++) {
                int value = matrix[y][x];
                if (value != 0) {
                    int times3 = value * 3;
                    int rgb = times3 > 255 ? 0xffffff : (times3 << 16) + (times3 << 8) + (times3);
                    b.setRGB( x, y, rgb );
                }
            }
        }
        try {
            System.out.println( "result:" + Boolean.toString( ImageIO.write( b, "png", new File( tofile ) ) ) );
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println( "end" );

    }
}
