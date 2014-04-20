package dbFileBuilder.wiki;

import dbFileBuilder.article.nituachDikduki.NituachDikduki;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * Test for {@link  dbFileBuilder.wiki.Wiki} class.<br>
 * Convert {@code wikiTestFile.txt} into {@code  test.html}.
 */
public class WikiTester {

    static File OUTPUT_FILE = new File("test.html");
    static File TEST_FILE = new File("wikiTestFile.txt");

    public static void main(String[] args) throws IOException {
        System.out.println("Entry point is on Wiki Tester");
        Wiki nw = new Wiki();
        FileOutputStream fileOutputStream = new FileOutputStream(OUTPUT_FILE);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, Charset.forName("UTF8"));
        BufferedWriter bufferWriter = new BufferedWriter(outputStreamWriter);

        BufferedReader in = new BufferedReader(new FileReader(TEST_FILE));
        String str = "";

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

        bufferWriter.write("<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "       <title>פלט לבדיקה</title>\n"
                + "       <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "       <META HTTP-EQUIV=\"CACHE-CONTROL\" CONTENT=\"NO-CACHE\">\n"
                + "       <link href=\"testStyle.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\">\n"
                + "    </head>\n"
                + "    <body>\n");

        if (NituachDikduki.isNeeded(str)) {
            NituachDikduki nd = new NituachDikduki(str);
            str = nd.getWikiEntryWithoutND();
        }
        str = nw.process(str);
        bufferWriter.write(str);
        bufferWriter.write("</body>\n</html>");

        bufferWriter.close();

        System.out.println("Results in: \n" + OUTPUT_FILE.getAbsolutePath());
    }
}
