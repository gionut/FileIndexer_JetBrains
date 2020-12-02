package model;

public interface Tokenizer {
    /*
    Tokenize a text given as parameter

    @params String text - the text to be tokenized
    @returns String[] - an array of words. A word is defined by the tokenization algorithm
                        used by each implementing class
     */
    String[] tokenize(String text);
}
