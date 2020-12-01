package view;

import service.Service;
import java.util.Set;

public class AvailableTokenizers extends Command{
    Service service;

    public AvailableTokenizers(String key, String description, Service service)
    {
        super(key, description);
        this.service = service;
    }

    @Override
    public void execute(String[] params)
    {
        try
        {
            if(params.length != 0)
                throw new RuntimeException("Invalid number of parameters. See the manual for tokenizers!");

            Set<String> tokenizers = service.getTokenizers();
            for(String tokenizer : tokenizers)
            {
                System.out.println(tokenizer);
            }
        }
        catch(RuntimeException e)
        {
            System.out.println(e.toString());
        }
    }
}
