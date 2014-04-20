package dbFileBuilder.playground;

import dbFileBuilder.wiki.Wiki;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 *
 */
public class TestMain {

    public static void main(String args[]) throws FileNotFoundException, IOException {
        File file = new File("wikiTestFile.txt");
        File oFile = new File("test.html");
        FileOutputStream fileOutputStream = new FileOutputStream(oFile);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, Charset.forName("UTF8"));
        BufferedWriter bufferWriter = new BufferedWriter(outputStreamWriter);

        System.out.println("File to process is " + file);
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str = "";
        Wiki wk = new Wiki();
        try {
            while (in.ready()) {
                str += in.readLine() + '\n';
            }
            in.close();
        } // print the error
        catch (IOException ex) {
            System.out.println(ex);
            for (StackTraceElement el : ex.getStackTrace()) {
                System.out.println(el);
            }
        }

        str = wk.process(str);
        bufferWriter.write(str);

        bufferWriter.write("</body>\n</html>");
        bufferWriter.close();

    }
}
