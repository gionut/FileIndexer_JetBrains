package repository;

import model.Library;
import model.Tokenizer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Repository implements IRepository{
    private final Map<Tokenizer, Library> libraries;
    private final Map<String, Tokenizer> tokenizers;

    public Repository()
    {
        libraries = new HashMap<>();
        tokenizers = new HashMap<>();
    }

    @Override
    public void addLibrary(Tokenizer tokenizer, Library library)
    {
        libraries.put(tokenizer, library);
    }

    @Override
    public Library getLibrary(Tokenizer tokenizer) {
        return libraries.get(tokenizer);
    }

    @Override
    public void addTokenizer(String tokenizerName, Tokenizer tokenizer)
    {
        tokenizers.put(tokenizerName, tokenizer);
    }

    @Override
    public Tokenizer getTokenizer(String tokenizerName) {
        return tokenizers.get(tokenizerName);
    }

    @Override
    public Set<String> getTokenizers(){ return tokenizers.keySet();}
}
