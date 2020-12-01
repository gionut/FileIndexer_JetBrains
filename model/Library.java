package model;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Library{
    private final Map<String, Set<File>> wordsTable;
    private final Tokenizer tokenizer;

    public Library(Tokenizer tokenizer)
    {
        wordsTable = new HashMap<>();
        this.tokenizer = tokenizer;
    }

    /*
    Returns the Library data
    @returns: Map<String, Set<File>> - the map containing words and the fileNames that contains them
     */
    public Map<String, Set<File>> getLibrary() {
        return wordsTable;
    }

    /*
    Returns a set of strings with the name of the files that contain the given word

    @params String word - the word that is searched for
    @returns Set<String> - the name of the files that contain the given word
     */
    public Set<File> getFiles(String word)
    {
        return wordsTable.get(word);
    }

    /*
    After a file is indexed, the resulted dictionary of words is merged to the library.
    The library is the union of all dictionaries resulted by indexing all the previous files,
    using the same Tokenization algorithm.

    multiple threads may access wordsTable, this is why add must be synchronized altough the method that calls
    it is also synchronized
    @params Dictionary dictionary - the dictionary that resulted after the indexation of fileName's content
    */
    public synchronized void add(Dictionary dictionary)
    {
        for(String word : dictionary.getWords())
        {
            addWordToLibrary(dictionary, word);
        }
    }

    /*
    if a word is already contained in the library, the file will be added to the set of files that is mapped by that word
    if a word is contained only in the dictionary, a new entry will be created in the base library, containing
    the word and a set with one element( the file containing that word)

    multiple threads may access wordsTable, this is why addWordToLibrary must be synchronized
     */
    private synchronized void addWordToLibrary(Dictionary dictionary, String word) {
        File file = dictionary.getFile();

        if(wordsTable.containsKey(word))
        {
            wordsTable.get(word).add(file);
        }
        else
        {
            Set<File> fileSet = new HashSet<>();
            fileSet.add(file);
            wordsTable.put(word, fileSet);
        }
    }

    /*
    Remove a dictionary of a file from the library in which was formerly added.
    It consists of removing the file from the sets of files mapped by every word in the dictionary.
    if the word was contained by the dictionary file only, the word will be removed from the keySet of wordsTable

    multiple threads may access wordsTable, this is why removeDictionary must be synchronized
    @params Dictionary dictionary - the dictionary to be removed
    */
    public synchronized void removeDictionary(Dictionary dictionary)
    {
        File file = dictionary.getFile();

        for(String word : dictionary.getWords())
        {
            if(this.wordsTable.containsKey(word))
            {
                this.wordsTable.get(word).remove(file);

                if(this.wordsTable.get(word).size() == 0)
                    this.wordsTable.remove(word);
            }
        }
    }
}
