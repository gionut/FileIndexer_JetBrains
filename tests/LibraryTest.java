package tests;

import model.*;
import org.junit.jupiter.api.*;

import java.io.File;

import static junit.framework.Assert.assertEquals;

public class LibraryTest {
    Library library;
    Dictionary dictionary1;
    Dictionary dictionary2;

    @BeforeEach
    public void setUp()
    {
        File file1 = new File("E:\\IONUT\\Semestrul3\\JetBrainsFileIndexing\\src\\IndexedFiles\\test4.TXT");
        File file2 = new File("E:\\IONUT\\Semestrul3\\JetBrainsFileIndexing\\src\\IndexedFiles\\test1.TXT");

        dictionary1 = new Dictionary(new WordsTokenizer(), file1);
        dictionary2 = new Dictionary(new WordsTokenizer(), file2) ;
        dictionary1.run();
        dictionary2.run();
        Tokenizer tokenizer = new WordsTokenizer();

        library = new Library(tokenizer);
    }

    @Test
    public void Library_add_should_add_a_dictionary_to_the_library_specific_words_should_map_specific_files()
    {
        library.add(dictionary1);
        library.add(dictionary2);
        assertEquals(library.getLibrary().get("wolf").size(), 1);
    }

    @Test
    public void Library_add_should_add_a_second_dictionary_to_the_library_common_words_should_map_two_files()
    {
        library.add(dictionary1);
        library.add(dictionary2);
        assertEquals(library.getLibrary().get("the").size(), 2);
    }

    @Test
    public void Library_RemoveDictionary_should_remove_occurrences_of_dictionary_file_from_library()
    {
        library.add(dictionary1);
        library.add(dictionary2);
        library.removeDictionary(dictionary1);
        assertEquals(library.getFiles("the").size(), 1);
    }

    @AfterEach
    public void tearDown()
    {
        library = null;
    }
}
