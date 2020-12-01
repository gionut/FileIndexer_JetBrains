package observer;

import model.Dictionary;
import model.Library;
import service.Service;

public class FileWatcher implements Runnable {
    private long timeStamp;
    private final Dictionary dictionary;
    private final Service service;

    public FileWatcher(Service service, Dictionary dictionary ) {
        this.service = service;
        this.dictionary = dictionary;
        this.timeStamp = dictionary.getFile().lastModified();
    }

    /*
    Checks if an indexed file was modified and sends a signal through the private method update()
     */
    @Override
    public void run() {
        while(true) {
            long timeStamp = dictionary.getFile().lastModified();

            if (this.timeStamp != timeStamp) {
                this.timeStamp = timeStamp;
                update();
            }
        }
    }

    /*
    Is called when an indexed file was modified.
    The dictionary of the indexed file is removed from its library.
    If the file was not deleted( it still exists ) it will be indexed again
     */
    private synchronized void update()
    {
        removeOldDictionaryFromLibrary();
        if(dictionary.getFile().exists())
            service.indexFile(dictionary.getTokenizer(), dictionary.getFile().getAbsolutePath());
        else
        {
            //the file was deleted. Don't track it anymore
            System.exit(0);
        }
    }


    private void removeOldDictionaryFromLibrary()
    {
        Library library =  service.getLibrary(dictionary.getTokenizer());
        library.removeDictionary(dictionary);
    }
}
