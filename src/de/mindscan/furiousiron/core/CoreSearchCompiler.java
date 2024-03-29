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
package de.mindscan.furiousiron.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.mindscan.furiousiron.core.ast.CoreQueryNode;
import de.mindscan.furiousiron.core.ast.EmptyCoreNode;
import de.mindscan.furiousiron.core.ast.TrigramsCoreNode;
import de.mindscan.furiousiron.query.ast.AndNode;
import de.mindscan.furiousiron.query.ast.EmptyNode;
import de.mindscan.furiousiron.query.ast.ExactMatchingTextNode;
import de.mindscan.furiousiron.query.ast.ExcludingNode;
import de.mindscan.furiousiron.query.ast.IncludingNode;
import de.mindscan.furiousiron.query.ast.MetaDataTextNode;
import de.mindscan.furiousiron.query.ast.OrNode;
import de.mindscan.furiousiron.query.ast.QueryNode;
import de.mindscan.furiousiron.query.ast.TextNode;

/**
 * This implements a proof of concept of a core search compiler, which will translate a
 * semantic search tree into a core search tree.
 */
public class CoreSearchCompiler {

    /**
     * Compile the given semantic query AST into a core query AST.
     *  
     * @param ast semantic ast to translate
     * 
     * @return the given optimized core search tree
     */
    public static CoreQueryNode compile( QueryNode ast ) {
        if (ast == null) {
            return new EmptyCoreNode();
        }

        if (ast instanceof EmptyNode) {
            return new EmptyCoreNode();
        }

        // Compile parsedAST into a technical AST
        if (ast instanceof TextNode) {
            // we only have lower case tri-grams indexed
            return new TrigramsCoreNode( ast.getContent().toLowerCase() );
        }

        // TODO: also support phrases
        if (ast instanceof ExactMatchingTextNode) {
            // ATTENTION: this will not work for phrases
            // FOR PHRASES we need to segment it into words and produce multiple trigrams
            // FOR PHRASES it is essentially an AND list /see andnode-processing.
            return new TrigramsCoreNode( ast.getContent().toLowerCase() );
        }

        if (ast instanceof AndNode) {
            Set<String> includedTrigrams = new HashSet<String>();
            Set<String> includedmetadataTrigrams = new HashSet<String>();

            // collect each word
            for (QueryNode queryNode : ast.getChildren()) {
                CoreQueryNode t = compile( queryNode );
                includedTrigrams.addAll( t.getTrigrams() );
                includedmetadataTrigrams.addAll( t.getMetadataTrigrams() );
            }
            List<String> wordTrigrams = includedTrigrams.stream().collect( Collectors.toList() );
            List<String> metaTrigrams = includedmetadataTrigrams.stream().collect( Collectors.toList() );

            return new TrigramsCoreNode( wordTrigrams, metaTrigrams );
        }

        if (ast instanceof OrNode) {
            // We really don't support Or nodes right now.
            throw new RuntimeException( "Or Optimization is not implemented yet" );
        }

        if (ast instanceof ExcludingNode) {
            // We don't support excluding on this level right now.
            return new EmptyCoreNode();
        }

        if (ast instanceof IncludingNode) {
            for (QueryNode queryNode : ast.getChildren()) {
                return compile( queryNode );
            }
            return new EmptyCoreNode();
        }

        if (ast instanceof MetaDataTextNode) {
            return new TrigramsCoreNode( "", ast.getContent().toLowerCase() );
        }

        throw new RuntimeException( "This Node type is not supported: " + String.valueOf( ast ) );
    }

    public static boolean hasMetaDataTextNode( QueryNode ast ) {
        if (ast == null) {
            return false;
        }

        if (ast instanceof MetaDataTextNode) {
            return true;
        }

        if (ast.hasChildren()) {
            for (QueryNode queryNode : ast.getChildren()) {
                if (hasMetaDataTextNode( queryNode )) {
                    return true;
                }
            }
        }

        return false;
    }
}
