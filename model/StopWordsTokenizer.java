package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StopWordsTokenizer implements Tokenizer{
    List<String> stopWords;
    String keyword = "stopWordsTokenizer";

    public StopWordsTokenizer() {
        try {
            FileReader reader = new FileReader("E:\\IONUT\\Semestrul3\\JetBrainsFileIndexing\\src\\stopWords.TXT");
            BufferedReader br = new BufferedReader(reader);
            stopWords = br.lines().map(String::strip).collect(Collectors.toList());
        }
        catch(IOException e)
        {
           throw new RuntimeException("Cannot find a file for StopWords!");
        }
    }

    @Override
    public String getKeyword() {
        return keyword;
    }

    /*
        Tokenize a text removing stopWords. StopWords are given in a file
         */
    @Override
    public String[] tokenize(String text) {
        String[] words = text.split("\\W+");
        ArrayList<String> listOfWords = Stream.of(words).map(String::toLowerCase)
                                                        .collect(Collectors.toCollection(ArrayList<String>::new));
        listOfWords.removeAll(stopWords);
        return listOfWords.toArray(new String[0]);
    }
}
