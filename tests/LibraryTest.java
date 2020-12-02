package tests;

import model.*;
import org.junit.jupiter.api.*;

import java.io.File;


import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {
    Library library;
    Dictionary dictionary1;
    Dictionary dictionary2;

    @BeforeEach
    public void setUp()
    {
        File file1 = new File("./IndexedFiles/test4.TXT");
        File file2 = new File("./IndexedFiles/test1.TXT");

        dictionary1 = new Dictionary(new WordsTokenizer(), file1);
        dictionary2 = new Dictionary(new WordsTokenizer(), file2) ;
        dictionary1.run();
        dictionary2.run();
        Tokenizer tokenizer = new WordsTokenizer();

        library = new Library(tokenizer);
    }

    @Test
    /*
    Library::add adds a dictionary containing the words of a tokenized file into the library.
    wordsTable field in Library should contain now the words in that file
     */
    public void add_method_should_add_a_dictionary_to_the_library()
    {
        library.add(dictionary1);
        library.add(dictionary2);
        assertEquals(library.getLibrary().get("wolf").size(), 1);
    }

    @Test
    public void removeDictionary_should_remove_dictionary_from_library()
    {
        library.add(dictionary1);
        library.add(dictionary2);
        library.removeDictionary(dictionary1);
        assertEquals(library.getDictionaries("the").size(), 1);
    }

    @AfterEach
    public void tearDown()
    {
        library = null;
    }
}
