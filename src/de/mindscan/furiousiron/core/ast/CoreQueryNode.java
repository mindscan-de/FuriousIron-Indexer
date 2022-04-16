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
package de.mindscan.furiousiron.core.ast;

import java.util.Collection;

/**
 * This is the common interface for CoreQueryNodes for the core search, based
 * on tri-grams. The Core Nodes represent a tree, to find all documents which 
 * might fit the search but without loosing documents which would satisfy the
 * search query.
 * 
 * The core search is based only on tri-grams because the index is based on 
 * tri-grams, so there are actually two kinds of searches the search, which is 
 * based on the data structure of the index and the other, what the user wants
 * to see. These are two different concepts, which can and should get tackled 
 * differently.
 * 
 * Like in a bloom filter the core search promises to deliver documents which 
 * match or may match the search, but every removed document element will for 
 * sure not match the search.
 * 
 * We also want a high rejection rate at this level, but we also don't want to
 * waste cpu-cycles.
 */
public interface CoreQueryNode {

    /**
     * Return whether this node has children.
     * 
     * @return <code>true</code> iff this node has Children
     */
    boolean hasChildren();

    /**
     * Returns the children of this node.
     * 
     * @return the collection of children.
     */
    Collection<CoreQueryNode> getChildren();

    /**
     * Returns the unique tri-grams for search. (should be in order)
     * 
     * @return A Collection of the stored trigrams
     */
    Collection<String> getTrigrams();

    /**
     * Returns the unique metadata tri-grams for search. (should be in order)
     * 
     * @return A Collection of the stored metadata trigrams
     */
    Collection<String> getMetadataTrigrams();
}
