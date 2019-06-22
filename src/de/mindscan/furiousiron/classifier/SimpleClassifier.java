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
 * This implements a way to classify the document according to a few parameters, like the documentId, 
 * its meta data, the filename or the words found in the document.
 * 
 * Really, this implementation is a very useless and non intelligent way to classify something, 
 * but might be helpful for the very few iterations it might live, till there is an urgent need 
 * to recreate this Classifier thing with a more sophisticated approach. 
 */
public class SimpleClassifier implements Classifier {

    /** 
     * {@inheritDoc}
     */
    @Override
    public void classify( DocumentId documentId, DocumentMetadata documentMetaData, Path fileToIndex ) {
        // some very basic classifiers, that should be something more sophisticated, 
        // but nevertheless, we want to implement a feature like this, and test it before spending too much time. 
        String fileNameAsString = fileToIndex.toString();

        if (fileNameAsString.endsWith( ".java" )) {
            documentMetaData.addClass( "filetype", "java" );
        }
        else if (fileNameAsString.endsWith( ".py" )) {
            documentMetaData.addClass( "filetype", "python" );
        }
        else if (fileNameAsString.endsWith( ".xtend" )) {
            documentMetaData.addClass( "filetype", "xtend" );
        }
        else if (fileNameAsString.endsWith( ".MF" )) {
            documentMetaData.addClass( "filetype", "manifest" );
        }
        else if (fileNameAsString.endsWith( ".txt" ) || fileNameAsString.endsWith( ".text" )) {
            documentMetaData.addClass( "filetype", "text" );
        }
        else if (fileNameAsString.endsWith( ".md" ) || fileNameAsString.endsWith( ".MD" )) {
            documentMetaData.addClass( "filetype", "markdown" );
        }
        else if (fileNameAsString.endsWith( ".xml" ) || fileNameAsString.endsWith( ".pom" )) {
            documentMetaData.addClass( "filetype", "xml" );
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void classify( DocumentId documentId, DocumentMetadata documentMetaData, List<String> uniqueWordlist ) {
        // this is some very basic classifier to test this functionality, we will do something more sophisticated soon. 

        Map<String, String> classifierMap = documentMetaData.getClassifierMap();
        if (documentMetaData.containsClass( "filetype" )) {
            if ("java".equals( classifierMap.get( "filetype" ) )) {
                // classify java content
                int isAssert = hasWords( uniqueWordlist, "assertequals", "assertthat", "asserttrue", "assertfalse" );
                int isJunit = hasWords( uniqueWordlist, "junit", "@before", "@test", "@ignore", "@beforeall", "@after", "@afterall", "@beforeclass" );
                int isMatcher = hasWords( uniqueWordlist, "hamcrest", "matchers", "equalto", "sameinstance" );
                int isMockito = hasWords( uniqueWordlist, "mockito", "mock", "spy", "thenreturn" );

                if (isAssert + isJunit + isMatcher + isMockito >= 2) {
                    documentMetaData.addClass( "unit-test", "true" );
                }

                // TODO: classify an interface
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
