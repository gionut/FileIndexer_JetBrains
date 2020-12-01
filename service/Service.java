package service;

import model.Dictionary;
import model.Library;
import model.Tokenizer;
import observer.FileWatcher;
import repository.IRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Service {
    private final IRepository repository;

    public Service(IRepository repository) {
        this.repository = repository;
    }

    /*
    Returns the library associated with a certain tokenization algorithm
    @params Tokenizer tokenizer - the tokenization algorithm
    @returns Library - the library constructed with the given tokenization algorithm
     */
    public Library getLibrary(Tokenizer tokenizer)
    {
        return repository.getLibrary(tokenizer);
    }

    /*
    @params String tokenizerName - the name of the tokenizer used for indexing
    @returns Tokenizer - an instance of the Tokenizer class that has tokenizerName as keyword
     */
    public Tokenizer getTokenizer(String tokenizerName)
    {
        return repository.getTokenizer(tokenizerName);
    }

    /*
    @returns Set<String> the set of the tokenizers keywords
     */
    public Set<String> getTokenizers()
    {
        return repository.getTokenizers();
    }

    /*
    Updates the Repository with a new dictionary.
    If there is already a library (using the same Tokenization Algorithm as the dictionary) the dictionary
    will be merged to the already existing library. Otherwise, a new entry will be created in the repository,
    consisting of the Tokenization method and the library constructed with that method
    The method must be synchronized because it can be possibly called by multiple threads at the same time
    while it involves modifications over a common resource( Library ).

    @params Tokenizer tokenizer - The Tokenization Algorithm
            Dictionary dictionary - The dictionary resulted after the file was indexed;

     */
    private void addToLibrary(Tokenizer tokenizer, Dictionary dictionary) {

        Library library = repository.getLibrary(tokenizer);

        if (library == null) {
            Library newLibrary = new Library(tokenizer);
            newLibrary.add(dictionary);
            repository.addLibrary(tokenizer, newLibrary);
        }
        else
            library.add(dictionary);
    }

    /*
    After a file is indexed, it is being watched for possible modifications in a separate thread.
    If a modification occurs the method FileWatcher.run() will be called.

    watchFile method is accessible from multiple threads so it must be synchronized
    @params Dictionary dictionary - the dictionary of the file that is being watched
     */
    private void watchFile(Dictionary dictionary)
    {
        FileWatcher fileWatcher = new FileWatcher(this, dictionary);
        Thread thread = new Thread(fileWatcher);
        //threads.add(thread);
        thread.start();
    }

    /*
    Indexing a new file using a given Tokenization Algorithm. The Tokenization is computed in a different thread.
    If the Tokenization Algorithm was not used before, a new library will be created and added to the repository of libraries
    Otherwise, a new library is created for indexing the file, but it will be reunited with the base library afterwards.
    The base library consists of all the files that were indexed with the same Tokenization Algorithm.

    The indexed file is going to be checked for modification during runtime;

    @params Tokenizer tokenizer - The Tokenization Algorithm
            String fileName - The file to be indexed;
    */
    public void indexFile(Tokenizer tokenizer, String fileName) {
        File file = new File(fileName);
        Dictionary dictionary = new Dictionary(tokenizer, file);
        dictionary.run();
        watchFile(dictionary);
        addToLibrary(tokenizer, dictionary);
    }

    /*
    Indexing a directory means indexing all the files and subdirectories in the directory.
    Parallel Streams are 2X slower than Threads... sorry functional programming, it's not enough to be pretty :'(

    The files are going to be indexed and checked for modifications in different threads;

    @params Tokenizer tokenizer - The Tokenization Algorithm
            String dirNam - The directory to be indexed;
    */
    public void indexDirectory(Tokenizer tokenizer, String dirName) {
        File directory = new File(dirName);
        File[] listOfFiles = directory.listFiles();
        if(listOfFiles == null)
            return;

        List<Dictionary> dictionaries = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                Dictionary dictionary = new Dictionary(tokenizer, file);
                dictionaries.add(dictionary);
                watchFile(dictionary);
                Thread thread = new Thread(dictionary);
                threads.add(thread);
                thread.start();
            }
            else if (file.isDirectory()) {
                indexDirectory(tokenizer, file.getAbsolutePath());
            }
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        }catch(InterruptedException e)
        {
            throw new RuntimeException("Indexing directory failed");
        }
        // parallel streams approach ( 2X slower)
        //dictionaries = dictionaries.stream().parallel().map(Dictionary::index).collect(Collectors.toList());

        for (Dictionary dictionary : dictionaries) {
            addToLibrary(tokenizer, dictionary);
        }
    }


    /*
    Query for a word in the indexed files

    @params String word - the word to query for
            Tokenizer tokenizer - the Tokenization Algorithm for the libraries that are going to be queried
    @returns Set<File> - the files that contain the given word
    */
    public Set<File> query(Tokenizer tokenizer, String word)
    {
        Library library = repository.getLibrary(tokenizer);
        return library.getFiles(word.toLowerCase());
    }

}
