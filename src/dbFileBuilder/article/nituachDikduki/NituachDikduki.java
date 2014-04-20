package dbFileBuilder.article.nituachDikduki;

import dbFileBuilder.article.nituachDikduki.finders.NetiyotFinder;
import dbFileBuilder.article.nituachDikduki.finders.BuildingFinder;
import dbFileBuilder.article.nituachDikduki.finders.GenderFinder;
import dbFileBuilder.article.nituachDikduki.finders.RootFinder;
import dbFileBuilder.article.nituachDikduki.finders.DerechTatzuraFinder;
import dbFileBuilder.RegEx;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;

/**
 * The {@link  dbFileBuilder.wiki.Wiki Wiki} library does not support tables, but
 * we still want to keep the Nituach Dikduki (grammatical analysis) section in
 * the database.<br>
 * This class process wiktionary entry in non-structured way.<br>
 * The purpose of the process is to retrieve as much information as possible
 * from the Nituach Dikduki section, keep it in internal representation, and to
 * remove it from the wiktionary entry.
 * <br>
 * Usage:<br>
 * Before creating an instance of the class the static
 * {@link  #isNeeded isNeeded} method should be called.
 */
public class NituachDikduki {

    /**
     *
     * See {@link  #isNeeded isNeeded} method documentation.
     */
    private static Position lastTime = new Position();
    /**
     * Holds the Ktiv hasar niqqud (כתיב מלא) if exists
     *
     * @see <a
     * href="http://he.wikipedia.org/wiki/%D7%9B%D7%AA%D7%99%D7%91_%D7%97%D7%A1%D7%A8_%D7%A0%D7%99%D7%A7%D7%95%D7%93">כתיב
     * חסר ניקוד</a>
     */
    private String ktivMale = "";
    /**
     * Holds the pronunciation (הגייה) if exists
     *
     * @see <a
     * href="http://he.wiktionary.org/wiki/%D7%95%D7%99%D7%A7%D7%99%D7%9E%D7%99%D7%9C%D7%95%D7%9F:%D7%AA%D7%A2%D7%AA%D7%99%D7%A7_%D7%A4%D7%95%D7%A0%D7%98%D7%99">ויקימילון:תעתיק
     * פונטי</a>
     */
    private String pronunciation = "";
    /**
     * Holds the pronunciation (חלקי הדיבר) if exists
     *
     * @see <a
     * href="http://he.wiktionary.org/wiki/%D7%95%D7%99%D7%A7%D7%99%D7%9E%D7%99%D7%9C%D7%95%D7%9F:%D7%90%D7%99%D7%9A_%D7%9C%D7%9B%D7%AA%D7%95%D7%91_%D7%A0%D7%99%D7%AA%D7%95%D7%97_%D7%93%D7%A7%D7%93%D7%95%D7%A7%D7%99#.D7.97.D7.9C.D7.A7.D7.99_.D7.94.D7.93.D7.99.D7.91.D7.95.D7.A8">ויקימילון:חלקי
     * הדיבור</a>
     */
    private String lexicalClass = "";
    /**
     * Holds the gender (מין) if exists
     */
    private String gender = "";
    /**
     * Holds the root word (שורש) if exists
     */
    private String rootWord = "";
    /**
     * Holds the form (גזרה) if exists
     */
    private String form = "";
    /**
     * Holds the building (בניין) if exists
     */
    private String building = "";
    /**
     * Holds the netiyot (נטיות) if exists
     */
    private String netiyot = "";
    /**
     * Holds the derech Tatzura (דרך תצורה) if exists
     */
    private String derechTatzura = "";
    private Pattern pattern;
    private Matcher matcher;
    private String wikiEntryWithoutND;
    private static boolean isNeeded;
    /**
     * Constant Value : {@value}.
     */
    private final static String splitter = "\\|(?!(?:(?![\\{\\[]|[\\]\\}]).)*[\\}\\]])";
    private Iterator<String> iterator;

