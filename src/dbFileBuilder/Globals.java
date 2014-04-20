package dbFileBuilder;

/**
 * 
 * Global setting.
 */
public class Globals {

    /**
     * Enables Unicode-aware case folding.
     *
     * <p>
     * When this flag is specified then case-insensitive matching, when enabled
     * by the {@link #CASE_INSENSITIVE} flag, is done in a manner consistent
     * with the Unicode Standard. By default, case-insensitive matching assumes
     * that only characters in the US-ASCII charset are being matched.
     */
    //  public static final int REGEX_FLAGS = Pattern.UNICODE_CASE;
    /**
     * The name of output file.
     */
    public static final String OUTPUT_FILE_NAME = "output.json";
    /**
     * The name of input file.
     */
    public static final String INPUT_FILE_NAME = "multistreams/hewiktionary-latest-pages-articles-multistream.xml";
}
