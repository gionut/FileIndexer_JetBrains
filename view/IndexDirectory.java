package view;

import model.Tokenizer;
import service.Service;


public class IndexDirectory extends Command{
    Service service;

    public IndexDirectory(String key, String description, Service service)
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
                throw new RuntimeException("Invalid number of parameters. See the manual for indexFile!");

            String directoryPath = params[0];
            String tokenizerName = params[1];
            Tokenizer tokenizer = service.getTokenizer(tokenizerName);
            service.indexDirectory(tokenizer, directoryPath);
        }
        catch(RuntimeException e)
        {
            System.out.println(e.toString());
        }
    }
}
