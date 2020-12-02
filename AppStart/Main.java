package AppStart;

import model.StopWordsTokenizer;
import model.Tokenizer;
import model.WordsTokenizer;
import repository.IRepository;
import repository.Repository;
import service.Service;
import view.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Main {

    private static void SampleProgramRun(Service service)
    {
        try
        {
            Tokenizer stopWordsTokenizer = service.getTokenizer("stopWordsTokenizer");
            Tokenizer wordsTokenizer = service.getTokenizer("wordsTokenizer");

            File dir = new File("./IndexedFiles");
            service.indexDirectory(stopWordsTokenizer, dir.getAbsolutePath());
            service.indexFile(wordsTokenizer, "./IndexedFiles/test1.TXT");

            //Should print null since there are no files indexed with StopWordsTokenizer that contain the word "the"
            System.out.println(service.query(stopWordsTokenizer, "the"));

            //Should print test4.TXT, the one with Little Red Riding Hood, since it is the only one that contain that word
            System.out.println(service.query(stopWordsTokenizer, "riding"));

            //Should print one file, because only one file that was indexed with wordsTokenizer contain the word "the"
            System.out.println(service.query(wordsTokenizer, "the"));

            //modifying a file between indexing and query it, should be manageable
            File file = new File("./IndexedFiles/test4.TXT");
            PrintWriter writer = new PrintWriter(file);
            writer.print("\0");
            writer.close();

            //Emptying the file about Little Red Riding Hood should determine query for word "riding" to return
            // a set of size 0
            System.out.println(service.query(stopWordsTokenizer, "riding").size());

            writer = new PrintWriter(file);
            writer.print("\"Little Red Riding Hood\" is a European fairy tale about a young girl and a Big Bad Wolf. Its origins can be traced back to the 10th century to several European folk tales, including one from Italy called The False Grandmother. The best known version was written by Charles Perrault.");
            writer.close();

        }
        catch(IOException | RuntimeException e)
        {

        }
    }

    public static void main(String[] args) {
        IRepository repository = new Repository();
        Service service = new Service(repository);
        View view = new View();

        Tokenizer tokenizer1 = new WordsTokenizer();
        Tokenizer tokenizer2 = new StopWordsTokenizer();

        repository.addTokenizer("wordsTokenizer", tokenizer1);
        repository.addTokenizer("stopWordsTokenizer", tokenizer2);

        //SampleProgramRun(service);

        Command command1 = new IndexFile("indexFile", "Index a given file with a given Tokenization algorithm. The file must be provided " +
                "through its absolute path." +
                "\n>>indexFile fileAbsolutePath tokenizer\n", service);

        Command command2 = new IndexDirectory("indexDir", "Index all the files in a given directory," +
                " on all levels with a given Tokenization algorithm. The directory must be provided through its absolute path." +
                "\n>>indexDir directoryAbsolutePath tokenizer\n", service);

        Command command3 = new QueryFiles("query", "Query all files that were indexed with the given " +
                "tokenization algorithm for a given word;" +
                "\n>>query word tokenizer\n", service);

        Command command4 = new AvailableTokenizers("tokenizers", "Show all the available tokenization algorithms" +
                "\n>>tokenizers\n", service);

        Command command5 = new Manual("manual", "Describe the given command." +
                "\n>>manual commandName\n", view);

        Command command6 = new Exit("exit", "Close the program" +
                "\n>>exit\n");

        view.addCommand(command1);
        view.addCommand(command2);
        view.addCommand(command3);
        view.addCommand(command4);
        view.addCommand(command5);
        view.addCommand(command6);

        view.show();
    }

}