    /**
     * //TODO: complete NituachDikduki constructor.
     *
     * @see
     * <a
     * href="http://he.wiktionary.org/wiki/%D7%95%D7%99%D7%A7%D7%99%D7%9E%D7%99%D7%9C%D7%95%D7%9F:%D7%90%D7%99%D7%9A_%D7%9C%D7%9B%D7%AA%D7%95%D7%91_%D7%A0%D7%99%D7%AA%D7%95%D7%97_%D7%93%D7%A7%D7%93%D7%95%D7%A7%D7%99">ויקימילון:איך
     * לכתוב ניתוח דקדוקי</a>
     * @param hebWikiEntry hebrew Wiktionary entry.
     */
    public NituachDikduki(final String hebWikiEntry) {

        if (isNeeded == false) {
            System.err.println("isNeeded is false.\nCanot initiate instance.\n");
            return;
        }

        String beforeND = hebWikiEntry.substring(0, NituachDikduki.lastTime.start);
        String afterND = hebWikiEntry.substring(NituachDikduki.lastTime.end + 1);

        this.wikiEntryWithoutND = beforeND + afterND;

        String ndSection = // Trim the entry to be just the Nituach Dikduki section
                hebWikiEntry.substring(NituachDikduki.lastTime.start, NituachDikduki.lastTime.end);

        ArrayList<String> section
                = new ArrayList<String>(Arrays.asList(ndSection.split(splitter)));

        for (iterator = section.iterator(); iterator.hasNext();) {
            String str = iterator.next();
            if (!str.contains("=") || str.length() < 2 || str.equals("\n")) {
                iterator.remove();
            }
        }
        String last = section.get(section.size() - 1);
        section.remove(section.size() - 1);
        if (Util.count(last, "}}") == 1) {
            last = last.substring(0, last.length() - 2);
        } else if (Util.count(last, "}}") == 2) {
            if (Util.count(last, "{{") == 1) {
                last = last.substring(0, last.length() - 2);
            }
        }
        section.add(last);

        // Look for Ktiv Male
        for (iterator = section.iterator(); iterator.hasNext();) {
            String str = iterator.next();

            if (str.contains("כתיב מלא=")) {
                pattern = Pattern.compile("כתיב מלא=", Pattern.UNICODE_CASE);
                matcher = pattern.matcher(str);
                matcher.find();

                String tmp = str.substring(matcher.end(), str.length() - 1);
                tmp = tmp.trim();
                tmp = tmp.replaceAll("\\{\\{הפניה\\|[^\\}.]*\\}\\}", "");
                tmp = Util.toPlain(tmp);
                //  String[] ktivMaleArr = tmp.split("([']*|[\\(]*)[^\\u05D0-\\u05EA](וגם|גם|או|ואו)[^\\u05D0-\\u05EA][\\:'\\ ]*");

                if (tmp.length() <= 1) {
                    tmp = "";
                }
                ktivMale = tmp;

                iterator.remove();
                break;
            }
        }

        // Look for hagaya
        for (iterator = section.iterator(); iterator.hasNext();) {
            String str = iterator.next();

            if (str.contains("הגייה=")) {
                str = str.replace("הגייה=", "");
                str = Util.toHtml(str);
                if (!str.isEmpty()) {
                    pronunciation = str;
                }
                iterator.remove();
                break;
            }
        }

        // Look for gender
        for (iterator = section.iterator(); iterator.hasNext();) {
            String str = iterator.next();
            if (str.contains("מין=")) {
                gender = GenderFinder.find(str);
                iterator.remove();
                break;
            }
        }

        // Look for Root word
        for (iterator = section.iterator(); iterator.hasNext();) {
            String str = iterator.next();

            if (str.contains("שורש=") || str.contains("שורש וגזרה=")) {
                RootFinder rootFinder = new RootFinder(str);
                rootWord = rootFinder.getRoot();
                form = rootFinder.getGizra();
                iterator.remove();
                break;
            }
        }

        // Look for Building 
        for (iterator = section.iterator(); iterator.hasNext();) {
            String str = iterator.next();

            if (str.contains("בניין=")) {
                building = BuildingFinder.find(str);
                iterator.remove();
                break;
            }
        }

        // Look for Netyot 
        for (iterator = section.iterator(); iterator.hasNext();) {
            String str = iterator.next();

            if (str.contains("נטיות=")) {
                netiyot = NetiyotFinder.find(str);
                //if (!netiyot.isEmpty()) //Uncomment to find problems
                //    System.out.println(netiyot);
                //  iterator.remove();
                break;
            }
        }

        // Look for tatzura
        for (iterator = section.iterator(); iterator.hasNext();) {
            String str = iterator.next();

            if (str.contains("דרך תצורה=")) {
                derechTatzura = DerechTatzuraFinder.find(str);

                iterator.remove();
                break;
            }
        }
    }

