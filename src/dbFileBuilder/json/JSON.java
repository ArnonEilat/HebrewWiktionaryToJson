package dbFileBuilder.json;

import dbFileBuilder.RegEx;
import dbFileBuilder.article.Article;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONObject;

/**
 * Convert {@link Article} to <a href="http://www.json.org/">JSON</a>
 * representation.
 */
public class JSON {

    private static Pattern isHebrew = Pattern.compile("\\p{InHebrew}+", Pattern.UNICODE_CASE);
    private static Pattern nonPrintableCharacters = Pattern.compile("[\\p{C}\u200F]*", Pattern.UNICODE_CASE);
    private Document document;
    private ArrayList<String> look_by;
    private JSONObject jsonObject;
    private Article article;
    /**
     * count article which can't be process.
     */
    public static int defect = 0;

    /**
     *
     * @param article to convert to JSON object.
     */
    public JSON(final Article article) {
        this.document = Jsoup.parse(article.getContent());
        this.normalize();
        this.look_by = new ArrayList<String>();
        this.jsonObject = new JSONObject();
        this.article = article;

        if (article.hasNituachDikduki()) {
            this.jsonObject.put("nituach_dikduki", article.getNituachDikduki().toJSON());
        }

        if (article.thereIsCategories() == true) {
            this.jsonObject.put("categories", article.getCategories());
        }

        if (article.hasNituachDikduki()) {
            if (article.getNituachDikduki().hasKtivMale()) {
                String[] ktivMale = article.getNituachDikduki().getKtivMale();
                this.look_by.addAll(Arrays.asList(ktivMale));
            }

            if (article.getNituachDikduki().hasNetiyot()) {
                ArrayList<String> netiyot = article.getNituachDikduki().getNetiyot();
                for (String netiya : netiyot) {
                    if (!this.look_by.contains(netiya)) {
                        this.look_by.add(netiya);
                    }
                }
            }
        }

        Element sectionTitle;  // The title of the entry
        sectionTitle = document.getElementsByTag("h2").first();
        if (sectionTitle == null) {
            sectionTitle = document.getElementsByTag("h3").first();
        }

        if (sectionTitle == null) {
            // System.out.println("sectionTitle == null");
            JSON.defect++;
        } else {

            /*
             * if the title from XML <title> tag is not equal to the title
             * display in the web page: take the title in the tag to lookBy
             * array and set the display title as title.
             */
            if (article.getTitle().equals(sectionTitle.text().trim()) == false) {

                String candidateTitle = JSONUtil.removeNewLine(sectionTitle.text().trim());

                //    System.out.println(candidateTitle);
                if (candidateTitle.contains("מילים נרדפות")) {
                    jsonObject.accumulate("title", article.getTitle());

                } else {
                    this.generateTitle(candidateTitle);
                }

            } else {
                String title = JSONUtil.removeNewLine(sectionTitle.text().trim());
                jsonObject.accumulate("title", title);

                if (article.isAcronym() == true) {
                    if (title.contains("\"")) {
                        if (title.length() >= 6 && title.length() <= 8) {
                            title = title.replace("\"", "");
                            jsonObject.accumulate("title", title);
                        }
                    }
                }
            }

            ExplanationGenerator.findExplanation(sectionTitle, this);

            Elements subSectionTitles = document.getElementsByTag("h3");
            this.findGizron(subSectionTitles);

            TranslationFinder tf = new TranslationFinder(subSectionTitles, document);
            if (tf.found()) {
                JSONArray translationArr = tf.getTranslationArray();

                this.jsonObject.put("translation", translationArr);
                // Add the translations to look_by
                for (int i = 0; i < translationArr.length(); i++) {
                    look_by.add(translationArr.getString(i));
                }
            }

            if (this.jsonObject.get("title") instanceof String) {
                look_by.add(this.jsonObject.getString("title"));
            } else {
                JSONArray a = this.jsonObject.getJSONArray("title");
                for (int i = 0; i < a.length(); i++) {
                    look_by.add(a.getString(i));
                }
            }
            if (!look_by.isEmpty()) {
                this.jsonObject.put("look_by", look_by);
            }

        }
        /*
         * TODO: מילים נרדפות
         * TODO: צירופים
         * TODO: ראו גם
         * TODO: תואר השם
         * TODO: ניגודים
         */
//        if (this.jsonObject.has("look_by"))
//            System.out.println("title: " + this.jsonObject.get("title").toString() + " look_by: " + this.jsonObject.get("look_by").toString());
    }

    /**
     * An attempt to find Gizron(etymology). If the gizron was found its added
     * to the object.
     *
     * @param subSectionTitles list of all the h3 tag in the document.
     */
    private void findGizron(Elements subSectionTitles) {
        for (Element h3 : subSectionTitles) {
            if (h3.text().equals("גיזרון") == true) {
                Element gizron = h3.nextElementSibling();
                if (gizron == null) {
                    //         System.out.println("");
                }
                if (gizron.tagName().equals("ol") || gizron.tagName().equals("ul")) {
                    gizron = gizron.child(0);
                }
                jsonObject.put("etymology", JSONUtil.removeNewLine(gizron.html()));
                return;
            }
        }
    }

    /**
     * An attempt to remove tags without meaning.
     */
    private void normalize() {
        for (Element el : document.select("*")) {
            if (!el.hasText() || el.text().length() == 0) {
                el.remove();
            }
        }
    }

    @Override
    public String toString() {
        return RegEx.removeAll(jsonObject.toString().toLowerCase(), nonPrintableCharacters) + '\n';
    }

    /**
     * Returns <tt>true</tt> if, and only if, this object contain on properties.
     *
     * @return <tt>true</tt> if, and only if, this object contain on properties,
     * otherwise <tt>false</tt>
     */
    public boolean isEmpty() {
        if (jsonObject.length() < 3) {
            return true;
        }

        return jsonObject.toString().equals("{}");
    }

    private void generateTitle(final String ttl) {
        String[] sep = null;

        if (ttl.indexOf("(גם") != -1 || ttl.indexOf("(וגם") != -1) {
            if (ttl.indexOf("(גם") != -1) {
                sep = ttl.split("\\u0028\\u05d2\\u05dd");// גם)
            } else if (ttl.indexOf("(וגם") != -1) {
                sep = ttl.split("\\u0028\\u05d5\\u05d2\\u05dd");// (וגם
            }
            jsonObject.accumulate("title", sep[0].trim());

            if (sep[0].indexOf(",") != -1) {
                String additionalValue = sep[1];
                additionalValue = additionalValue.replaceAll("[():]", "");
                jsonObject.accumulate("title", additionalValue.trim());
            } else {
                String additionalValues[] = sep[1].split(",");
                for (String vlue : additionalValues) {
                    vlue = vlue.replaceAll("[():]", "");
                    jsonObject.accumulate("title", vlue.trim());
                }
            }
        } else {
            jsonObject.accumulate("title", ttl);
        }
    }

    /**
     * Put a key/value pair in the JSONObject. If the value is null, then the
     * key will be removed from the JSONObject if it is present.
     *
     * @param key A key string.
     * @param value An object which is the value. It should be of one of these
     * types: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or
     * the JSONObject.NULL object.
     */
    public void put(String key, Object value) {
        jsonObject.put(key, value);
    }
    /**
     * Not in use private static boolean isEnglish(String text) { return
     * !RegEx.find(isHebrew, text); }
     */
}
