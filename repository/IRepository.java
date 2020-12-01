package repository;

import model.Library;
import model.Tokenizer;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IRepository {

    /*
    Returns the Library that was constructed with the given Tokenization algorithm
    @params Tokenizer tokenizer - the Tokenization algorithm
    @returns Library
    */
    Library getLibrary(Tokenizer tokenizer);

    /*
    Adds a new Library that was constructed using the given Tokenization Algorithm
    @params Tokenizer tokenizer - the Tokenization algorithm
            Library library - the library to be added, constructed with tokenizer
    */
    public void addLibrary(Tokenizer tokenizer, Library library);

    /*
    Adds a new Tokenizer to the list of tokenizers in the repository

    @params String tokenizerName - the key by which the tokenizer will be identified
            Tokenizer tokenizer - the new tokenizer that is going to be added to the repository
     */
    public void addTokenizer(String tokenizerName, Tokenizer tokenizer);

    /*
    Returns a tokenizer identified by the given tokenizerName

    @params String tokenizerName - the key by which the tokenizer will be identified
    @returns Tokenizer - the tokenizer that was identified
     */
    public Tokenizer getTokenizer(String tokenizerName);

    /*
    @returns Set<String> - the names of all tokenizers in the repository
     */
    public Set<String> getTokenizers();
}
