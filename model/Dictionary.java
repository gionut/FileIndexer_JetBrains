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
    private final long lastModification;

    public Dictionary(Tokenizer tokenizer, File file)
    {
        this.tokenizer = tokenizer;
        this.file = file ;
        lastModification = file.lastModified();
        this.words = new ArrayList<>();
    }

    /*
    Checks if the file referenced by the file field has been modified since the last indexation
     */
    public boolean hasBeenModified()
    {
        return this.lastModification != file.lastModified();
    }

    /*
    @returns List<String> - the list containing the words that resulted from the tokenization of the file
    */
    public List<String> getWords() {
        return this.words;
    }

    /*
    @returns File - the file that was tokenized in the dictionary
     */
    public File getFile() {
        return file;
    }

    /*
    @returns Tokenizer - the Tokenization algorithm that was used to tokenize the file
     */
    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    /*
    Only for using parallel streams in  Service.indexDirectory method. it does exactly what run method does,
    but it also returns the current(modified) dictionary

    @throws RuntimeException if the file provided cannot be accessed
    */
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
    The List<String> words field will be updated to contain the tokenized words in the file

    @throws RuntimeException if the file provided cannot be accessed
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
