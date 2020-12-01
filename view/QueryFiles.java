package view;

import model.Tokenizer;
import service.Service;

import java.io.File;
import java.util.Set;

public class QueryFiles extends Command{
    Service service;

    public QueryFiles(String key, String description, Service service)
    {
        super(key, description);
        this.service = service;
    }

    @Override
    public void execute(String[] params)
    {
        try
        {
            if(params.length != 2)
                throw new RuntimeException("Invalid number of parameters. See the manual for query!");

            String word = params[0];
            String tokenizerName = params[1];
            Tokenizer tokenizer = service.getTokenizer(tokenizerName);

            Set<File> files = service.query(tokenizer, word);

            if(files == null)
                System.out.println("the word '" + word + "' could not be found");
            else {
                for (File file : files) {
                    System.out.println(file.getAbsoluteFile());
                }
            }
        }
        catch(RuntimeException e)
        {
            System.out.println(e.toString());
        }
    }
}
