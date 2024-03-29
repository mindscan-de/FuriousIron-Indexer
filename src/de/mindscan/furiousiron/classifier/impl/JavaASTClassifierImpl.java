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
package de.mindscan.furiousiron.classifier.impl;

import java.nio.file.Path;
import java.util.List;

import de.mindscan.furiousiron.classifier.Classifier;
import de.mindscan.furiousiron.document.DocumentId;
import de.mindscan.furiousiron.document.DocumentMetadata;

/**
 * The idea, is to provide a classifier for Java files based on the Parse-AST. A Java parser is more computationally
 * expensive. 
 * 
 * TODO: The current state is, that this idea needs to be developed further.
 */
public class JavaASTClassifierImpl implements Classifier {

    /** 
     * {@inheritDoc}
     */
    @Override
    public void classify( DocumentId documentId, DocumentMetadata documentMetaData, Path fileToIndex ) {
        // class
        // interface
        // enumeration
        // abstract class
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void classify( DocumentId documentId, DocumentMetadata documentMetaData, List<String> uniqueWordlist ) {

    }

}
