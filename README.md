# FuriousIron-Indexer

FuriousIron-Indexer is a simple indexer for java code and some releated files to that code. Since this 
is also a completely private educational project, the code might not be fit for any purpose whatsoever.

# The Idea

Because I often need to do some research in a codebase of approximately 15m loc, I want to educate myself
on how to implement a code search engine - for my later projects.

Since I also have some more advanced ideas (no spoiler right now) on what to do with this kind of search
engine, I do not want to stick to a ready to use solution right now. That would limit me to the builtin 
feature set while starting and till I learn how to implement my own ideas into their carefully designed
projects.

# MVP

For a fully working pipeline of (crawler, indexer, (*..*), search engine backend, search engine frontend)
I want this project to be running as soon as possible and then add more features, while having the whole
pipeline runing

* The indexer will index all important files in a given directory
* It will store these files, and their content, their meta-data and so on in a different directory structure
* It will construct a forward index
* It will construct different inverse indexes (search)
* It will also index meta data
* It will also create an inverse index to that meta-data
* Maybe later
 * It will construct a forward index of the inverse index
 * It will also construct an inverse index of the last said forward index (searchterm completion)

All data will be stored into simple files instead of a database. Because I like do defer unnecessary 
architectural decisions as long as they do not need attention. Please do not expect any more writing
or writeups at the moment, nor me to provide a full architecture. This is simply an undeveloped idea.

That said, please remember this is a private educational project.

# Outline

A complete search engine consists of different components.

* A Crawler (which finds, schedules documents and downloads them)
 * will be announced later
* An Indexer (documents are read in different ways and an index as well as inverse indexes are built)
 * see [FuriousIron-Indexer](https://github.com/mindscan-de/FuriousIron-Indexer) (this project)
* A Ranker (it will rank the documents)
 * will probably never announced later ;-)
* A Search Backend (retrieve search queries and searches the index and uses the ranking to order the results)
 * see [FuriousIron-SearchBackend](https://github.com/mindscan-de/FuriousIron-SearchBackend)
* A Search Frontend (reads the user queries and forwards them to a backend and reads results from backend)
 * see [FuriousIron-Frontend](https://github.com/mindscan-de/FuriousIron-Frontend)  

# License

Usage is provided under the [MIT License](http://opensource.org/licenses/mit-license.php). See LICENSE for the full details.
