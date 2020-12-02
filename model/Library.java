package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Library{
    private final Map<String, Set<Dictionary>> wordsTable;
    private final Set<Dictionary> fileWatcher;
    private final Tokenizer tokenizer;

    public Library(Tokenizer tokenizer)
    {
        wordsTable = new HashMap<>();
        fileWatcher = new HashSet<>();
        this.tokenizer = tokenizer;
    }

    /*
    @returns Tokenizer - the tokenizer used to construct the library
     */
    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    /*
    Returns the dictionaries containing the indexed files together and a timeStamp of their last modification
    @returns: Set<Dictionary> - the set of dictionaries in the library containing files and their las modification time
    */
    public Set<Dictionary> getFileWatcher() {
        return fileWatcher;
    }

    /*
    Returns the Library data
    @returns: Map<String, Set<Dictionary>> - the map containing words and the dictionaries of files that contains them
    */
    public Map<String, Set<Dictionary>> getLibrary() {
        return wordsTable;
    }

    /*
    @params String word - the word that is searched for
    @returns Set<Dictionary> - set of dictionaries consisting of the files that contain the given word
     */
    public Set<Dictionary> getDictionaries(String word)
    {
        return wordsTable.get(word);
    }

    /*
    After a file is indexed, the resulted dictionary of words is merged to the library.
    The library is the union of all dictionaries resulted by indexing all the previous files,
    using the same Tokenization algorithm.

    The new added dictionary is also added to the fileWatcher in order to keep track of the file that were
    modified between indexation and a query operation

    @params Dictionary dictionary - the dictionary that resulted after the indexation of fileName's content
    */
    public void add(Dictionary dictionary)
    {
        fileWatcher.add(dictionary);

        for(String word : dictionary.getWords())
        {
            addWordToLibrary(dictionary, word);
        }
    }

    /*
    Directly called by Library::add method
    If a word is already contained in the library, the dictionary will be added to the set of dictionaries
    that is mapped by that word. If a word is contained only in the dictionary, a new entry will be created
    in the base library, containing the word and a set with one element( the dictionary of the file
    containing that word)

    @params Dictionary dictionary - the dictionary to be added to the wordsTable
            String word - the word that can be found in the file contained by the given dictionary
     */
    private void addWordToLibrary(Dictionary dictionary, String word) {
        if(wordsTable.containsKey(word))
        {
            wordsTable.get(word).add(dictionary);
        }
        else
        {
            Set<Dictionary> dictionaries = new HashSet<>();
            dictionaries.add(dictionary);
            wordsTable.put(word, dictionaries);
        }

    }

    /*
    Remove a dictionary of a file from the library in which was formerly added.
    It consists of removing the dictionary from the sets of dictionaries mapped by every word in the library.
    If the word was contained by the dictionary file only, the word will be removed from the keySet of wordsTable

    @params Dictionary dictionary - the dictionary to be removed
    */
    public void removeDictionary(Dictionary dictionary)
    {
        fileWatcher.remove(dictionary);

        for(String word : dictionary.getWords())
        {
            if(this.wordsTable.containsKey(word))
            {
                this.wordsTable.get(word).remove(dictionary);

                if(this.wordsTable.get(word).size() == 0)
                    this.wordsTable.remove(word);
            }
        }
    }
}
