package view;

public class Manual extends Command {
    View view;

    public Manual(String key, String description, View view)
    {
        super(key, description);
        this.view = view;
    }

    @Override
    public void execute(String[] params)
    {
        try
        {
            if(params.length != 1)
                throw new RuntimeException("Invalid number of parameters. See the manual for manual" +
                        "\n\t<manual manual>!");

            String commandName = params[0];

            Command command = view.getCommand(commandName);
            System.out.println(command.getDescription());

        }
        catch(RuntimeException e)
        {
            System.out.println(e.toString());
        }
    }
}
