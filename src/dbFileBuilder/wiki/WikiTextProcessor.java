package dbFileBuilder.wiki;

/**
 * Class with method to convert text in Wiki markup to HTML.<br>
 */
public final class WikiTextProcessor {
    /*
     * Supported Internet protocols.
     */

    private final static String[] WEB_PROTOCOLS = {"http", "ftp", "news"};

    /*
     * Don't let anyone instantiate this class.
     */
    private WikiTextProcessor() {
    }

    /**
     * Get one line in a Wiki markup and return it as HTML.<br>
     * Supported syntax:
     * <blockquote>
     * <pre>
     * Image         [[File:path/to/image/file.jpg]]
     * URL           [http://www.link.com linkName].
     * bold text     '''bold Text'''
     * Italic text   ''italic Text''
     * //TODO: complit the list of supported syntax
     * </pre>
     * </blockquote>
     *
     * @param wikiText to convert to HTML text.
     * @return String with HTML representation of wiki markup.
     */
    public static String processLine(String wikiText) {
       
        String rtn;
        if (wikiText.matches("^={6}[^=]*={6}") == true) {
            rtn = "<h6>";
            rtn += wikiText.substring(6, wikiText.length() - 6);
            rtn += "</h6>";
            return rtn;
        } else if (wikiText.matches("^={5}[^=]*={5}") == true) {
            rtn = "<h5>";
            rtn += wikiText.substring(5, wikiText.length() - 5);
            rtn += "</h5>";
            return rtn;
        } else if (wikiText.matches("^={4}[^=]*={4}") == true) {
            rtn = "<h4>";
            rtn += wikiText.substring(4, wikiText.length() - 4);
            rtn += "</h4>";
            return rtn;
        } else if (wikiText.matches("^={3}[^=]*={3}") == true) {
            rtn = "<h3>";
            rtn += wikiText.substring(3, wikiText.length() - 3);
            rtn += "</h3>";
            return rtn;
        } else if (wikiText.matches("^={2}[^=]*={2}") == true) {
            rtn = Processors.processHeading(wikiText.substring(2, wikiText.length() - 2));
            rtn = "<h2>" + rtn + "</h2>";
            return rtn;
        } else if (dbFileBuilder.RegEx.find(Patterns.LINE, wikiText) == true) {
            return "<hr/>";
        }
        // Image
        int indexOfImage = wikiText.indexOf("[[תמונה:");
        int endIndexOfImage = Util.findClosingBracket(wikiText, indexOfImage);
        //     wikiText.lastIndexOf("]]", indexOfImage + 8);
        while (indexOfImage != -1 && endIndexOfImage != -1) {
            String prefixText = wikiText.substring(0, indexOfImage);// Removes the "[[תמונה:"
            String imageTag = Processors.processImage(wikiText.substring(indexOfImage + 8, endIndexOfImage));
            String suffixText = wikiText.substring(endIndexOfImage + 2);// Removes the "]]"

            wikiText = prefixText + imageTag + suffixText;

            indexOfImage = // Gets the indexes of next image
                    wikiText.indexOf("[[תמונה:", indexOfImage + 7);
            endIndexOfImage = wikiText.lastIndexOf("]]", indexOfImage + 7);
        }

        // URL 
        for (int i = 0; i < WEB_PROTOCOLS.length; i++) {
            int index = wikiText.indexOf("[" + WEB_PROTOCOLS[i] + "://");
            int endIndex = wikiText.indexOf("]", index + 1);
            while (index != -1 && endIndex != -1) {
                String prefixText = wikiText.substring(0, index);
                String aTag = Processors.processURL(wikiText.substring(index + 1, endIndex));
                String suffixText = wikiText.substring(endIndex + 1);

                wikiText = prefixText + aTag + suffixText;

                index = wikiText.indexOf("[" + WEB_PROTOCOLS[i] + "://", endIndex + 1);
                endIndex = wikiText.indexOf("]", index + 1);
            }
        }

        // Inner URL
        int indexOfOpenBracket = wikiText.indexOf("[[");
        int indexOfCloseBracket = wikiText.indexOf("]]", indexOfOpenBracket + 2);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {

            String prefixText = wikiText.substring(0, indexOfOpenBracket);// Removes the "[[File:"
            String aTag = Processors.processInnerURL(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);// Removes the "]]"

            wikiText = prefixText + aTag + suffixText;

            indexOfOpenBracket = // Gets the indexes of next image
                    wikiText.indexOf("[[", indexOfOpenBracket + 2);
            indexOfCloseBracket = wikiText.indexOf("]]", indexOfOpenBracket + 2);
        }

        // register (mishlav)
        indexOfOpenBracket = wikiText.indexOf("{{משלב");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 6);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);
            String text = Processors.processRegister(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);

            wikiText = prefixText + text + suffixText;

            indexOfOpenBracket = // Gets the indexes of next processShortQuote
                    wikiText.indexOf("{{משלב", indexOfOpenBracket + 6);
            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 6);
        }

        // roved lashon (stratum)
        indexOfOpenBracket = wikiText.indexOf("{{רובד|");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 7);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);
            String text = Processors.processStratum(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);

            wikiText = prefixText + text + suffixText;

            indexOfOpenBracket = // Gets the indexes of next processShortQuote
                    wikiText.indexOf("{{רובד|", indexOfOpenBracket + 7);
            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 7);
        }

        // Chidush lashon 
        indexOfOpenBracket = wikiText.indexOf("{{חידוש|");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 8);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);
            String text = Processors.processNewHebrew(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);

            wikiText = prefixText + text + suffixText;

            indexOfOpenBracket = // Gets the indexes of next processShortQuote
                    wikiText.indexOf("{{חידוש|", indexOfOpenBracket + 8);
            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 8);
        }

        // from foreign language (מלועזית)
        indexOfOpenBracket = wikiText.indexOf("{{לועזית|");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 9);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);
            String text = Processors.processForeignLanguage(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);

            wikiText = prefixText + text + suffixText;

            indexOfOpenBracket = // Gets the indexes of next processShortQuote
                    wikiText.indexOf("{{לועזית|", indexOfOpenBracket + 9);
            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 9);
        }

        // Greek
        indexOfOpenBracket = wikiText.indexOf("{{יוונית|");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 9);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);
            String text = Processors.processGreek(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);

            wikiText = prefixText + text + suffixText;

            indexOfOpenBracket = // Gets the indexes of next processShortQuote
                    wikiText.indexOf("{{יוונית|", indexOfOpenBracket + 9);
            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 9);
        }

        // Unicode
        indexOfOpenBracket = wikiText.indexOf("{{יוניקוד|");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 10);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);
            String text = Processors.processUnicode(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);

            wikiText = prefixText + text + suffixText;

            indexOfOpenBracket = // Gets the indexes of next processShortQuote
                    wikiText.indexOf("{{יוניקוד|", indexOfOpenBracket + 10);
            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 10);
        }

        //   Quote short
        indexOfOpenBracket = wikiText.indexOf("{{צט");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 4);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);
            String quoteText = Processors.processShortQuote(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);

            wikiText = prefixText + quoteText + suffixText;

            indexOfOpenBracket = // Gets the indexes of next processShortQuote
                    wikiText.indexOf("{{צט", indexOfOpenBracket + 1);

            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 4);
        }

        // Small  Quote
        indexOfOpenBracket = wikiText.indexOf("{{ציטוטון|");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 8);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);
            String quoteText = Processors.processSmallQuote(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);

            wikiText = prefixText + quoteText + suffixText;

            indexOfOpenBracket = // Gets the indexes of next processShortQuote
                    wikiText.indexOf("{{ציטוטון|", indexOfOpenBracket + 1);

            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 8);
        }

        //   Quote
        indexOfOpenBracket = wikiText.indexOf("{{ציטוט|");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 8);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);
            String quoteText = Processors.processQuote(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);

            wikiText = prefixText + quoteText + suffixText;

            indexOfOpenBracket = // Gets the indexes of next processShortQuote
                    wikiText.indexOf("{{ציטוט|", indexOfOpenBracket + 1);

            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 8);
        }

        // Emphasis
        indexOfOpenBracket = wikiText.indexOf("{{הדגשה|");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 8);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);

            String emTag = Processors.processEmphasis(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);

            wikiText = prefixText + emTag + suffixText;

            indexOfOpenBracket = // Gets the indexes of next image
                    wikiText.indexOf("{{הדגשה|", indexOfOpenBracket + 2);
            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 2);
        }

        // abbreviation
        indexOfOpenBracket = wikiText.indexOf("{{ר\"ת");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 5);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);

            String emTag = Processors.processAbbreviation(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);

            wikiText = prefixText + emTag + suffixText;

            indexOfOpenBracket = // Gets the indexes of next image
                    wikiText.indexOf("{{ר\"ת", indexOfOpenBracket + 5);
            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 5);
        }

        // Translation
        indexOfOpenBracket = wikiText.indexOf("{{ת|");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 4);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);

            String spanTag = Processors.processTranslation(wikiText.substring(indexOfOpenBracket, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);// Removes the "}}"

            wikiText = prefixText + spanTag + suffixText;
            indexOfOpenBracket = // Gets the indexes of next image
                    wikiText.indexOf("{{", indexOfOpenBracket + 4);
            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 2);
        }

        // Inner URL of the second  type
        indexOfOpenBracket = wikiText.indexOf("{{");
        indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 2);
        while (indexOfOpenBracket != -1 && indexOfCloseBracket != -1) {
            String prefixText = wikiText.substring(0, indexOfOpenBracket);
            String aTag = Processors.processInnerURL(wikiText.substring(indexOfOpenBracket + 2, indexOfCloseBracket));
            String suffixText = wikiText.substring(indexOfCloseBracket + 2);// Removes the "]]"
            wikiText = prefixText + aTag + suffixText;

            indexOfOpenBracket = // Gets the indexes of next image
                    wikiText.indexOf("{{", indexOfOpenBracket + 2);
            indexOfCloseBracket = wikiText.indexOf("}}", indexOfOpenBracket + 2);
        }

        int countBold = 0;
        int indexOfApostrophe = wikiText.indexOf("'''");
        while (indexOfApostrophe != -1) {
            if ((countBold % 2) == 0) {
                wikiText = wikiText.replaceFirst("'''", "<b>");
            } else {
                wikiText = wikiText.replaceFirst("'''", "</b>");
            }
            countBold++;
            indexOfApostrophe = wikiText.indexOf("'''", indexOfApostrophe);
        }
        int countItalic = 0;
        indexOfApostrophe = wikiText.indexOf("''");
        while (indexOfApostrophe > -1) {
            if ((countItalic % 2) == 0) {
                wikiText = wikiText.replaceFirst("''", "<i>");
            } else {
                wikiText = wikiText.replaceFirst("''", "</i>");
            }
            countItalic++;
            indexOfApostrophe = wikiText.indexOf("''", indexOfApostrophe);
        }
        wikiText = wikiText.replace("<\\/b><\\/i>", "</i></b>");
        return wikiText;
    }
}
