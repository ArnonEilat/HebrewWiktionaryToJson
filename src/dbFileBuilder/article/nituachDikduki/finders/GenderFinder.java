package dbFileBuilder.article.nituachDikduki.finders;

import dbFileBuilder.utilities.StringUtility;

/**
 * Class to extract gender(מין) from
 * {@link dbFileBuilder.article.nituachDikduki.NituachDikduki} entry.
 */
public final class GenderFinder {

    private GenderFinder() {// Don't let anyone instantiate this class.
    }

    /**
     * Attempt to find gender value in <tt>line</tt>.<br>
     * To maintain consistency the gender values are clustering.<br>
     *
     * @param line of text to process.
     * @return gender value.<br>If none found empty string is returned.
     */
    public static String find(String line) {

        line = line.replace("מין=", "");
        line = StringUtility.removeNewLine(line);

        String tmp = line.replaceAll("\"", "");
        tmp = tmp.replaceAll("'", "");
        tmp = tmp.replaceAll("\\(|\\)", "");
        tmp = tmp.replaceAll(",", "");
        tmp = tmp.replaceAll("  ", " ");

        // Clustering the gender values
        if (tmp.equals("ז") || tmp.equals("זכר")) {
            line = "זכר";
        } else if (tmp.equals("זר") || line.equals("זכר ריבוי") || line.equals("זכר רבים")) {
            line = "זכר רבים";
        } else if (tmp.equals("נ") || tmp.equals("נקבה")) {
            line = "נקבה";
        } else if (tmp.equals("נקבה רבים")) {
            line = "נקבה רבות";
        } else if (tmp.equals("זונ")) {
            line = "זכר ונקבה";
        } // Starting to guess
        else if (tmp.contains("ז")) {
            line = "זכר";
        } else if (tmp.contains("נ")) {
            line = "נקבה";
        } else if (tmp.contains("רבות")) {
            line = "נקבה רבות";
        } else if (tmp.contains("רבים")) {
            line = "זכר רבים";
        }

        //Uncomment to find problems
        //  if (!line.contains("זכר") && !line.contains("נקבה") && !line.isEmpty()) {
        //     System.out.println("GenderFinder: " + line);
        //   }
        return line;
    }
}
