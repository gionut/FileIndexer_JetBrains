package service;

import model.Dictionary;
import model.Library;
import model.Tokenizer;
import repository.IRepository;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

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
    Updates a library in the Repository with a new dictionary.
    If there is already a library (using the same Tokenization Algorithm as the dictionary) the dictionary
    will be merged to the already existing library.Otherwise, a new entry will be created in the repository,
    consisting of the Tokenization method and the library constructed with that method

    @params Dictionary dictionary - The dictionary resulted after the file was indexed;

     */
    private void addToLibrary(Dictionary dictionary) {

        Tokenizer tokenizer = dictionary.getTokenizer();
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
    Indexing a new file using a given Tokenization Algorithm. A new dictionary is created for the
    specified file, containing the words of that file's tokenization. The dictionary will be merged to the
    library constructed with the same tokenization algorithm. If such library does not exists, one
    containing the newly created dictionary will be created and added to the repository of libraries.

    @params Tokenizer tokenizer - The Tokenization Algorithm
            String fileName - The file to be indexed;
    */
    public void indexFile(Tokenizer tokenizer, String fileName) {
        File file = new File(fileName);
        Dictionary dictionary = new Dictionary(tokenizer, file);
        dictionary.run();
        addToLibrary(dictionary);
    }

    /*
    Indexing a directory means indexing all the files and subdirectories in the directory.
    Parallel Streams are 2X slower than Threads... sorry functional programming, it's not enough to be pretty :'(

    The files are going to be tokenized in different threads. There is no need for synchronization because
    no shared resources are involved in the tokenization process.

    The threads will be joined later on in this method, and the dictionaries resulted are going to be added
    to the corresponding libraries in the repository.

    The method is called recursively for each subdirectory.

    @params Tokenizer tokenizer - The Tokenization Algorithm
            String dirNam - The directory to be indexed;
    */
    public void indexDirectory(Tokenizer tokenizer, String dirName) {
        File directory = new File(dirName);
        File[] listOfFiles = directory.listFiles();
        if(listOfFiles == null)
            return;

        // parallel streams approach ( 2X slower)--TESTED--
        //dictionaries = dictionaries.stream().parallel().map(Dictionary::index).collect(Collectors.toList());

        indexFiles(tokenizer, Arrays.asList(listOfFiles));
    }

    /*
    Called directly by Service::indexDirectory method. It handles Thread creation and joining for each file
    that is needed to be tokenized, calls Service::indexDirectory for subdirectories and adds the newly
    created directories to the corresponding libraries in the repository through Service::addToLibrary method

    @params Tokenizer tokenizer - the tokenization algorithm used to index the files
            List<File> listOfFiles - the list of files that is going to be indexed with the tokenizer
     */
    private void indexFiles(Tokenizer tokenizer, List<File> listOfFiles) {
        List<Dictionary> dictionaries = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                Dictionary dictionary = new Dictionary(tokenizer, file);
                dictionaries.add(dictionary);

                Thread thread = new Thread(dictionary);
                threads.add(thread);
                thread.start();
            }
            else if (file.isDirectory()) {
                indexDirectory(tokenizer, file.getAbsolutePath());
            }
        }

        try
        {
            for(Thread thread : threads)
            {
                thread.join();
            }
        }
        catch(InterruptedException e)
        {
            throw new RuntimeException("Indexing directory failed");
        }

        dictionaries.forEach(this::addToLibrary);
    }


    /*
    Query for a word in the indexed files

    If some files have been modified during runtime( state persistence between running session is not required )
    their corresponding dictionaries will be removed from the library they were added and the files will be indexed
    again

    @params String word - the word to query for
            Tokenizer tokenizer - the Tokenization Algorithm for the libraries that are going to be queried
    @returns Set<File> - the files that contain the given word
    */
    public Set<File> query(Tokenizer tokenizer, String word)
    {
        if(word.isEmpty())
            return new HashSet<>();

        Library library = repository.getLibrary(tokenizer);
        updateLibrary(library);

        Set<Dictionary> dictionaries = library.getDictionaries(word.toLowerCase());

        if(dictionaries == null)
           return new HashSet<>();

        return dictionaries.stream().map(Dictionary::getFile)
                                    .collect(Collectors.toSet());
    }

    /*
    Directly called by Service::query(). Checks if there are files that have been modified since the last
    indexation. If so, the dictionaries corresponding to the modified files will be removed from the
    library they were added earlier. The modified files will be indexed again.

    @params Library library - the library from which we remove the corrupted dictionaries
     */
    private void updateLibrary(Library library) {

        List<File> modifiedFiles = new ArrayList<>();
        Set<Dictionary> dictionaries = library.getFileWatcher();
        List<Dictionary> corruptedDictionary = new ArrayList<>();

        for(Iterator<Dictionary> iterator = dictionaries.iterator(); iterator.hasNext();)
        {
            Dictionary dictionary = iterator.next();
            if(dictionary.hasBeenModified())
            {
                corruptedDictionary.add(dictionary);
                modifiedFiles.add(dictionary.getFile());
            }
        }

        corruptedDictionary.forEach(library::removeDictionary);

        indexFiles(library.getTokenizer(), modifiedFiles);
    }
}
