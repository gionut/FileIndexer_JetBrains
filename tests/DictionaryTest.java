package tests;

import model.Dictionary;
import model.Tokenizer;
import model.WordsTokenizer;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {
    private Dictionary dictionary;
    private List<String> expectedWords;

    @BeforeEach
    public void setUp()
    {
        File file = new File("./IndexedFiles/test4.TXT");
        dictionary = new Dictionary(new WordsTokenizer(), file);
        Tokenizer tokenizer = new WordsTokenizer();
        expectedWords = new ArrayList<>();
        try {
            FileReader reader = new FileReader(file.getAbsolutePath());
            BufferedReader br = new BufferedReader(reader);

            String text = br.lines().collect(Collectors.joining());
            expectedWords.addAll(Arrays.asList(tokenizer.tokenize(text)));

        }
        catch(IOException e)
        {}
    }

    @Test
    /*
    Dictionary::run method should tokenize the file in a dictionary
     */
    public void run_method_should_return_all_the_words_from_file()
    {
        dictionary.run();
        assertArrayEquals(dictionary.getWords().toArray(), expectedWords.toArray());
    }

    @Test
    /*
    Dictionary::run method should throw a RuntimeException if the path to the provided file was not specified
    correctly, or if the file does not exist.
     */
    public void run_method_should_throw_RuntimeException_for_incorrectly_specified_file()
    {
        File file1 = new File("incorrectSpecifiedFile");
        try
        {
            Dictionary dictionary1 = new Dictionary(dictionary.getTokenizer(), file1);
            dictionary1.run();
            assert(false);
        }
        catch(RuntimeException e)
        {
            assert(true);
        }

    }

    @AfterEach
    public void tearDown()
    {
        dictionary = null;
        expectedWords = null;
    }

}
