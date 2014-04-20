package dbFileBuilder.article;

import dbFileBuilder.RegEx;
import java.util.regex.Pattern;

/**
 * Useful text replace operations.
 */
public final class SpecialTreatment {

    /**
     * Don't let anyone instantiate this class.
     */
    private SpecialTreatment() {
    }

    /**
     *
     * @param text
     * @return same as text Special Treatment
     */
    public static String act(String text) {
        text = text.replace("{{עשר המכות}}", "");
        text = text.replace("{{אבני החושן}}", "");
        text = text.replace("{{פירוש לקוי}}", "");

        text = text.replace("{{תחבורה ציבורית}}", "");

        // line break - we dont need them
        text = text.replaceAll("\\{\\{ש\\}\\}", "");
        text = text.replaceAll("\\<br\\>", "");
        text = text.replaceAll("\\<BR\\>", "");
        text = text.replaceAll("<br\\s*/>", "");

        /**
         * Remove all the references to footnotes.
         */
        text = text.replaceAll("\\{\\{הפניה[^\\}]+\\}\\}", "");
        // Footnotes to web site
        text = text.replaceAll("\\[((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)\\]", "");

        // Remove images
        text = text.replaceAll("\\[\\[קובץ:[^\\]]*\\]\\]", "");

        if (text.contains("תמונה=")) {
            text = RegEx.removeAll(text, Pattern.compile("\\{\\{[^\\}]+(תמונה=){1}[^\\}]+\\}\\}", Pattern.MULTILINE));
        }

        text = text.replaceAll("\\{\\{מקור\\}\\}", "");

        text = RegEx.removeAll(text, Pattern.compile("\\{\\{מספרים\\|[^}]*\\}\\}"));

        // make it easier to run on the DOM
        do {
            text = text.replaceAll("\n\n", "\n");
        } while (text.contains("\n\n"));

        return text;
    }
}
