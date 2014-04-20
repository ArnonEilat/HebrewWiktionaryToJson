package dbFileBuilder.article.nituachDikduki.finders;

import dbFileBuilder.utilities.StringUtility;

/**
 * Class to extract netiyot(נטיות) from
 * {@link dbFileBuilder.article.nituachDikduki.NituachDikduki} entry.
 */
public final class NetiyotFinder {

    private NetiyotFinder() {// Don't let anyone instantiate this class.
    }

    /**
     * Attempt to find netiyot(נטיות) value in <tt>line</tt>.<br>
     *
     * @param line of text to process.
     * @return netiyot value.<br>If none found empty string is returned.
     */
    public static String find(String line) {
        line = StringUtility.removeNewLine(line);
        if (line.contains("דרך תצורה=|נטיות=}")) {
            return "";
        }

        if (line.contains("דרך תצורה=")) {
            String[] arr = line.split("\\|נטיות=");
            if (arr.length > 1) {
                arr[1] = arr[1].replaceAll("<[^>]*>", "");
                arr[1] = arr[1].replaceAll("'''|''", "");
                arr[1] = arr[1].replaceAll("\\ \\ ", " ");
                arr[1] = arr[1].replaceAll("[\\[\\{\\}\\|\\]#]*", "");
                if (arr[1].length() >= 3) {
                    return arr[1].trim();
                }
                return "";
            }
        }
        line = line.replace("נטיות=", "");
        line = line.replaceAll("[\\[\\{\\}\\|\\]#]*", "");

        line = line.replaceAll("<[^>]*>", "");
        line = line.replaceAll("'''|''", "");
        line = line.replaceAll("\\ \\ ", " ");

        if (line.equals("אין")) {
            return "אין";
        }
        if (line.trim().length() <= 3) {
            return "";
        }

        return line.trim();
    }
}