    /**
     * Check whether Nituach Dikduki section is exists.<br>
     * If so, an {@link Position#end end} and {@link Position#start start}
     * positions of the <i>Nituach Dikduki</i>
     * section are assign to {@link  NituachDikduki#lastTime <tt>lastTime</tt>}
     * to be used by {@link NituachDikduki} constructor.
     *
     * @param entryText String with the contents of the entry.
     * @return <tt>true</tt> if Nituach Dikduki section is exists, otherwise
     * <tt>false</tt>.
     */
    public static boolean isNeeded(String entryText) {
        /*
         * If 'stack' is zero at the end of the method: * The lastTime point to
         * the end of "Nituach Dikduki" section. * The open curly bracket equals
         * to the close curly bracket.
         */
        int stack = 0;
        NituachDikduki.lastTime.start = 0;
        NituachDikduki.lastTime.end = 0;
        int index;
        if (!entryText.contains("ניתוח דקדוקי")) { // No Nituach Dikduki found.
            NituachDikduki.isNeeded = false;
            return false;
        } else {
            NituachDikduki.lastTime.start = entryText.indexOf("{{ניתוח דקדוקי", 0);
            NituachDikduki.lastTime.end = NituachDikduki.lastTime.start;
            index = NituachDikduki.lastTime.end;
            NituachDikduki.isNeeded = true;
        }
        do {
            if (Util.stringAt(entryText, index).equals("{{")) {// Push stack
                stack++;
            } else if (Util.stringAt(entryText, index).equals("}}")) { // Pop stack
                stack--;
            }
            if (stack == 0) {// The stack is empty
                break;
            }
            index++;
        } while (index < entryText.length());
        index++;
        NituachDikduki.lastTime.end = index;
        return true;
    }

    @Override
    public String toString() {
        return "\nNituachDikduki { "
                + "\n\tktivMale      = " + ktivMale
                + "\n\tpronunciation = " + pronunciation
                + "\n\tlexicalClass  = " + lexicalClass
                + "\n\tgender        = " + gender
                + "\n\trootWord      = " + rootWord
                + "\n\tform          = " + form
                + "\n\tbuilding      = " + building
                + "\n\netiyot        = " + netiyot
                + "\nderechTatzura   = " + derechTatzura
                + "\n}";
    }

    /**
     * Return the Wiki entry construct this {@link NituachDikduki} object
     * without the text of the nituach dikduki.
     *
     * @return Text of wiki entry without the text of the nituach dikduki.
     */
    public String getWikiEntryWithoutND() {
        return this.wikiEntryWithoutND;
    }

    /**
     * Returns <tt>true</tt> if, and only if, the NituachDikduki is empty.
     *
     * @return <tt>true</tt> the NituachDikduki is empty, otherwise
     * <tt>false</tt>
     */
    public boolean isEmpty() {
        return ktivMale.isEmpty()
                && pronunciation.isEmpty()
                && lexicalClass.isEmpty()
                && gender.isEmpty()
                && rootWord.isEmpty()
                && form.isEmpty()
                && building.isEmpty()
                && netiyot.isEmpty()
                && derechTatzura.isEmpty();
    }

    /**
     * @return JSON representation of the object.
     */
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        if (ktivMale.length() != 0) {
            obj.accumulate("ktivmale", ktivMale);
        }

        if (!"".equals(pronunciation)) {
            obj.accumulate("pronunciation", pronunciation.replaceAll("\n", ""));
        }

        if (!"".equals(lexicalClass)) {
            obj.accumulate("lexicalclass", lexicalClass.replaceAll("\n", ""));
        }

        if (!"".equals(gender)) {
            obj.accumulate("gender", gender.replaceAll("\n", ""));
        }

        if (!"".equals(rootWord)) {
            obj.accumulate("rootword", rootWord.replaceAll("\n", ""));
        }

        if (!"".equals(form)) {
            obj.accumulate("form", form.replaceAll("\n", ""));
        }

        if (!"".equals(building)) {
            obj.accumulate("building", building.replaceAll("\n", ""));
        }

