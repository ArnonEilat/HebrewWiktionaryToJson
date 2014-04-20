package dbFileBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Writes text to a given file.
 */
public class Writer {

    private File file;
    private FileOutputStream fileOutputStream;
    private OutputStreamWriter outputStreamWriter;
    private BufferedWriter bufferWriter;

    /**
     * Constructs a Writer object given a file name.
     *
     * @param fileName The name of file to write.
     * @exception UnsupportedCharsetException If the <tt>file.encoding</tt>
     * {@link System#setProperty system} property is not set to
     * <tt>UTF-8</tt>
     * an <tt>UnsupportedCharsetException</tt> is thrown.
     */
    public Writer(String fileName) throws UnsupportedCharsetException {
        if (System.getProperty("file.encoding").equalsIgnoreCase("UTF-8") == false) {
            String explainString // Chack whether the encoding property is correct
                    = "\n\tThe system property: \"file.encoding\" should set to \"UTF-8\"."
                    + "\n\tSet it, and run the program again."
                    + "\n\tYou can set the \"file.encoding\" property by passing -Dfile.encoding=UTF-8 as argument to the JVM.\n";
            throw new UnsupportedCharsetException(explainString);
        }

        try {
            this.file = new File(fileName);

            if (this.file.exists() == false) {//if file doesnt exists, then create it
                this.file.createNewFile();
            }

            this.fileOutputStream = new FileOutputStream(this.file);
            this.outputStreamWriter = new OutputStreamWriter(this.fileOutputStream, Charset.forName("UTF8"));
            this.bufferWriter = new BufferedWriter(this.outputStreamWriter);

            //<editor-fold defaultstate="collapsed" desc="would you like to test?">
//            this.write("<!DOCTYPE html>\n"
//                    + "<html>\n"
//                    + "    <head>\n"
//                    + "       <title>פלט לבדיקה</title>\n"
//                    + "       <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
//                    + "       <META HTTP-EQUIV=\"CACHE-CONTROL\" CONTENT=\"NO-CACHE\">\n"
//                    + "       <link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\">\n"
//                    + "    </head>\n"
//                    + "    <body>\n");
            //</editor-fold>
        } catch (IOException ex) {
            System.out.println(ex);
            for (StackTraceElement el : ex.getStackTrace()) {
                System.out.println(el);
            }
        }
    }

    /**
     * Append data to the file.
     *
     * @param data to append.
     */
    public void write(String data) {
        try {
            this.bufferWriter.write(data);
        } catch (IOException ex) {
            System.out.println(ex);
            for (StackTraceElement el : ex.getStackTrace()) {
                System.out.println(el);
            }
        }
    }

    /**
     * Close the <tt>writer</tt>.
     */
    public void close() {
        try {
            this.bufferWriter.close();
        } catch (IOException ex) {
            System.out.println(ex);
            for (StackTraceElement el : ex.getStackTrace()) {
                System.out.println(el);
            }
        }
    }

    @Override
    public String toString() {
        return this.file.toString();
    }
}
