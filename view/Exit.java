package view;

public class Exit extends Command{

        public Exit(String key, String description)
        {
            super(key, description);
        }

        @Override
        public void execute(String[] params)
        {
            System.exit(0);
        }
}

