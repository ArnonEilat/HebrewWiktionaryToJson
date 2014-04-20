package dbFileBuilder.article;

import dbFileBuilder.RegEx;
import dbFileBuilder.article.nituachDikduki.NituachDikduki;
import dbFileBuilder.wiki.Wiki;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;

/**
 * Class to handle the transformation from wiki to HTML.
 */
public class Article {

    private static Pattern pattern;
    private static Matcher matcher;
    private static Wiki wiki = new Wiki();
    /**
     * Text title of the article.
     */
    private String title;
    /**
     * Text content of the article.
     */
    private String content;
    private boolean stub = false;
    private boolean rewriting = false;
    private boolean isEntryCompleted = true;
    private String rootWord = "";
    /**
     * Store the categories of the Article.
     */
    private JSONArray categories = new JSONArray();
    private NituachDikduki nd;

    /**
     * Creates a new <tt>Article</tt>.
     *
     * @param title of the Article.
     * @param mwContent of the Article in media Wiki markup.
     * @see Article#getArticles
     */
    private Article(final String title, final String content) {
        this.title = title;
        this.content = content;

        if (NituachDikduki.isNeeded(this.content)) {
            this.nd = new NituachDikduki(this.content);
            this.content = this.nd.getWikiEntryWithoutND();
        }

        if (NSWMP.isAGoodEntry(this.content, this.title)) {
            this.content = SpecialTreatment.act(this.content);
            this.content = NSWMP.preprocessor(this.content);
            this.content = NSWMP.flattenTable(this.content);
            this.content = NSWMP.flattenIndent(this.content);
            this.content = NSWMP.convertToWikiIndent(this.content);

            this.findPatterns();
            this.content = wiki.process(this.content);
        } else {
            this.content = "";
        }
    }

    @Override
    public String toString() {
        return this.content;
    }

    /**
     * Returns <tt>true</tt> if the article is incomplete.<br>
     * A stub is an article deemed too short to provide full coverage of a
     * subject.
     *
     * @see <a
     * href="http://he.wikipedia.org/wiki/%D7%95%D7%99%D7%A7%D7%99%D7%A4%D7%93%D7%99%D7%94:%D7%A7%D7%A6%D7%A8%D7%9E%D7%A8">ויקיפדיה:קצרמר</a>
     */
    public boolean isAStub() {
        return this.stub;
    }

    /**
     * Returns <tt>true</tt> article is incomplete.
     *
     * @see <a
     * href="http://he.wikipedia.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%9C%D7%94%D7%A9%D7%9C%D7%99%D7%9D">תבנית:להשלים</a>
     */
    public boolean isCompleted() {
        return this.isEntryCompleted;
    }

    /**
     * @return Returns category list of the entry.
     * @see <a
     * href="http://en.wiktionary.org/wiki/Help:Category">Help:Category</a>
     */
    public JSONArray getCategories() {
        return this.categories;
    }

    /**
     * @return Returns <tt>true</tt> if there is categories, otherwise return
     * false.
     */
    public boolean thereIsCategories() {
        return this.categories.length() != 0;
    }

