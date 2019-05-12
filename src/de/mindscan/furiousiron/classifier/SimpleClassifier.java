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
package de.mindscan.furiousiron.classifier;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import de.mindscan.furiousiron.document.DocumentId;
import de.mindscan.furiousiron.document.DocumentMetadata;

/**
 * 
 */
public class SimpleClassifier implements Classifier {

    /** 
     * {@inheritDoc}
     */
    @Override
    public void classify( DocumentId documentId, DocumentMetadata documentMetaData, Path fileToIndex ) {
        // some very basic classifiers, that should be something more sophisticated, 
        // but nevertheless, we want to implement a feature like this, and test it before spending too much time. 

        if (fileToIndex.toString().endsWith( ".java" )) {
            documentMetaData.addClass( "filetype", "java" );
        }

        if (fileToIndex.toString().endsWith( ".py" )) {
            documentMetaData.addClass( "filetype", "python" );
        }

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void classify( DocumentId documentId, DocumentMetadata documentMetaData, List<String> uniqueWordlist ) {
        // this is some very basic classifier to test this functionality, we will do something more sophisticated soon. 

        Map<String, String> classifierMap = documentMetaData.getClassifierMap();
        if (classifierMap.containsKey( "filetype" )) {
            if ("java".equals( classifierMap.get( "filetype" ) )) {
                // classify java content
                int isAssert = hasWords( uniqueWordlist, "assertequals", "assertthat", "asserttrue", "assertfalse" );
                int isJunit = hasWords( uniqueWordlist, "junit", "@before", "@test", "@ignore", "@beforeall", "@after", "@afterall" );
                int isMatcher = hasWords( uniqueWordlist, "hamcrest", "matchers", "equalto", "sameinstance" );
                int isMockito = hasWords( uniqueWordlist, "mockito", "mock", "spy", "thenreturn" );

                if (isAssert + isJunit + isMatcher + isMockito >= 2) {
                    documentMetaData.addClass( "unit-test", "true" );
                }
            }
        }
    }

    private int hasWords( List<String> uniqueWordlist, String... words ) {
        for (String word : words) {
            if (uniqueWordlist.contains( word )) {
                return 1;
            }
        }

        return 0;
    }

}
