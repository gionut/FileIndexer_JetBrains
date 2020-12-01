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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ServiceTest {
    Service service;

    @BeforeEach
    public void setUp()
    {
        service = new Service(new Repository());
    }

    @Test
    public void IndexFile_addToLibrary_should_add_indexed_file_to_Library()
    {
        String file = "E:\\IONUT\\Semestrul3\\JetBrainsFileIndexing\\src\\IndexedFiles\\test4.TXT";
        Tokenizer tokenizer = new WordsTokenizer();
        service.indexFile(tokenizer, file);

        assertNotNull(service.getLibrary(tokenizer).getLibrary());
    }

    @Test
    public void IndexFile_watchFile_if_file_modified_remove_from_library_and_index_again()
    {
        String file = "E:\\IONUT\\Semestrul3\\JetBrainsFileIndexing\\src\\IndexedFiles\\test4.TXT";
        Tokenizer tokenizer = new WordsTokenizer();
        service.indexFile(tokenizer, file);

        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("\0");
            writer.close();

            assertEquals(service.getLibrary(tokenizer).getLibrary().size(), 0);

            writer = new PrintWriter(file);
            writer.print("\"Little Red Riding Hood\" is a European fairy tale about a young girl and a Big Bad Wolf. Its origins can be traced back to the 10th century to several European folk tales, including one from Italy called The False Grandmother. The best known version was written by Charles Perrault.");
            writer.close();
        }
        catch(IOException e)
        {}
    }

    @Test
    public void IndexDirectory_add_to_library()
    {
        String file = "E:\\IONUT\\Semestrul3\\JetBrainsFileIndexing\\src\\IndexedFiles";
        Tokenizer tokenizer = new WordsTokenizer();
        service.indexDirectory(tokenizer, file);

        assertEquals(5, service.getLibrary(tokenizer).getLibrary().get("the").size());
    }

    @Test
    public void IndexDirectory_watchFiles_if_file_modified_remove_from_library_and_index_again()
    {
        String dir = "E:\\IONUT\\Semestrul3\\JetBrainsFileIndexing\\src\\IndexedFiles";
        String file = "E:\\IONUT\\Semestrul3\\JetBrainsFileIndexing\\src\\IndexedFiles\\test4.TXT";
        Tokenizer tokenizer = new WordsTokenizer();
        service.indexDirectory(tokenizer, dir);

        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("\0");
            writer.close();

            assertEquals(4, service.getLibrary(tokenizer).getLibrary().get("the").size());

            writer = new PrintWriter(file);
            writer.print("\"Little Red Riding Hood\" is a European fairy tale about a young girl and a Big Bad Wolf. Its origins can be traced back to the 10th century to several European folk tales, including one from Italy called The False Grandmother. The best known version was written by Charles Perrault.");
            writer.close();
        }
        catch(IOException e)
        {}
    }

    @Test
    public void Query_should_return_indexed_files_that_contain_word()
    {
        String dir = "E:\\IONUT\\Semestrul3\\JetBrainsFileIndexing\\src\\IndexedFiles";
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
