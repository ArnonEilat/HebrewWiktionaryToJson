package dbFileBuilder.json;

import org.json.JSONArray;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class to extract translation values from HTML document.
 */
public class TranslationFinder {
    private JSONArray JSONTransArray;

    /**
     * Creates an object that tries to find a translation.<br>
     * To know whether a translation exists use the {@link #found() found}
     * method.
     *
     * @param subSectionTitles List of all the h3 tag in the document.
     * @param document         A HTML Document.
     */
    TranslationFinder(Elements subSectionTitles, Document document) {

        Elements transArray = document.getElementsByAttributeValueContaining("data-destinationLanguage", "אנגלית");

        this.JSONTransArray = new JSONArray();
        for (Element translation : transArray) {
            this.JSONTransArray.put(translation.text().toLowerCase());
        }

        if (this.JSONTransArray.length() != 0) {
            return;
        }




        for (Element h3 : subSectionTitles) {
            if (h3.text().equals("תרגום") == true) {
                Element translation = h3.nextElementSibling();
                Element englishEntry = null;

                if (translation == null) {
                    //    System.out.println("No translation for " + article.getTitle());
                    return;
                }
                for (Element li : translation.getElementsByTag("li")) {
                    if (li.text().contains("אנגלית") == true) {
                        englishEntry = li;
                        break;
                    }
                }
                if (englishEntry == null) {
                    return;
                }
                String englishEntryText = englishEntry.text();
                englishEntryText = englishEntryText.replaceAll("אנגלית:", "");
                englishEntryText = englishEntryText.replaceAll(" ", "");

                String[] strTransArray = englishEntryText.split("\\|");
                for (String s : strTransArray) {
                    s = s.toLowerCase();
                }
                JSONTransArray = new JSONArray(strTransArray);

            }
        }
        // Uncomment to find problems
        //if (document.toString().contains("אנגלית") && !found()) {
        //    System.out.println("This may be a problem");
        //}
    }

    /**
     * Return <tt>true</tt> if at least one translation was found, otherwise
     * return <tt>false</tt>.
     */
    public boolean found() {
        if (this.JSONTransArray == null)
            return false;
        if (this.JSONTransArray.length() == 0)
            return false;

        return true;
    }

    /**
     * Return JSON array with all the translations.
     */
    public JSONArray getTranslationArray() {
        return JSONTransArray;
    }


}

