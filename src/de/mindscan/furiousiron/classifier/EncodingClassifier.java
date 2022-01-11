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

/**
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

}
