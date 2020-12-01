package view;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class View {
    private final Map<String, Command> commands;

    public View()
    {
        commands = new HashMap<>();
    }

    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    public void addCommand(Command c)
    {
        commands.put(c.getKey(),c);
    }

    private void printMenu()
    {
        for(Command command : commands.values())
        {
            System.out.println(command.getKey());
        }
    }

    public void show()
    {
        Scanner scanner = new Scanner(System.in);
        printMenu();
        while(true)
        {
            System.out.print("\n>>");
            String[] commandString = scanner.nextLine().split(" ");
            String key = commandString[0];
            String[] params = Arrays.copyOfRange(commandString, 1, commandString.length);

            Command command = commands.get(key);
            if (command == null)
            {
                System.out.println("Invalid Option");
                continue;
            }

            command.execute(params);
        }
    }
}
