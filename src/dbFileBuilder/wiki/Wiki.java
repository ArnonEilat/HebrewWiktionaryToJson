package dbFileBuilder.wiki;

import dbFileBuilder.utilities.ArrayUtils;
import java.util.ArrayList;

/**
 * Convert Wiki markup to HTML.<br>
 * Supported Syntax:
 * <li>Heading (Levels 6 to 2): <tt>== Heading ==</tt> .
 * <li>Links: <tt>[http://www.url.com|Name of URLs]</tt>
 * <li>images: <tt>[[File:http://www.url.com/image.png|alt=Alternative
 * Text]]</tt>
 * <li>Horizontal line: <tt>----</tt>
 * <li>Indentation: <tt>:</tt>
 * <li>Ordered bullet point: <tt>#</tt>
 * <li>Unordered bullet point: <tt>*</tt>
 * <li>Definition List: <tt>;:</tt>
 */
public class Wiki {

    /**
     * Convert Wiki markup to HTML.<br>
     *
     * @param wikiText to convert to HTML.
     * @return HTML representation of {@code wikiText}
     */
    public String process(String wikiText) {
        String[] linesArray = arrayOfBloks(wikiText);
        String HalfBaked = "";
        for (String line : linesArray) {
            // For each bloke of text: 
            //      if it free text Wrap with P tag.
            //      Otherwise append it as is.
            if (Util.isNormalText(line) == true) {
                String[] a = Util.splitByNewLineSign(line);
                line = ArrayUtils.join(a, "<br/>\n");
                HalfBaked += "<p>" + line + "</p>\n";
            } else {
                HalfBaked += line + '\n';
            }
        }

        return jobManager(HalfBaked);
    }

    private String jobManager(String str) {
        String stringToReturn = "";
        if (ListUtil.isAList(str) == true) {
            String[] linesArray = arrayOfBloks(str);
            for (int i = 0; i < linesArray.length; i++) {
                if (ListUtil.isAList(linesArray[i]) == true) {
                    stringToReturn += list(linesArray[i]);
                    stringToReturn = WikiTextProcessor.processLine(stringToReturn);
                } else {
                    String[] notAListArray = Util.splitByNewLineSign(linesArray[i]);
                    for (String line : notAListArray) {
                        line = WikiTextProcessor.processLine(line);
                        stringToReturn += '\n' + line;
                    }
                }
            }

            return Util.removeNewLineSignFromStart(stringToReturn);
        } else {
            String[] notAListArray = Util.splitByNewLineSign(str);
            for (String line : notAListArray) {
                line = WikiTextProcessor.processLine(line);
                stringToReturn += '\n' + line;
            }
            return Util.removeNewLineSignFromStart(stringToReturn);
        }
    }

    private String list(String match) {
        String type = ListUtil.getListTagForString(match);

        String innerType = // dl if the type is dd. Otherewith li
                ListUtil.getInnerListTag(type);

        match = // Strip first layer of list
                ListUtil.removeFirstListSign(match);

        match = jobManager(match);
        String tmp = Util.removeNewLineSignFromStart(match);
        String[] arr = Util.splitByNewLineSign(tmp);
        String str = ArrayUtils.join(arr, "</li><li>");
        return '<' + type + "><" + innerType + '>' + str + "</" + innerType + "></" + type + '>';

    }

    /**
     * Get string in mediaWiki markup return array of what gonna be HTML blocks.
     *
     * @param wikiText description Indicate whether the element create block by
     * default.
     * @see <a
     * href="https://developer.mozilla.org/en-US/docs/HTML/Block-level_elements">
     * MDN: Block-level elements</a>
     */
    private String[] arrayOfBloks(String wikiText) {
        ArrayList<String> arrayToReturn = new ArrayList<String>();
        String[] linesArray = Util.splitByNewLineSign(wikiText);
        StringBuilder currentBlock;
        for (int i = 0; i < linesArray.length; i++) {
            linesArray[i] = linesArray[i].trim();
        }
        for (int i = 0; i < linesArray.length; i++) {
            currentBlock = new StringBuilder();
            if (linesArray[i].length() == 0) // Ignore empty lines
            {
                continue;
            }
            char firstChar
                    = /*
                     * The kind of the list is determined by the first character
                     * of the line.
                     */ linesArray[i].charAt(0);
            if (ListUtil.isAListChar(firstChar) == true) { // Is it list block?
                do {
                    currentBlock.append(linesArray[i]).append('\n');
                    i++;
                    if (i == linesArray.length) // Reach to the end?
                    {
                        break;
                    }
                    if (linesArray[i].length() == 0) {// Empty line?
                        break;
                    }
                } while (linesArray[i].charAt(0) == firstChar);// Is it still a list block?
                currentBlock = // Remove the last \n from the last line
                        Util.removeNewLineSignFromEnd(currentBlock);
                arrayToReturn.add(currentBlock.toString());
                i--;
            } else if (Util.isNormalText(linesArray[i]) == true) {
                do {
                    currentBlock.append(linesArray[i]).append('\n');
                    i++;
                    if (i == linesArray.length) // Reach to the end?
                    {
                        break;
                    }
                    if (linesArray[i].length() == 0) {// Empty line?
                        break;
                    }
                } while (Util.isNormalText(linesArray[i]) == true
                        && ListUtil.isAListChar(linesArray[i].charAt(0)) == false);
                i--;
                currentBlock = // Remove the last \n from the last line
                        Util.removeNewLineSignFromEnd(currentBlock);
                arrayToReturn.add(currentBlock.toString());
            } else // it's not a list, and not a free text - its get it one block
            {
                currentBlock.append(linesArray[i]).append('\n');
                currentBlock = // Remove the last \n from the last line
                        Util.removeNewLineSignFromEnd(currentBlock);
                arrayToReturn.add(currentBlock.toString());
            }
        }

        String[] returnType = // The right type to return
                new String[arrayToReturn.size()];
        returnType = arrayToReturn.toArray(returnType);

        return returnType;
    }
}
