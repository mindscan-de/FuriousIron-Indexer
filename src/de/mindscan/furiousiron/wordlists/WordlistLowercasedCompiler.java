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
package de.mindscan.furiousiron.wordlists;

import java.util.ArrayList;
import java.util.List;

import de.mindscan.furiousiron.query.ast.AndNode;
import de.mindscan.furiousiron.query.ast.EmptyNode;
import de.mindscan.furiousiron.query.ast.ExcludingNode;
import de.mindscan.furiousiron.query.ast.IncludingNode;
import de.mindscan.furiousiron.query.ast.OrNode;
import de.mindscan.furiousiron.query.ast.QueryNode;
import de.mindscan.furiousiron.query.ast.TextNode;
import de.mindscan.furiousiron.search.Search;

/**
 * This will create a lowercase but natural order compiler. Because the wordlists are stored in lowercase, to catch 
 * camelcase spellings easily. (and to save compute in comparing the strings, without tolowercase every string.
 */
@Deprecated
public class WordlistLowercasedCompiler implements WordlistCompiler {

    /** 
     * {@inheritDoc}
     */
    @Override
    public QueryNode compile( QueryNode ast, Search search ) {
        if (ast == null) {
            return new EmptyNode();
        }

        if (ast instanceof EmptyNode) {
            return new EmptyNode();
        }

        if (ast instanceof TextNode) {
            // searches in the wordlist are always lowercase.
            return new TextNode( ast.getContent().toLowerCase() );
        }

        if (ast instanceof AndNode) {
            List<QueryNode> andList = new ArrayList<>();

            if (ast.hasChildren()) {
                for (QueryNode queryNode : ast.getChildren()) {
                    andList.add( compile( queryNode, search ) );
                }
            }

            return new AndNode( andList );
        }

        if (ast instanceof OrNode) {
            List<QueryNode> orList = new ArrayList<>();

            if (ast.hasChildren()) {
                for (QueryNode queryNode : ast.getChildren()) {
                    orList.add( compile( queryNode, search ) );
                }
            }

            return new OrNode( orList );
        }

        if (ast instanceof ExcludingNode) {
            if (ast.hasChildren()) {
                for (QueryNode node : ast.getChildren()) {
                    return new ExcludingNode( compile( node, search ) );
                }
            }
            return new ExcludingNode( new EmptyNode() );
        }

        if (ast instanceof IncludingNode) {
            if (ast.hasChildren()) {
                for (QueryNode node : ast.getChildren()) {
                    return new IncludingNode( compile( node, search ) );
                }
            }
            return new IncludingNode( new EmptyNode() );
        }

        return null;
    }

}
