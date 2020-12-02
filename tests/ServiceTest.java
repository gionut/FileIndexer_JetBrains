package tests;

import model.Tokenizer;
import model.WordsTokenizer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.Repository;
import service.Service;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    Service service;

    @BeforeEach
    public void setUp()
    {
        service = new Service(new Repository());
    }

    @Test
    /*
    after a file is indexed, the dictionary that contains the file should be added to the Library
     */
    public void indexFile_should_add_the_dictionary_to_Library()
    {
        String file = "./IndexedFiles/test4.TXT";
        Tokenizer tokenizer = new WordsTokenizer();
        service.indexFile(tokenizer, file);

        assertNotNull(service.getLibrary(tokenizer).getLibrary());
    }

    @Test
    /*
    If a file has been modified between the indexation and the query, the query should return an updated
    result
     */
    public void query_for_word_if_file_modified_should_bring_updated_results()
    {
        String file = "./IndexedFiles/test4.TXT";
        Tokenizer tokenizer = new WordsTokenizer();
        service.indexFile(tokenizer, file);

        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("\0");
            writer.close();

            assertEquals(0, service.query(tokenizer, "the").size());

            writer = new PrintWriter(file);
            writer.print("\"Little Red Riding Hood\" is a European fairy tale about a young girl and a Big Bad Wolf. Its origins can be traced back to the 10th century to several European folk tales, including one from Italy called The False Grandmother. The best known version was written by Charles Perrault.");
            writer.close();
        }
        catch(IOException e)
        {}
    }

    @Test
    public void indexDirectory_add_to_library()
    {
        String file = "./IndexedFiles";
        Tokenizer tokenizer = new WordsTokenizer();
        service.indexDirectory(tokenizer, file);

        assertEquals(5, service.getLibrary(tokenizer).getLibrary().get("the").size());
    }

    @Test
    /*
    If the a directory was indexed and a file was modified afterwards, a query should return updated results
     */
    public void query_for_word_if_one_file_in_directory_modified_should_bring_updated_results()
    {
        String dir = "./IndexedFiles";
        String file = "./IndexedFiles/test4.TXT";
        Tokenizer tokenizer = new WordsTokenizer();
        service.indexDirectory(tokenizer, dir);

        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("\0");
            writer.close();

            assertEquals(4, service.query(tokenizer, "the").size());

            writer = new PrintWriter(file);
            writer.print("\"Little Red Riding Hood\" is a European fairy tale about a young girl and a Big Bad Wolf. Its origins can be traced back to the 10th century to several European folk tales, including one from Italy called The False Grandmother. The best known version was written by Charles Perrault.");
            writer.close();
        }
        catch(IOException e)
        {}
    }

    @Test
    public void Query_for_a_word_should_return_files_that_contain_the_word()
    {
        String dir = "./IndexedFiles";
        Tokenizer tokenizer = new WordsTokenizer();
        service.indexDirectory(tokenizer, dir);

        assertEquals(1, service.query(tokenizer, "riding").size());
    }

    @AfterEach
    public void tearDown()
    {
        service = null;
    }

}
