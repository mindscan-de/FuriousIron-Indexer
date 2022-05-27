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

# MVP+

This project requires to build and maintain the inverse index on an SSD-hard-drive, and SSDs are 
reasonably cheap as well nowadays. Much of the performance is gained by reading from an SSD. This 
project doesn't implement any RAM caches for performance gains. Most of the gains are achieved by 
culling useless operations on the data. 

That means that most of the gains are achieved by neither spending compute nor I/O. In a project
where virtually everything is prematurely cached, you wouldn't notice, that you are spending use-
less cycles. On the other hand, if I feel an urge to speed things up, I still can trade memory 
for performance wherever it may be useful. If you start by thinking what can be omitted without
sacrificing the result, you will obtain much simpler and more efficient solutions.

## HFB-Filter compiler and HFB Filtering for Metadata filtering [done]

I will test my hash free bloom filter implementation for filtering the document IDs. Instead of
loading a lot of document ids from disk, I only load the first candidate list of document IDs. 
For every remaining trigram a hash free bloom filter will be applied. This should reduce I/O, 
reduce parsing time for JSON files and provide a very fast testing strategy. That should speed 
up the filtering step by at least between one or two orders of magnitude.

First we need a new index / cache for hfb filters, then the compiler step must be implemented,
from trigrams to hfb filters. Then the algorithm Search#collectDocumentIdsForMetadataTrigramsOpt
must be implemented to collect and apply the hfb filters in question. Finally SearchQueryExecutorV2
needs to be adapted and the new performance needs to be measured and evaluated.

The number of filter bank data to save is set down to three and the filter data banks written
are optimized for efficiency. *Speedup* in filtering-phase (search) *is about ten fold*, and the 
*hfb-index size is about a tenth of the trigram index* from which it is compiled.

## HFB-Filters and continuous index self-optimization

* Compile the inverse index to HFB-Filters
  * instead of keeping the list of document IDs and will also increase the overall performance (very likely by a factor of 10 to 100).
  * completely transfer to filter only approach? or still hybrid approach? 
  
* then use HFB-Filters to filter search candidates efficiently
  * instead of reading files encoded in json-format from a disk, parsing them and then preparing 
    a single HashSet containing Strings for a single use, I would like to test my idea of a 
    high-performance high-optimized Bloom filter
* and also use partial search results for the construction and caching of new HFB-Filters, such that the search index will optimize and adapt itself for each use over time
* storing partial results and full results as a HFB filter will also increase the overall system
  performance.

## Query Plan Compiler 3search, filterw,

* build a query plan and and a query plan optimizer, which can be executed in parallel and/or distributed.

## Incremental Preview Extension

* currently only the best ranked 35 are actually previewed. Each time the query is repeated, another
  K previews are calculated.
  
## Second-Pass Filtering and Ranking

* in case of of phrase searches, the documents containing the exact phrase should be on top of the results, 
  and must occur in the preview.

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
