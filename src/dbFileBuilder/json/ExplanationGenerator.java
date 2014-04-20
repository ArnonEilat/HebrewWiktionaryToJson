package dbFileBuilder.json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Create explanation
 */
public class ExplanationGenerator {

    /**
     * Reference to the {@link JSON} object who call
     * {@link  ExplanationGenerator#findExplanation findExplanation} method.
     */
    private static JSON caller;
    /**
     * {@link java.util.regex.Pattern Pattern} to identify open tags of the
     * following types: <tt>UL, OL, DL, DD, LI</tt>.
     */
    private static Pattern OPENING_BLOCK_TAG = Pattern.compile("<\\s*(ul|ol|dl|dd|li).*?>");
    /*
     * NOT IN USE! static Pattern CLOSING_BLOCK_TAG =
     * Pattern.compile("</\\s*(ul|ol|dl|dd|li).*?>");
     */

    /**
     * An attempt to find explanation.
     * <pre>
     *{
     *    "explanations": [
     *        {
     *            "description": "First description text",
     *            "usage-examples": [
     *                "First usage example",
     *                "Second usage example",
     *                "Third usage example"
     *            ]
     *        },
     *        {
     *            "description": "Second description text",
     *            "usage-examples": [
     *                "First usage example",
     *                "Second usage example",
     *                "Third usage example"
     *            ]
     *        }
     *    ]
     *}</pre>
     *
     * @param sectionTitle The explanation suppose to be under (a sibling) the
     * section title.<br>The section title should be h2 tag.
     * @param caller The {@link JSON} object who call this method.
     */
    protected static void findExplanation(Element sectionTitle, JSON caller) {
        ExplanationGenerator.caller = caller;
        JSONArray // To store all the explanations and examples
                explanationList = new JSONArray();

        Element //The next element after the h2
                explanationSection = sectionTitle.nextElementSibling();

        for (int i = 0; i < explanationSection.children().size(); i++) // iterat all the explanations
        {
            Element // current element to work on 
                    ex = explanationSection.children().get(i);
            JSONArray // Store all the direct nested element
                    explanationList4OneExample = new JSONArray();
            String oneExample;

            if (hasABlockChild(ex)) {
                String textTillStartOfNextBlock = getTextOfFather(ex);
                oneExample = JSONUtil.removeNewLine(textTillStartOfNextBlock);

                // Unwarp unnecessary tags
                while (ex.children().size() == 1 && ex.isBlock()) {
                    ex = ex.child(0);
                }
                if (!ex.isBlock()) // Walking to deep up the DOM tree?
                {
                    ex = ex.parent(); // Roll back
                }
                for (Element el : ex.children()) {//
                    Element ptr = el;// access via reference
                    boolean goUp = true;
                    while (ptr.isBlock()) { // Unwarp unnecessary tags
                        if (ptr.children().isEmpty()) {
                            if (ex.isBlock() == true)// its a block - dont go up.
                            {
                                goUp = false;
                            }
                            break;
                        }
                        ptr = ptr.child(0);
                    } // End while

                    if (goUp == true)// Walking to deep up the DOM tree?
                    {
                        ptr = ptr.parent(); // Roll back
                    }
                    String candidate = JSONUtil.removeNewLine(ptr.html());
                    boolean needToAdd = true;
                    for (int j = 0; j < explanationList4OneExample.length(); j++) {
                        String arrEntry = explanationList4OneExample.getString(j);
                        if (arrEntry.equals(candidate)) {
                            needToAdd = false;
                            break;
                        }
                    }
                    if (needToAdd) {
                        explanationList4OneExample.put(candidate);
                    }
                }
            } else {
                oneExample = ex.html();
            }

            // The following code check witch parts are not empty, and if so - add them.
            boolean needToAdd = false;
            JSONObject explanObject = new JSONObject();

            if (!oneExample.isEmpty()) {
                explanObject.put("description", oneExample);
                needToAdd = true;
            }

            if (explanationList4OneExample.length() > 0) {
                explanObject.put("example_list", explanationList4OneExample);
                needToAdd = true;
            }
            if (needToAdd == true) {
                explanationList.put(explanObject);
            }
        }// End for

//      System.out.println(explanationList);
        if (explanationList.length() > 0) {
            ExplanationGenerator.caller.put("explanation", explanationList);
        }

    }

    /**
     * If this method has been called <tt>element</tt> own children. Therefore
     * the method extract the and remove <a
     * href="https://developer.mozilla.org/en-US/docs/Web/API/Node.nodeType">text</a>
     * of <tt>element</tt> and return it.<br>
     * If no text node were found the <a
     * href="https://developer.mozilla.org/en-US/docs/Web/API/element.innerHTML">innerHTML</a>
     * of <tt>element</tt> itself returned.
     * <br><br>
     * <b><tt>element</tt> is modified! </b>
     *
     * @param element to extract text from.
     * @return text of <tt>{@literal element} </tt>
     */
    private static String getTextOfFather(Element element) {
        String innerHTML = element.html();

        Pattern p = OPENING_BLOCK_TAG;
        Matcher m = p.matcher(innerHTML);

        if (!m.find()) {
            try {
                // System.out.println("textTillStartOfNextBlock not found");
                throw new Exception("get Text Of Father: Error");
            } catch (Exception exception) {
                System.out.println(exception);
            }
        }

        int start = m.start() - 1;
        int end = element.html().length();

        if (start < 0) // nothing has been found. Malformed document.
        {
            while (element.isBlock()) {
                if (element.children().isEmpty()) {
                    break;
                }
                element = element.child(0);
            } // End while

            return element.html();
        }
        String stringToReturn = innerHTML.substring(0, start).trim();
        String html = innerHTML.substring(start, end);
        element.html(html);
        return stringToReturn;
    }

    /**
     * @return Returns <tt>true</tt> if, and only if, <tt>elm</tt> has at list
     * one child {@link  Element} with the following {@link Element#tag() tags}:
     * <tt>ul,ol,dl,dd,li</tt>
     */
    private static boolean hasABlockChild(Element elm) {
        Elements list = elm.select("ul,ol,dl,dd,li");
        if (list.size() > 1) {
            return true;
        }

        return false;
    }
}