    /**
     * @return Returns <tt>true</tt> if there is '<i>acronym</i>'(ראשי תיבות) in
     * the categories array, otherwise return false.
     */
    public boolean isAcronym() {
        for (int i = 0; i < this.categories.length(); i++) {
            if (this.categories.getString(i).contains("ראשי תיבות")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns <tt>true</tt> if there is
     * {@link  dbFileBuilder.article.nituachDikduki.NituachDikduki NituachDikduki}
     * and its not empty, otherwise return false.
     *
     * @return <tt>true</tt> if there is NituachDikduki.
     */
    public boolean hasNituachDikduki() {
        if (this.nd == null) {
            return false;
        }
        if (this.nd.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * @return Nituach dikduki(ניתוח דיקדוקי) object of this Article.
     */
    public NituachDikduki getNituachDikduki() {
        return this.nd;
    }

    /**
     * Get raw text and cut it to separated {@link  Article}s
     *
     * @param title The title of all the {@link  Article}s
     * @param textToAnalyze raw text which will be the {@link  Article}s
     * @return list of Articles made of <tt>textToAnalyze</tt>.
     */
    public static ArrayList<Article> getArticles(String title, String textToAnalyze) {
        ArrayList<Article> listToReturn = new ArrayList<Article>();
        ArrayList<Integer> pos = new ArrayList<Integer>();
        String tmp;

        if (RegEx.find(Patterns.WRONG_SPELLING, textToAnalyze) == true) {
            // Those are not needed.
            //  Uncomment to watch them
            // System.out.println(textToAnalyze);
            return listToReturn;
        }

        pattern = Patterns.SECTION_HEADING;

        textToAnalyze = removeIrrelevants(textToAnalyze);

        matcher = pattern.matcher(textToAnalyze);
        while (matcher.find()) {
            if (matcher.group().contains("קישורים חיצוניים") == true) {
                continue;
            }
            if (matcher.group().contains("הערות שוליים") == true) {
                continue;
            }

            pos.add(matcher.start());
        }

        if (pos.isEmpty() == true) { // Just one dictionary entry in text.
            if (textToAnalyze.isEmpty()) {
                return listToReturn;
            }
            listToReturn.add(new Article(title, textToAnalyze));
            return listToReturn;
        }
        if (pos.size() == 1) { // Just one dictionary entry in text.
            listToReturn.add(new Article(title, textToAnalyze));
            return listToReturn;
        }

        if (pos.get(0) == 0) {
            pos.remove(0);
        }

        tmp = // Geting first article out of textToAnalyze
                textToAnalyze.substring(0, pos.get(0));
        listToReturn.add(new Article(title, tmp));

        for (int i = 0; i < pos.size() - 1; i++) // Geting articles out of textToAnalyze
        {
            tmp = textToAnalyze.substring(pos.get(i), pos.get(i + 1));
            listToReturn.add(new Article(title, tmp));
        }

        tmp = // Geting last article out of textToAnalyze
                textToAnalyze.substring(pos.get(pos.size() - 1), textToAnalyze.length());
        listToReturn.add(new Article(title, tmp));

        for (int i = 0; i < listToReturn.size(); i++) {
            if (listToReturn.get(i).content.isEmpty()) {
                listToReturn.remove(i);
            }
        }
        return listToReturn;
    }

    /**
     * @return Returns the title of the {@link Article}, with leading and
     * trailing whitespace omitted.
     */
    public String getTitle() {
        return title.trim();
    }

    /**
     * @return Returns the content of the {@link Article}.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * @return Returns the root word.
     * @see <a
     * href="http://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A9%D7%A8%D7%A9">
     * תבנית:שרש</a>
     */
    public String getRoot() {
        return this.rootWord;
    }

    /**
     * Remove irrelevant parts from <tt>text</tt> and fix some misspellings.<br>
     * The irrelevant parts are:
     * <ul>
     * <li>Table Of Contents.</li>
     * <li>Pirush nosaf.</li>
     * </ul>
     *
     * @param text to remove irrelevant parts from.
     * @return The same as <tt>text</tt> but without irrelevant parts.
     */
    private static String removeIrrelevants(String text) {
        text = text.replace("{{להשלים|כל הערך=כן}}", "");

        // Subheading should be H3 not h2 - fix it
        if (RegEx.find(Patterns.SEE_ALSO_MISTAKE, text) == true) {
            text = RegEx.replaceAll(text, Patterns.SEE_ALSO_MISTAKE, "===ראו גם===");
        }
        if (RegEx.find(Patterns.SYNONYM_MISTAKE, text) == true) {
            text = RegEx.replaceAll(text, Patterns.SYNONYM_MISTAKE, "===מילים נרדפות===");
        }

        if (RegEx.find(Patterns.ANOTHER_MEANING, text) == true) {
            text = RegEx.removeAll(text, Patterns.ANOTHER_MEANING);
        }

        /*
         * Markup say tohen einyanim(index). not needed! Chop text to start with
         * heading.
         */
        if (text.contains("{{תוכן}}")) {
            text = text.substring(8);
        }

        text = text.replaceAll("\\{\\{תוכן\\|.*[\\}\\}]", "");
        text = text.replaceAll("\\{\\{חסר\\|.*[\\}\\}]", "");

        text = // Strange Markup. TODO: what is it for?
                text.replaceAll("\\{\\{כ\\}\\}", "");

        if (RegEx.find(Patterns.SECTION_HEADING, text) == true) {
            while (text.length() != 0 && text.charAt(0) != '=') {
                text = text.substring(1);
            }
        }
        return text;
    }

    /**
     * Returns <tt>true</tt> if, and only if, the length of
     * {@link Article#content content} is
     * <tt>0</tt>.
     *
     * @return <tt>true</tt> if length of content is <tt>0</tt>, otherwise
     * <tt>false</tt>
     */
    public boolean isEmpty() {
        return this.content.isEmpty();
    }

    /**
     * Find simple wiki patterns, save them and then remove them from the
     * {@link Article#content content}.
     *
     * @return Nothing. its work on the content of this Article.
     */
    private void findPatterns() {
        if (RegEx.find(Patterns.STUB, this.content) == true) {
            this.content = RegEx.removeAll(this.content, Patterns.STUB);
            this.stub = true;
        }

        if (RegEx.find(Patterns.IS_COMPLETED, this.content) == true) {

            this.content = RegEx.removeAll(this.content, Patterns.IS_COMPLETED);
            this.isEntryCompleted = false;
        }

        if (RegEx.find(Patterns.ROOT, this.content) == true) {
            Pattern rootPattern = Patterns.ROOT;
            Matcher rootMatcher = rootPattern.matcher(this.content);
            rootMatcher.find();
            this.rootWord = rootMatcher.group().substring(7, rootMatcher.group().length() - 2);
            this.content = RegEx.removeAll(this.content, Patterns.ROOT);
        }

        if (RegEx.find(Patterns.REWRITING, this.content) == true) {
            this.content = RegEx.removeAll(this.content, Patterns.REWRITING);
            this.rewriting = true;
        }

        // Find categories
        Pattern categoriesPattern = Patterns.CATEGORIES;
        Matcher categoriesMatcher = categoriesPattern.matcher(this.content);
        while (categoriesMatcher.find()) {
            String category = categoriesMatcher.group().substring(10, categoriesMatcher.group().length() - 2);
            categories.put(category);
        }
        if (categories.length() > 0) {
            this.content = RegEx.removeAll(this.content, Patterns.CATEGORIES);
        }
    }
}
