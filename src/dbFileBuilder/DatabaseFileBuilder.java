package dbFileBuilder;

import dbFileBuilder.article.Article;
import dbFileBuilder.json.JSON;
import dbFileBuilder.utilities.StringUtility;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Take XML {@link dbFileBuilder.Globals#INPUT_FILE_NAME  file}
 * (<code>{@value  dbFileBuilder.Globals#INPUT_FILE_NAME}</code>) and convert it
 * to JSON
 * {@link dbFileBuilder.Globals#OUTPUT_FILE_NAME   file}(<code>{@value  dbFileBuilder.Globals#OUTPUT_FILE_NAME}</code>).<br>Each
 * line of the JSON file contains one JSON object, but the file itself is not a
 * JSON array.
 *
 */
public class DatabaseFileBuilder {

    private File fileToProcess;
    private BufferedReader bufrReader;
    /**
     * Indicates whether the processing is inside page tag or not.
     */
    private boolean insidePage = false;
    private StringBuilder strBuilder;
    /**
     * Indicates which entry is processing.
     */
    private static int entryToProcess = 0;
    private DocumentBuilderFactory docBuilderFactory;
    private DocumentBuilder DocBuilder;
    private InputStream inputStream;
    private Document doc;
    /**
     * Current line to process.
     */
    private int lineCount;
    private Writer writer;
    private String title;

    /**
     * Creates a new <tt>DatabaseFileBuilder</tt>.
     *
     * @throws FileNotFoundException if {@link Globals#INPUT_FILE_NAME input}
     * file is not found.
     */
    public DatabaseFileBuilder() throws FileNotFoundException {
        this.fileToProcess = new File(Globals.INPUT_FILE_NAME);
        this.bufrReader = new BufferedReader(new FileReader(this.fileToProcess));
        this.strBuilder = new StringBuilder("");
        this.writer = new Writer(Globals.OUTPUT_FILE_NAME);
    }

    /**
     * Build the Database
     */
    public void buildDatabase() {
        this.lineCount = 0;
        try {
            while (this.bufrReader.ready()) {
                this.process(this.bufrReader.readLine() + '\n');
                this.lineCount++;
            }
            this.bufrReader.close();
        } // print the error
        catch (IOException ex) {
            System.out.println(ex);
            for (StackTraceElement el : ex.getStackTrace()) {
                System.out.println(el);
            }
        } finally {
            this.writer.close();
        }
    }

    /**
     * Process wiktionary entry and write it as JSON to the
     * {@link Globals#OUTPUT_FILE_NAME output file}.
     *
     * @param stringToProcess Current line to process.
     */
    private void process(String stringToProcess) {

        String // Containing the data of the entry in wiktionary syntax
                wiktionaryEntryTxt = "";

        if (StringUtility.isAPageTag(stringToProcess, true)) {
            this.insidePage = true;// Processing going inside page tag 
            this.strBuilder = new StringBuilder(300);
        }
        this.strBuilder.append(stringToProcess);

        if (this.insidePage == true && StringUtility.isAPageTag(stringToProcess, false)) //stringToProcess contains an <page> entry.
        {
            this.insidePage = false;

            try {
                this.docBuilderFactory = DocumentBuilderFactory.newInstance();
                this.DocBuilder = this.docBuilderFactory.newDocumentBuilder();
                this.inputStream = new ByteArrayInputStream(this.strBuilder.toString().getBytes("UTF-8"));
                this.doc = DocBuilder.parse(this.inputStream);
                boolean isaArticle = true;

                //optional, but recommended
                //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
                //   doc.getDocumentElement().normalize();
                NodeList nodTitle // title of the page
                        = doc.getElementsByTagName("title");
                NodeList NodComment = doc.getElementsByTagName("comment");
                NodeList redirectNode = doc.getElementsByTagName("redirect");
                this.title = nodTitle.item(0).getTextContent();

                if (redirectNode.getLength() != 0 && (!this.title.contains(":"))) {
                    isaArticle = false; // It is REDIRECT entry - so it’s not an Article.
                    DatabaseFileBuilder.entryToProcess++;

                    RedirectEntry redirectEntry = new RedirectEntry(doc);
                    if (!redirectEntry.isEmpty()) {
                        this.writer.write(redirectEntry.asJSON());
                    }

                    return;// Start the next entry.
                }

                if (NodComment.getLength() == 0) // check whether its REDIRECT entry.
                {
                    if (NodComment.item(0) != null) {
                        if (NodComment.item(0).getTextContent().contains("REDIRECT")
                                || NodComment.item(0).getTextContent().contains("הועבר ל")) {
                            RedirectEntry redirectEntry = new RedirectEntry(doc);
                            isaArticle = false;// It is REDIRECT entry - so it’s not an Article.
                            this.writer.write(redirectEntry.asJSON());
                        }
                    }
                }

                if ((!this.title.contains(":"))
                        && (isaArticle == true)
                        && (this.title.equals("עמוד ראשי") == false)) { //Are we need to process the entry?
                    DatabaseFileBuilder.entryToProcess++;
                    //<editor-fold defaultstate="collapsed" desc="Good spot for debug">
                    // if (entryToProcess == 114) { ...   }
                    //    if (title.equals("הלך")) { ...  }
                    //   if (title.equals("דבר")) { ...   }
                    //  if (title.equals("כמר")) { ...    }
                    //</editor-fold>
                    nodTitle = doc.getElementsByTagName("text");
                    wiktionaryEntryTxt = nodTitle.item(0).getTextContent();

                    ArrayList<Article> articles = Article.getArticles(this.title, wiktionaryEntryTxt);

                    for (Iterator<Article> // Remove empty articles.
                            iter = articles.iterator(); iter.hasNext();) {
                        Article candidate = iter.next();
                        if (candidate.isEmpty()) {
                            iter.remove();
                        }
                    }
                    // Convert the articles to JSON and write them.
                    for (Article art : articles) {
                        JSON json = new JSON(art);

                        if (!json.isEmpty()) {
                            this.writer.write(json.toString());
                        }

                    }
                }

            } catch (Exception e) { // catch exception and prints necessary info.
                System.out.println(e);
                System.out.println("\tLine Count        : " + lineCount);
                System.out.println("\tEntry To Process  : " + DatabaseFileBuilder.entryToProcess);

                for (StackTraceElement el : e.getStackTrace()) {
                    System.out.println(el);
                }
                System.out.println("---------------------------------------------------------------------------------------------------");
                System.out.println("Title :\n\t" + title);
                System.out.println("Wiktionary Entry Text :");
                System.out.println(wiktionaryEntryTxt);
                writer.close();
                System.exit(0);
            }
        }
    }
}
