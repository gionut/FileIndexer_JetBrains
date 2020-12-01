package view;

public abstract class Command {
    private final String key;
    private final String description;

    public Command(String key, String description)
    {
        this.key = key; this.description = description;
    }

    /*
    Executes the command with the given parameters

    @params String[] params - array of strings representing the parameters required by the command
     */
    public abstract void execute(String[] params);

    /*
    @returns String - the keyword ford the command
     */
    public String getKey()
    {
        return key;
    }

    /*
    @returns String - the command description
     */
    public String getDescription()
    {
        return description;
    }
}
