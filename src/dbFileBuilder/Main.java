package dbFileBuilder;

import java.io.FileNotFoundException;

/**
 * Entry point for the program.
 */
public class Main {

    /**
     * No arguments needed.
     *
     * @param args the command line arguments - not needed.
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        System.setProperty("file.encoding", "UTF-8");
        System.out.println("Entry point is on Main");
        DatabaseFileBuilder dbFb = new DatabaseFileBuilder();
        dbFb.buildDatabase();

        System.out.println("Finished");
    }
}
