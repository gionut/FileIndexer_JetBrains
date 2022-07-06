# FileIndexer_JetBrains

A Java application which provides a service for indexing text files. 

The console interface allows for 

(i) specifying the indexed files and directories

(ii) querying files containing a given word. 

The library is extensible by the tokenization algorithm (simple splitting by words/support lexers/etc.). There is no state persistence between running sessions, only when the program is running.
