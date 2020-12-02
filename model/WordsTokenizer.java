package model;

import java.util.stream.Stream;

public class WordsTokenizer implements Tokenizer{
    public WordsTokenizer() {}

    /*
    Tokenize a text into words. A word is defined by regex \w metacharacter.
    A word character is a character from a-z, A-Z, 0-9, including the _ (underscore) character
    */
    @Override
    public String[] tokenize(String text) {
        String[] words =  text.split("\\W+");
        return Stream.of(words).map(String::toLowerCase).toArray(String[]::new);
    }
}
