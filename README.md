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
I want this project to be running as soon as possiblem and then add more features, while having the whole
pipeline runing

* The indexer will index all important files in a given directory
* It will store these files, and their content, their meta-data and so on in a different directory structure
* It will construct a forward index
* It will construct different inverse indexes (search)
* It will also index meta data
* It will also create an inverse index to that meta-data
* Maybe later
** It will construct a forward index of the inverse index
** It will also construct an inverse index of the last said forward index (searchterm completion)

All data will be stored into simple files instead of a database. Because i like do defer unneccecary 
architectural decisions as long as they do not need attention. Please do not expect any more writing
or writeups at the moment, nor me to provide a full architecture. This is simply an undeveloped idea.

That said, please remember this is a private educational project.

# License

Usage is provided under the [MIT License](http://opensource.org/licenses/mit-license.php). See LICENSE for the full details.
