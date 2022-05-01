# FuriousIron-Indexer

FuriousIron-Indexer is a simple indexer for source code and related files to that code. Since this is
also a completely personal educational project, the code might not be fit for any purpose whatsoever.

# The Idea

Because I often need to do some research in a code base of approximately 20m+ LOC, I want to educate
myself on how to implement a fast source code search engine - (for my later projects).

Since I also have some more advanced ideas (no spoiler right now) on what to do with this kind of search
engine, I do not want to stick to a ready to use solution right now. That would limit me to the built-in 
feature set while starting - until I learn how to implement my own ideas into other's carefully designed
projects.

# MVP

For a fully working pipeline of (crawler, indexer, (*..*), search engine backend, search engine frontend)
I want this project to be running as early as possible and then add more features, while having the whole
pipeline running.

* The indexer will index all important files in a given directory __[done]__
* It will store these files, and their content, their meta-data and so on, in a different directory structures __[done]__
* It will construct a forward index __[done]__
* It will construct an inverse index for every tri-gram found in the document. __[done]__
* It will construct other inverse indexes (search-search term completion))
* It will also index meta data __[done]__
* It will also create an inverse index to that meta-data __[done]__
* Maybe later
  * It will construct a forward index of the inverse index (think of a virtual file system, such that the code becomes browsable)
  * It will also construct an inverse index of the last said forward index (search term completion)

All data will be stored into simple files instead of a database. Because I like do defer unnecessary 
architectural decisions as long as they do not need attention. Please do not expect any more writing
or write-ups at the moment, nor me providing a full architecture. This is simply an underdeveloped 
idea. 

# Contributions

That said, please keep in mind, that this is a personal and educational project. That means, I will
not take any requests with respect to this source code search engine. I probably already dismissed 
your particular idea anyways for reasons.

# Nice to have

* Classifier should classify whether a java file named XYZ is a declaration of an interface XYZ
* Classifier using machine learning
* Classifier using AST based parsing
* Classifier using statistical means (like co-occurrence matrixes)
* Encode the whole text or methods or classes into vectors and use encoded search vectors to calculate similarity with methods of machine learning
* [Research] rank with machine learning - training with monte carlo simulation / monte carlo tree search
  * could be done with a cosine metrics and then order by dot product between query vector (trained) and document vector (trained) - (both calculated once) 
* collect statistics of requests and good answers -  training of ML model for ranking / or something like PCA

# Maybe have a look into

* JavaParser  https://javaparser.org/
* org.eclipse.jdt.core.*

# License

Usage is provided under the [MIT License](http://opensource.org/licenses/mit-license.php). See LICENSE for the full details.
