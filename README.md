codemining-treelm
===============
codemining-treelm contains code for language models that work on trees.

`codemining.ast` contains code to convert ASTs to language-agnostic TreeNodes

`codemining.lm` contains an implementation of PCFGs and TSGs as well as some idiom-related code.


The project depends on three internal (maven) modules:

a) [codemining-utils](https://github.com/mast-group/codemining-utils)
b) [codemining-core](https://github.com/mast-group/codemining-core)
c) [codemining-sequencelm](https://github.com/mast-group/codemining-sequencelm)

The rest of the dependencies are declared in the maven dependencies. 

For learning grammars over changes, CLG added a branch called "cd-models" (branch added mostly because I'm pointing at two remotes for the treelm project, which is probably a mistake, but too late now).

I added two files:

`codemining.ast.distilledchanges.ChangeDistillerTreeExtractor.java` 
(which doesn't have very much in it, yet).

and 

`codemining.lm.tsg.tui.java.CDSampleTSG.java`, which is where we are testing out our (broken) code.

For the latter, you'll see that I have been giving it a training directory (of before/after changes), a file that 
lists commit SHAs, and the number of iterations. For each line/SHA in the file, it looks in the training dir for 
that SHA_BEFORE.txt and *_AFTER.txt (following Ashley's naming convention).  Then, it calls the distiller to
extract the changes.

The absolute current version of the code (as of 11:30 am on 9/21) actually just exists at that point because I was debugging.
In theory, though, for each of the changes it gets from the distiller, it can extract the tree from the ChangeDistillerTreeExtractor
and add it to the sampler.

However, because I was having obvious problems getting the changes to come out correctly, I commented out a bunch of stuff/left
it in a weird state.  Go check out my notes on the ChangeDistiller code for the status over there.

