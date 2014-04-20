package dbFileBuilder;

import org.json.JSONObject;
import org.w3c.dom.Document;

/**
 * Class to represent redirect entry.<br>
 * Redirect entry contains two properties:
 * <ul>
 * <li><tt>title</tt> - The title of the entry.</li>
 * <li><tt>whereToRedirect</tt> - Reference to another entry.</li>
 * </ul>
 */
public class RedirectEntry {

    private String where_to_redirect;
    private String title;
    private boolean isEmpty = false;

    /**
     * Creates a new redirect entry.
     *
     * @param document to retrieve data from.
     */
    public RedirectEntry(Document document) {
        this.title = document.getElementsByTagName("title").item(0).getTextContent();

        this.where_to_redirect = document.getElementsByTagName("text").item(0).getTextContent();

        String fullText = this.title + this.where_to_redirect;
        if (fullText.contains("שורש")
                || fullText.contains("שרש")
                || fullText.contains("קטגוריה")
                || fullText.contains("גזרת")
                || fullText.contains("נספח")
                || fullText.contains(":")) {
            this.isEmpty = true;
            return;
        }
        where_to_redirect = where_to_redirect.replace("#REDIRECT", "");
        where_to_redirect = where_to_redirect.replace("#redirect", "");
        where_to_redirect = where_to_redirect.replace("#הפניה", "");
        where_to_redirect = where_to_redirect.replaceAll("[\\[\\]]*", "");
        where_to_redirect = where_to_redirect.trim();

        if (where_to_redirect.length() <= 2) {
            this.isEmpty = true;
            return;
        }
        if (this.where_to_redirect.contains("#")
                || this.where_to_redirect.contains("|")) {
            String[] arr = this.where_to_redirect.trim().split("[,\\|#\\ ]");
            this.where_to_redirect = arr[1].trim();
        }
        if (this.where_to_redirect.isEmpty()) {
            this.isEmpty = true;
        }
    }

    /**
     * Return JSON representation of this redirect entry.
     *
     * @return JSON representation of this entry.
     */
    public String asJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", this.title);
        jsonObject.put("where_to_redirect", this.where_to_redirect);

        return jsonObject.toString() + '\n';
    }

    /**
     * Returns <tt>true</tt> if, and only if, this object contain no properties.
     *
     * @return <tt>true</tt> if, and only if, this object contain on properties,
     * otherwise <tt>false</tt>
     */
    public boolean isEmpty() {
        return this.isEmpty;
    }
}
