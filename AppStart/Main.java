package AppStart;

import model.StopWordsTokenizer;
import model.Tokenizer;
import model.WordsTokenizer;
import repository.IRepository;
import repository.Repository;
import service.Service;
import view.*;


public class Main {

    public static void main(String[] args) {
        IRepository repository = new Repository();
        Service service = new Service(repository);
        View view = new View();

        Tokenizer tokenizer1 = new WordsTokenizer();
        Tokenizer tokenizer2 = new StopWordsTokenizer();

        repository.addTokenizer("wordsTokenizer", tokenizer1);
        repository.addTokenizer("stopWordsTokenizer", tokenizer2);

        Command command1 = new IndexFile("indexFile", "Index a given file. The file must be provided " +
                "through its absolute path." +
                "\n>>indexFile fileAbsolutePath\n", service);

        Command command2 = new IndexDirectory("indexDir", "Index all the files in a given directory," +
                " on all levels. The directory must be provided through its absolute path." +
                "\n>>indexDir directoryAbsolutePath\n", service);

        Command command3 = new QueryFiles("query", "Query all files that were indexed with the given " +
                "tokenization algorithm for a given word;" +
                "\n>>query word tokenization\n", service);

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