        if (!"".equals(netiyot)) {
            obj.accumulate("netiyot", netiyot.replaceAll("\n", ""));
        }

        if (!"".equals(derechTatzura)) {
            obj.accumulate("derechtatzura", derechTatzura.replaceAll("\n", ""));
        }

        return obj;
    }

    /**
     * Returns <tt>true</tt> if, and only if, the {@link #ktivMale ktivMale}
     * (כתיב מלא) is exists in this object.
     *
     * @return <tt>true</tt> if the {@link #ktivMale ktivMale} (כתיב מלא) is
     * exists.
     */
    public boolean hasKtivMale() {
        return !ktivMale.isEmpty();
    }

    /**
     * Gets all the {@link #ktivMale ktivMale} (כתיב מלא) in array.<br>
     * Each entry of the array contains one way of writing the entry (Sometimes
     * there is more than one way of writing).
     *
     * @return ArrayList with all the {@link #ktivMale ktivMale}.
     */
    public String[] getKtivMale() {
        String[] arr = this.ktivMale.split(" או |,");

        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].trim();
        }
        return arr;
    }

    /**
     * Returns <tt>true</tt> if, and only if, the {@link #netiyot netiyot}
     * (נטיות) is exists in this object.
     *
     * @return <tt>true</tt> if the {@link #netiyot netiyot} (נטיות) is exists.
     */
    public boolean hasNetiyot() {
        return !this.netiyot.isEmpty();
    }

    /**
     * Gets all the {@link #netiyot netiyot} (נטיות) in ArrayList.<br>
     * Each entry of the array contains one netiya (נטיה אחת)
     *
     * @return ArrayList with all the {@link #netiyot netiyot} (נטיות).
     */
    public ArrayList<String> getNetiyot() {
        /*
         * //TODO: replace the Nikudd with ktiv male as describe here:
         * http://hebrew-academy.huji.ac.il/hahlatot/MissingVocalizationSpelling/Pages/ikkar.aspx
         */
        String separator = "@";// just a sing
        String tmp = this.netiyot;
        int UC = Pattern.UNICODE_CASE;

        tmp = RegEx.replaceAll(tmp, Pattern.compile("[,;]{1}|(ר\"[זני'])|ר'|(נ\"[רי])|נ'|ס\"ר|ס\"ז", UC), separator);
        tmp = RegEx.replaceAll(tmp, Pattern.compile("אוֹ ", UC), separator);
        tmp = RegEx.replaceAll(tmp, Pattern.compile(" או ", UC), separator);
        tmp = RegEx.replaceAll(tmp, Pattern.compile("\u05BE", UC), separator);// \u05BE is HEBREW PUNCTUATION MAQAF
        tmp = RegEx.replaceAll(tmp, Pattern.compile("ר׳", UC), separator);//  RESH follow by HEBREW PUNCTUATION GERESH(\u05F3)
        tmp = RegEx.replaceAll(tmp, Pattern.compile("נ׳ ", UC), separator);//  NUN follow by HEBREW PUNCTUATION GERESH(\u05F3)
        tmp = RegEx.replaceAll(tmp, Pattern.compile("\\-", UC), separator);
        tmp = RegEx.replaceAll(tmp, Pattern.compile("נ״ר", UC), separator);
        tmp = RegEx.replaceAll(tmp, Pattern.compile("ס\"נ", UC), separator);
        tmp = RegEx.replaceAll(tmp, Pattern.compile("ס״ר ", UC), separator);
        tmp = RegEx.replaceAll(tmp, Pattern.compile("ס' ", UC), separator);
        tmp = RegEx.replaceAll(tmp, Pattern.compile(" גם ", UC), separator);
        tmp = RegEx.replaceAll(tmp, Pattern.compile(separator + "[\\ ]*", UC), separator);

        ArrayList<String> arr = new ArrayList<String>(Arrays.asList(tmp.split(separator)));

        for (Iterator<String> it = arr.iterator(); it.hasNext();) {
            String b = it.next().trim();
            if (b.isEmpty()) {
                it.remove();
            }
        }
        for (String netiya : arr) {
            netiya = netiya.trim();
            //Uncomment to find problems
            // System.out.println(netiya);
        }

        return arr;
    }
}
