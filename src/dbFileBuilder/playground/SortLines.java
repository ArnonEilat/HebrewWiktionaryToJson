package dbFileBuilder.playground;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Sort lines in file.
 */
public class SortLines {

    static ArrayList<String> lines;

    public static void main(String... args) throws IOException {
        long start = System.nanoTime();
        lines = loadLines("output.json");

        Collections.sort(lines, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() > o2.length()) {
                    return 1;
                } else if (o1.length() < o2.length()) {
                    return -1;
                }
                return 0;
            }
        });

        generateFile("lines.txt");

        // save lines.
        long time = System.nanoTime() - start;

        System.out.printf("Took %.3f second to read, sort and write to a file%n", time / 1e9);
    }

    private static void generateFile(String fileName) throws FileNotFoundException, IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, Charset.forName("UTF8"));
        BufferedWriter bufferWriter = new BufferedWriter(outputStreamWriter);

        System.out.println(".lines.size: " + lines.size());
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).length() > 1369) {
                bufferWriter.write(lines.get(i) + "\n");
            }
        }

        System.out.println("... Created file to load");
    }

    private static ArrayList<String> loadLines(String fileName) throws IOException {
        System.out.println("Reading file");
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        ArrayList<String> ret = new ArrayList<String>();
        String line;
        while ((line = br.readLine()) != null) {
            ret.add(line);
        }
        System.out.println("... Read file.");
        return ret;
    }
}
