package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Dictionary implements Runnable{
    private final List<String> words;
    private final Tokenizer tokenizer;
    private final File file;

    public Dictionary(Tokenizer tokenizer, File file)
    {
        this.tokenizer = tokenizer;
        this.file = file ;
        this.words = new ArrayList<>();
    }

    /*
    @returns List<String> - the list containing the words that index the file
     */
    public List<String> getWords() {
        return this.words;
    }

    /*
    @returns File - the indexed file
     */
    public File getFile() {
        return file;
    }

    /*
    @returns Tokenizer - the Tokenization algorithm that was used to index the file
     */
    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    /*for using parallel streams in  Service.indexDirectory method. it does exactly what run method does, but it also
     returns the current(modified) dictionary*/
    public Dictionary index()
    {
        try {
            FileReader reader = new FileReader(file.getAbsolutePath());
            BufferedReader file = new BufferedReader(reader);

            String line;
            while ((line = file.readLine()) != null) {
                words.addAll(Arrays.asList(tokenizer.tokenize(line)));
            }
            return this;
        }
        catch(IOException e)
        {
            throw new RuntimeException("Error when Indexing file. Maybe an incorrect path was specified! (" + file.getAbsolutePath() + ")");
        }
    }

    /*
    Indexing a file requires splitting the text into words whose meaning is defined by
    the Tokenization algorithm.
    The List<String> words field will be updated to contain the words that index the file

    if the file provided cannot be accessed ...

    the whole operation parses two or three times the content of the file.
    A low level approach in which only one byte at a time is processed could manage to do the job in one parse.
    The overall complexity would remain the same(O(n) - linear time, where n is the number of bytes in the file), but
    the effort will be unmeasurable higher. Consequently, I decided to use that time to provide other functionalities or
    improvements that are more doable and extensive at the same time. Hope you don't mind, reader.
    */
    public void run() {
        try {
            FileReader reader = new FileReader(file.getAbsolutePath());
            BufferedReader file = new BufferedReader(reader);

            String text = file.lines().collect(Collectors.joining());
            words.addAll(Arrays.asList(tokenizer.tokenize(text)));

        }
        catch(IOException e)
        {
            throw new RuntimeException("Error when Indexing file. Maybe an incorrect path was specified! (" + file.getAbsolutePath() + ")");
        }
    }

}
